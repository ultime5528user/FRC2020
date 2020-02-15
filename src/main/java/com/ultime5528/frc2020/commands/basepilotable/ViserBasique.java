/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020.commands.basepilotable;

import java.util.OptionalDouble;

import com.ultime5528.frc2020.subsystems.BasePilotable;
import com.ultime5528.frc2020.subsystems.VisionController;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ViserBasique extends CommandBase {
  private BasePilotable basePilotable;
  private VisionController vision;

  private OptionalDouble angle;

  public static double kTolerance = 0.1;
  public static double kS = 0.3;
  public static double kP = 0.3;

  public ViserBasique(BasePilotable basePilotable, VisionController vision) {
    this.basePilotable = basePilotable;
    this.vision = vision;
    addRequirements(basePilotable, vision);

  }

  @Override
  public void initialize() {
    vision.enable();
  }

  @Override
  public void execute() {
    angle = vision.getAngleCible();
    if (angle.isPresent()) {
      double vitesseRotation = Math.signum(angle.getAsDouble()) * kS + kP * angle.getAsDouble();
      basePilotable.drive(0, (vitesseRotation));
    } else {
      basePilotable.drive(0, 0);
    }
  }

  @Override
  public void end(boolean interrupted) {
    basePilotable.drive(0, 0);
    vision.disable();
  }

  @Override
  public boolean isFinished() {
    if (angle.isPresent()) {

      return Math.abs(angle.getAsDouble()) < kTolerance;
    } else {
      return false;
    }
  }
}
