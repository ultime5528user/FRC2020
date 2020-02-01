/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020.commands;

import java.util.OptionalDouble;

import com.ultime5528.frc2020.subsystems.BasePilotable;
import com.ultime5528.frc2020.subsystems.Vision;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class Viser extends CommandBase {

  private BasePilotable basePilotable;

  private final TrapezoidProfile.Constraints constraints = new TrapezoidProfile.Constraints(BasePilotable.kMaxSpeedRadianPerSecond, BasePilotable.kMaxAccelerationRadianPerSecondSquared);
  private TrapezoidProfile.State goal = new TrapezoidProfile.State();
  private TrapezoidProfile.State current = new TrapezoidProfile.State();

  private OptionalDouble angle;

  private DifferentialDriveWheelSpeeds prevSpeeds = new DifferentialDriveWheelSpeeds();

  public Viser(BasePilotable basePilotable) {
    addRequirements(basePilotable);

    this.basePilotable = basePilotable;
  }

  @Override
  public void initialize() {
  }

  @Override
  public void execute() {
    // current.position = basePilotable.getHeading();
    // current.velocity = basePilotable.getTurnRate();

    angle = Vision.getAngleCible();
    goal = new TrapezoidProfile.State(angle.orElse(0), 0);

    TrapezoidProfile profile = new TrapezoidProfile(constraints, goal, current);

    current = profile.calculate(TimedRobot.kDefaultPeriod);

    var speeds = BasePilotable.kDriveKinematics.toWheelSpeeds(new ChassisSpeeds(0, 0, Math.toRadians(current.velocity)));

    basePilotable.tankDriveSpeeds(speeds, prevSpeeds);

    prevSpeeds = speeds;
  }

  @Override
  public void end(boolean interrupted) {
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
