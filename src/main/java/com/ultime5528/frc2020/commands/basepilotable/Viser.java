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

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Viser extends AbstractTourner {

  public static double kTolerance = 0.5; // en degrés

  private VisionController vision;

  private OptionalDouble angle;
  private double goalAngle;
  private long lastTimestamp = 0;

  public Viser(BasePilotable basePilotable, VisionController vision) {
    super(basePilotable, 0.75, 0.5);
    this.vision = vision;
    addRequirements(vision);
  }

  @Override
  public void initialize() {
    super.initialize();
    vision.enable();
    goalAngle = basePilotable.getAngleDegrees();
    angle = OptionalDouble.empty();
  }

  @Override
  public double calculateGoalAngleDegrees() {

    long timestamp = vision.getLastTimestamp();
    angle = vision.getAngleCible();

    if (angle.isPresent() && timestamp != lastTimestamp) {
      lastTimestamp = timestamp;
      goalAngle = basePilotable.getAngleAtGyroTimestamp(timestamp) + angle.orElse(0.0);
    }

    return goalAngle;

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
