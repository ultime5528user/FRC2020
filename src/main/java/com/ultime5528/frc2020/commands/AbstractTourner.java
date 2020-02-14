package com.ultime5528.frc2020.commands;

import com.ultime5528.frc2020.subsystems.BasePilotable;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * AbstractTourner
 */
public abstract class AbstractTourner extends CommandBase {

  public static double kTolerance = 0.1;

  protected BasePilotable basePilotable;

  private final TrapezoidProfile.Constraints constraints = new TrapezoidProfile.Constraints(
      BasePilotable.kMaxSpeedRadianPerSecond, BasePilotable.kMaxAccelerationRadianPerSecondSquared);
  private TrapezoidProfile.State goal = new TrapezoidProfile.State();
  private TrapezoidProfile.State current = new TrapezoidProfile.State();

  private DifferentialDriveWheelSpeeds prevSpeeds = new DifferentialDriveWheelSpeeds();

  public AbstractTourner(BasePilotable basePilotable) {
    this.basePilotable = basePilotable;
    addRequirements(basePilotable);
  }

  @Override
  public void initialize() {
    basePilotable.resetPID();
    current = new TrapezoidProfile.State();
    prevSpeeds = new DifferentialDriveWheelSpeeds();
  }

  @Override
  public void execute() {
    // current.position = basePilotable.getHeading();
    // current.velocity = basePilotable.getTurnRate();

    double goalAngle = calculateGoalAngle();
    goal = new TrapezoidProfile.State(goalAngle, 0);

    TrapezoidProfile profile = new TrapezoidProfile(constraints, goal, current);

    current = profile.calculate(TimedRobot.kDefaultPeriod);

    var speeds = BasePilotable.kDriveKinematics
        .toWheelSpeeds(new ChassisSpeeds(0, 0, Math.toRadians(current.velocity)));

    SmartDashboard.putNumber("goal", speeds.leftMetersPerSecond);
    SmartDashboard.putNumber("current", basePilotable.getLeftEncoder().getVelocity());

    basePilotable.tankDriveSpeeds(speeds, prevSpeeds);

    prevSpeeds = speeds;
  }

  @Override
  public void end(boolean interrupted) {
    basePilotable.drive(0, 0);
  }

  public abstract boolean isFinished();

  public abstract double calculateGoalAngle();
    
}