package com.ultime5528.frc2020.commands.basepilotable;

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

  public static double kTolerance = 3.0;

  protected BasePilotable basePilotable;

  private final TrapezoidProfile.Constraints constraints;
  private TrapezoidProfile.State goal = new TrapezoidProfile.State();
  private TrapezoidProfile.State current = new TrapezoidProfile.State();

  private DifferentialDriveWheelSpeeds prevSpeeds = new DifferentialDriveWheelSpeeds();

  /**
   * 
   * @param basePilotable
   * @param vitesse vitesse maximale, en rad/s
   * @param accel accélération maximale, en rad/s^2
   */
  public AbstractTourner(BasePilotable basePilotable, double vitesse, double accel) {
    this.basePilotable = basePilotable;
    this.constraints = new TrapezoidProfile.Constraints(vitesse, accel);
    addRequirements(basePilotable);
  }

  @Override
  public void initialize() {
    basePilotable.resetPID();
    current = new TrapezoidProfile.State(basePilotable.getAngleRadians(), 0.0);
    prevSpeeds = new DifferentialDriveWheelSpeeds();
  }

  @Override
  public void execute() {

    double goalAngleDegrees = calculateGoalAngleDegrees();
    double goalAngleRad = Math.toRadians(goalAngleDegrees);
    goal = new TrapezoidProfile.State(goalAngleRad, 0);

    TrapezoidProfile profile = new TrapezoidProfile(constraints, goal, current);

    current = profile.calculate(TimedRobot.kDefaultPeriod);
    
    var speeds = BasePilotable.kDriveKinematics
        .toWheelSpeeds(new ChassisSpeeds(0, 0, current.velocity));

    SmartDashboard.putNumber("goal", speeds.leftMetersPerSecond);
    SmartDashboard.putNumber("current", basePilotable.getLeftEncoder().getVelocity());

    basePilotable.turnToAngle(Math.toDegrees(current.position), speeds, prevSpeeds);

    prevSpeeds = speeds;
  }

  @Override
  public void end(boolean interrupted) {
    basePilotable.drive(0, 0);
  }

  public boolean isFinished() {
    return current.position >= goal.position && Math.abs(basePilotable.getAngleDegrees() - current.position) < kTolerance;
  }

  /**
   * Retourne l'angle absolu que l'on veut atteindre, en degrés.
   */
  public abstract double calculateGoalAngleDegrees();
    
}