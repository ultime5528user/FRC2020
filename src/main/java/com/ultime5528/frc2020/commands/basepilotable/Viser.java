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

public class Viser extends AbstractTourner {

  public static double kTolerance = 0.1;

  private VisionController vision;

  private OptionalDouble angle;

  public Viser(BasePilotable basePilotable, VisionController vision) {
    super(basePilotable, 3.14, 3.14); // TODO Vraies valeurs
    this.vision = vision;
    addRequirements(vision);
  }

  @Override
  public void initialize() {
    super.initialize();
    vision.enable();
  }

  @Override
  public double calculateGoalAngleDegrees() {
    angle = vision.getAngleCible();
    return basePilotable.getAngleAtGyroTimestamp(vision.getLastTimestamp()) + angle.orElse(0.0);
  }

  @Override
  public void end(boolean interrupted) {
    super.end(interrupted);
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
