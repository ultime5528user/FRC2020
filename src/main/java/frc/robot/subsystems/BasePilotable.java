/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class BasePilotable extends SubsystemBase {

  public static final double WHEEL_DIAMETER = 8 * 0.0254;
  public static final double GEARBOX_RATIO = 1 / 10.0;
  public static final double POSITION_CONVERSION_FACTOR = GEARBOX_RATIO * WHEEL_DIAMETER * Math.PI;
  public static final double VELOCITY_CONVERSION_FACTOR = POSITION_CONVERSION_FACTOR / 60;
  public static final double RAMP_RATE = 0;

  public static final boolean GYRO_REVERSED = false;

  private CANSparkMax moteurDroit;
  private CANSparkMax moteurGauche;

  private CANEncoder encoderDroit;
  private CANEncoder encoderGauche;

  private Gyro gyro = new ADXRS450_Gyro();

  private DifferentialDrive drive;

  private DifferentialDriveOdometry odometry;

  public BasePilotable() {
    moteurDroit = new CANSparkMax(Constants.Ports.PORT_MOTEUR_DROIT, MotorType.kBrushless);
    moteurGauche = new CANSparkMax(Constants.Ports.PORT_MOTEUR_GAUCHE, MotorType.kBrushless);

    configureMotor(moteurDroit);
    configureMotor(moteurGauche);

    encoderDroit = moteurDroit.getEncoder();
    encoderGauche = moteurGauche.getEncoder();

    drive = new DifferentialDrive(moteurGauche, moteurDroit);

    odometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(getHeading()));
  }

  public void configureMotor(CANSparkMax motor) {
    motor.restoreFactoryDefaults();

    motor.enableVoltageCompensation(12.0);
    motor.setClosedLoopRampRate(RAMP_RATE);
    motor.getEncoder().setPositionConversionFactor(POSITION_CONVERSION_FACTOR);
    motor.getEncoder().setVelocityConversionFactor(VELOCITY_CONVERSION_FACTOR);
  }

  @Override
  public void periodic() {
    // Update the odometry in the periodic block
    odometry.update(Rotation2d.fromDegrees(getHeading()), encoderGauche.getPosition(),
                      encoderDroit.getPosition());
  }

  public Pose2d getPose() {
    return odometry.getPoseMeters();
  }

  public DifferentialDriveWheelSpeeds getWheelSpeeds() {
    return new DifferentialDriveWheelSpeeds(encoderGauche.getVelocity(), encoderDroit.getVelocity());
  }

  public void resetOdometry(Pose2d pose) {
    resetEncoders();
    odometry.resetPosition(pose, Rotation2d.fromDegrees(getHeading()));
  }

  public void drive(double xSpeed, double zRotation) {
    drive.arcadeDrive(xSpeed, zRotation);
  }

  /**
   * Resets the drive encoders to currently read a position of 0.
   */
  public void resetEncoders() {
    encoderGauche.setPosition(0);
    encoderDroit.setPosition(0);
  }

  /**
   * Gets the average distance of the two encoders.
   *
   * @return the average of the two encoder readings
   */
  public double getAverageEncoderDistance() {
    return (encoderGauche.getPosition() + encoderDroit.getPosition()) / 2.0;
  }

  /**
   * Gets the left drive encoder.
   *
   * @return the left drive encoder
   */
  public CANEncoder getLeftEncoder() {
    return encoderGauche;
  }

  /**
   * Gets the right drive encoder.
   *
   * @return the right drive encoder
   */
  public CANEncoder getRightEncoder() {
    return encoderGauche;
  }

  /**
   * Sets the max output of the drive.  Useful for scaling the drive to drive more slowly.
   *
   * @param maxOutput the maximum output to which the drive will be constrained
   */
  public void setMaxOutput(double maxOutput) {
    drive.setMaxOutput(maxOutput);
  }

  /**
   * Zeroes the heading of the robot.
   */
  public void zeroHeading() {
    gyro.reset();
  }

  /**
   * Returns the heading of the robot.
   *
   * @return the robot's heading in degrees, from 180 to 180
   */
  public double getHeading() {
    return Math.IEEEremainder(gyro.getAngle(), 360) * (GYRO_REVERSED ? -1.0 : 1.0);
  }

  /**
   * Returns the turn rate of the robot.
   *
   * @return The turn rate of the robot, in degrees per second
   */
  public double getTurnRate() {
    return gyro.getRate() * (GYRO_REVERSED ? -1.0 : 1.0);
  }

}
