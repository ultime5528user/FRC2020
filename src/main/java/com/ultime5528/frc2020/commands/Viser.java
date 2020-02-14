/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020.commands;

import java.util.OptionalDouble;

import com.ultime5528.frc2020.subsystems.BasePilotable;
import com.ultime5528.frc2020.subsystems.VisionController;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class Viser extends AbstractTourner {

  public static double kTolerance = 0.1;

  private VisionController vision;

  private OptionalDouble angle;

  public Viser(BasePilotable basePilotable, VisionController vision) {
    super(basePilotable);
    this.vision = vision;
    addRequirements(vision);
  }

  @Override
  public void initialize() {
    super.initialize();
    vision.enable();
  }

  @Override
  public double calculateGoalAngle() {
    angle = vision.getAngleCible();
    return angle.orElse(0.0) + basePilotable.getGyroAngle();
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
