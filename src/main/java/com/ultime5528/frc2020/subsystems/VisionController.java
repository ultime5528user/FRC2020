/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020.subsystems;

import java.util.OptionalDouble;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class VisionController extends SubsystemBase {

  private NetworkTableEntry snapshotEntry;

  private static double kFOV = 49.8; // Degrés
  private static double kFocale = 1 / Math.tan(Math.toRadians(kFOV) / 2);

  private VisionSnapshot currentSnapshot;

  private boolean isEnabled = false;

  public VisionController() {
    snapshotEntry = NetworkTableInstance.getDefault().getTable("Vision").getEntry("Snapshot");
  }

  @Override
  public void periodic() {
    if (isEnabled) {
      readSnapshot();
    }
  }

  public void enable() {
    isEnabled = true;
  }

  public void disable() {
    isEnabled = false;
  }

  public void readSnapshot(){
    String data = snapshotEntry.getString("0;0;false;0");
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
      double angle = Math.atan(kFocale / x);

      return OptionalDouble.of(angle);
    } else {
      return OptionalDouble.empty();
    }
  }

  public static class VisionSnapshot {

    public final long timestamp;
    public final boolean found;
    public final double centreX;
    public final double hauteur;

    public VisionSnapshot(long timestamp, boolean found, double centreX, double hauteur) {
      this.timestamp = timestamp;

      this.found = found;
      this.centreX = centreX;
      this.hauteur = hauteur;
    }

    /**
     * Convertis en VisionSnapshot une chaîne de caractères sous
     * le format "timestamp<long>;found<boolean>;centreX<double>;hauteur<double>"
     */
    public static VisionSnapshot fromRasperryPiData(String data) {
      String[] splitted = data.split(";");

      return new VisionSnapshot(
        Long.parseLong(splitted[0]),
        Boolean.parseBoolean(splitted[1]),
        Double.parseDouble(splitted[2]),
        Double.parseDouble(splitted[3]));
    }

  }

}
