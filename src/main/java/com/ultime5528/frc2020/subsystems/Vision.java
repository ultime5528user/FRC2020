/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020.subsystems;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Vision {

  private NetworkTableEntry snapshotEntry;

  private static Vision instance;

  private static double kFOV = 49.8; // Degrés
  private static double kFocale = 1 / Math.tan(Math.toRadians(kFOV) / 2);

  private static VisionSnapshot currentSnapshot;

  private Vision() {
    snapshotEntry = NetworkTableInstance.getDefault().getTable("Vision").getEntry("Snapshot");
  }

  public static Vision getInstance() {
    if (instance == null)
      instance = new Vision();
    instance.readSnapshot();
    return instance;
  }

  public static void readSnapshot(){
    //currentSnapshot = VisionSnapshot.from(snapshotEntry.getString());
  }

  public static OptionalDouble getLargeurCible() {
    if (getInstance().currentSnapshot.found) {
      return OptionalDouble.of(getInstance().currentSnapshot.largeur);
    } else {
      return OptionalDouble.empty();
    }
  }

  public static OptionalDouble getAngleCible() {
    if (getInstance().currentSnapshot.found) {
      double x = getInstance().currentSnapshot.xpos;
      double angle = Math.atan(kFocale / x);

      return OptionalDouble.of(angle);
    } else {
      return OptionalDouble.empty();
    }
  }

  public static class VisionSnapshot {

    public final long timestamp;

    public final boolean found;
    public final double xpos;
    public final double largeur;

    public VisionSnapshot(long timestamp, boolean found, double xpos, double largeur) {
      this.timestamp = timestamp;

      this.found = found;
      this.xpos = xpos;
      this.largeur = largeur;
    }

    public static VisionSnapshot from(String fromString) {
      List<String> splitted = Arrays.asList(fromString.split(";"));

      return new VisionSnapshot(Long.parseLong(splitted.get(0)), Integer.parseInt(splitted.get(1)) == 1,
          Double.parseDouble(splitted.get(2)), Double.parseDouble(splitted.get(3)));
    }

  }

}
