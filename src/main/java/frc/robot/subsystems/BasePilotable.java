/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SendableRegistry;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class BasePilotable extends SubsystemBase {

  public static final double kWheelDiameter = 8 * 0.0254;
  public static final double kGearboxRatio = 1 / 10.75;
  public static final double kPositionConversionFactor = kGearboxRatio * kWheelDiameter * Math.PI;
  public static final double kVelocityConversionFactor = kPositionConversionFactor / 60;
  public static final double kRampRate = 0;
  public static final double kS = 0;
  public static final double kV = 0;
  public static final double kA = 0;
  public static final DifferentialDriveKinematics kDriveKinematics = new DifferentialDriveKinematics(0.541);
  public static final double kMaxSpeedMetersPerSecond = 0;
  public static final double kMaxAccelerationMetersPerSecondSquared = 0;
  public static final double kRamseteB = 0;
  public static final double kRamseteZeta = 0;
  public static final double kPDriveVel = 0;

  public static final boolean GYRO_REVERSED = false;

  private CANSparkMax moteurDroit;
  private CANSparkMax moteurGauche;

  private CANEncoder encoderDroit;
  private CANEncoder encoderGauche;

  private AHRS gyro;

  private DifferentialDrive drive;

  private DifferentialDriveOdometry odometry;

  public BasePilotable() {
    
    if (Constants.ENABLE_CAN) {
      moteurDroit = new CANSparkMax(Constants.Ports.BASE_PILOTABLE_MOTEUR_DROIT, MotorType.kBrushless);
      moteurGauche = new CANSparkMax(Constants.Ports.BASE_PILOTABLE_MOTEUR_GAUCHE, MotorType.kBrushless);
  
      configureMotor(moteurDroit);
      configureMotor(moteurGauche);
      
      encoderDroit = moteurDroit.getEncoder();
      encoderGauche = moteurGauche.getEncoder();
      
      drive = new DifferentialDrive(moteurGauche, moteurDroit);
    }

    gyro = new AHRS();
    SendableRegistry.addLW(gyro, getSubsystem(), "navX");

    odometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(getHeading()));
  }

  public void configureMotor(CANSparkMax motor) {
    motor.restoreFactoryDefaults();

    motor.enableVoltageCompensation(12.0);
    motor.setClosedLoopRampRate(kRampRate);
    motor.getEncoder().setPositionConversionFactor(kPositionConversionFactor);
    motor.getEncoder().setVelocityConversionFactor(kVelocityConversionFactor);
  }

  @Override
  public void periodic() {
    // Update the odometry in the periodic block
    if (Constants.ENABLE_CAN) {
      odometry.update(Rotation2d.fromDegrees(getHeading()), encoderGauche.getPosition(), encoderDroit.getPosition());
    }
  }

  public Pose2d getPose() {
    return odometry.getPoseMeters();
  }

  public DifferentialDriveWheelSpeeds getWheelSpeeds() {
    if (Constants.ENABLE_CAN) {
      return new DifferentialDriveWheelSpeeds(encoderGauche.getVelocity(), encoderDroit.getVelocity());
    } else {
      return new DifferentialDriveWheelSpeeds(0.0, 0.0);
    }
  }

  public void resetOdometry(Pose2d pose) {
    resetEncoders();
    odometry.resetPosition(pose, Rotation2d.fromDegrees(getHeading()));
  }

  public void drive(double xSpeed, double zRotation) {
    if (Constants.ENABLE_CAN) {
      drive.arcadeDrive(xSpeed, zRotation);
    }
  }

  /**
   * Resets the drive encoders to currently read a position of 0.
   */
  public void resetEncoders() {
    if (Constants.ENABLE_CAN) {
      encoderGauche.setPosition(0);
      encoderDroit.setPosition(0);
    }
  }

  /**
   * Gets the average distance of the two encoders.
   *
   * @return the average of the two encoder readings
   */
  public double getAverageEncoderDistance() {
    if (Constants.ENABLE_CAN) {
      return (encoderGauche.getPosition() + encoderDroit.getPosition()) / 2.0;
    } else {
      return 0.0;
    }
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
   * Sets the max output of the drive. Useful for scaling the drive to drive more
   * slowly.
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
   * @return the robot's heading in degrees, from -180 to 180
   */
  public double getHeading() {
    return Math.IEEEremainder(gyro.getAngle(), 360) * (GYRO_REVERSED ? -1.0 : 1.0); // TODO Vraiment nécessaire IEEEremainer?
  }

  /**
   * Returns the turn rate of the robot.
   *
   * @return The turn rate of the robot, in degrees per second
   */
  public double getTurnRate() {
    return gyro.getRate() * (GYRO_REVERSED ? -1.0 : 1.0);
  }

  public void tankDriveVolts(double leftVolts, double rightVolts) {
    if (Constants.ENABLE_CAN) {
      moteurGauche.setVoltage(leftVolts);
      moteurDroit.setVoltage(-rightVolts); // TODO Vérifier si négatif est nécessaire
    }
  }

}
