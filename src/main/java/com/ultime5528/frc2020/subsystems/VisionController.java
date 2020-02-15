/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020.subsystems;

import java.util.OptionalDouble;

import com.ultime5528.frc2020.Ports;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class VisionController extends SubsystemBase {

  private NetworkTableEntry snapshotEntry;
  private NetworkTableEntry angleEntry;
  private Relay led;

  private static double kFOV = 49.8; // Degrés
  private static double kFocale = 1 / Math.tan(Math.toRadians(kFOV) / 2);

  private VisionSnapshot currentSnapshot;

  private boolean isEnabled = false;

  private BasePilotable basePilotable;

  public VisionController(BasePilotable basePilotable) {
    this.basePilotable = basePilotable;

    snapshotEntry = NetworkTableInstance.getDefault().getTable("Vision").getEntry("Snapshot");
    angleEntry = NetworkTableInstance.getDefault().getTable("Vision").getEntry("RobotAngle");
    led = new Relay(Ports.VISION_LED);

    readSnapshot();
  }

  @Override
  public void periodic() {
    if (isEnabled) {
      readSnapshot();
    }

    //Send angle to RPi
    angleEntry.setValue(basePilotable.getGyroAngle());
  }

  public void enable() {
    isEnabled = true;
    
    led.set(Value.kOn);
  }

  public void disable() {
    isEnabled = false;
    led.set(Value.kOff);
  }

  public void readSnapshot(){
    String data = snapshotEntry.getString("0;false;0;0");
    currentSnapshot = VisionSnapshot.fromRasperryPiData(data);
  }

  public OptionalDouble getHauteurCible() {
    if (currentSnapshot.found) {
      return OptionalDouble.of(currentSnapshot.hauteur);
    } else {
      return OptionalDouble.empty();
    }
  }

  public OptionalDouble getAngleCible() {
    if (currentSnapshot.found) {
      double x = currentSnapshot.centreX;
      double angle = Math.atan(x / kFocale);
      
      //correction de l'angle
      angle -= (basePilotable.getGyroAngle() - currentSnapshot.angle);

      return OptionalDouble.of(angle);
    } else {
      return OptionalDouble.empty();
    }
  }

  public static class VisionSnapshot {

    public final double angle;
    public final boolean found;
    public final double centreX;
    public final double hauteur;

    public VisionSnapshot() {
      this(0L, false, 0.0, 0.0);
    }

    public VisionSnapshot(double angle, boolean found, double centreX, double hauteur) {
      this.angle = angle;
      this.found = found;
      this.centreX = centreX;
      this.hauteur = hauteur;
    }

    /**
     * Convertis en VisionSnapshot une chaîne de caractères sous
     * le format "timestamp<long>;found<boolean>;centreX<double>;hauteur<double>"
     */
    public static VisionSnapshot fromRasperryPiData(String data) {

      VisionSnapshot snapshot = null;

      try {
        String[] splitted = data.split(";");

        snapshot = new VisionSnapshot(
            Double.parseDouble(splitted[0]),
            Boolean.parseBoolean(splitted[1]),
            Double.parseDouble(splitted[2]),
            Double.parseDouble(splitted[3]));

      } catch (Exception e) {
        DriverStation.reportError("Erreur de formatage dans fromRasperryPiData() : " + data, false);
        snapshot = new VisionSnapshot();
      }

      return snapshot;
    }

  }

}
