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

public class Vision {

  private NetworkTableEntry xEntry;
  private NetworkTableEntry largeurEntry;
  private NetworkTableEntry foundEntry;

  private static Vision instance;

  private static double kFOV = 49.8; //Degrés
  private static double kFocale = 1/Math.tan(Math.toRadians(kFOV)/2);

  private Vision() {
    xEntry = NetworkTableInstance.getDefault().getTable("Vision").getEntry("xcible");
    largeurEntry = NetworkTableInstance.getDefault().getTable("Vision").getEntry("largeurcible");
    foundEntry = NetworkTableInstance.getDefault().getTable("Vision").getEntry("found");
  }

  public static Vision getInstance(){
    if(instance == null)
      instance = new Vision();
    return instance;
  }

  public static OptionalDouble getLargeurCible(){
    if(getInstance().foundEntry.getBoolean(false)){
      return OptionalDouble.of(getInstance().largeurEntry.getDouble(0.0));
    } else {
      return OptionalDouble.empty();
    }
  }

  public static OptionalDouble getAngleCible(){
    if(getInstance().foundEntry.getBoolean(false)){
      double x = getInstance().xEntry.getDouble(0);
      double angle = Math.atan(kFocale / x);

      return OptionalDouble.of(angle);
    } else {
      return OptionalDouble.empty();
    }
  }
}
