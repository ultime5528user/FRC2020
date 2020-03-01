/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020.subsystems;

import static com.ultime5528.util.SparkMaxUtil.handleCANError;

import com.kauailabs.navx.frc.AHRS;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.ControlType;
import com.ultime5528.frc2020.Constants;
import com.ultime5528.frc2020.Ports;
import com.ultime5528.util.LogUtil;
import com.ultime5528.util.SimpleOrientationHistory;

import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Config;
import io.github.oblarg.oblog.annotations.Log;

import static com.ultime5528.util.SparkMaxUtil.*;

@Log.Exclude(exclude = Constants.OBLOG_MATCH)
@Config.Exclude(exclude = Constants.OBLOG_MATCH)
public class BasePilotable extends SubsystemBase implements Loggable {

  public static final double kWheelDiameter = 8 * 0.0254; // 8 pouces en mètres
  public static final double kGearboxRatio = (1.0 / 7.0) * (18.0 / 42.0); // gearbox et poulies
  public static final double kPositionConversionFactor = kGearboxRatio * kWheelDiameter * Math.PI;
  public static final double kVelocityConversionFactor = kPositionConversionFactor / 60;

  public static final double kS = 0.251; // 0.274, 0.151
  public static final double kV = 4.16; // 3.16
  public static final double kA = 0.419; // .419
  public static final SimpleMotorFeedforward kFeedForward = new SimpleMotorFeedforward(kS, kV, kA);

  public static final double kRS = 0.625;
  public static final double kRV = 3.0;
  public static final double kRA = 0.500;
  public static final SimpleMotorFeedforward kRotationFeedForward = new SimpleMotorFeedforward(kRS, kRV, kRA);

  public static final double kTrackWidth = 0.686; // TODO Valider FRC Characterization, refaire depuis la correction
                                                  // gearbox gauche
  public static final DifferentialDriveKinematics kDriveKinematics = new DifferentialDriveKinematics(kTrackWidth);

  public static final double kMaxSpeedMetersPerSecond = 3.0;
  public static final double kMaxAccelerationMetersPerSecondSquared = 5.0;

  public static final double kMaxSpeedRadianPerSecond = toRadial(kMaxSpeedMetersPerSecond);
  public static final double kMaxAccelerationRadianPerSecondSquared = toRadial(kMaxAccelerationMetersPerSecondSquared);

  public static final double kRamseteB = 2.0; // 3
  public static final double kRamseteZeta = 0.7; // 0.2
  public static final double kPDriveVel = 5.0; // 2.0; // 1.41

  public static final SparkMaxConfig kMotorConfig = new SparkMaxConfig(1.0, 40, 50);

  public static final boolean GYRO_REVERSED = true;

  private CANSparkMax moteurDroit;
  private CANSparkMax moteurDroit2;
  private CANSparkMax moteurDroit3;
  private CANSparkMax moteurGauche;
  private CANSparkMax moteurGauche2;
  private CANSparkMax moteurGauche3;

  @Log.Graph(name = "Vitesse Encoder Droit", methodName = "getVelocity", rowIndex = 3, columnIndex = 0, width = 3, height = 2)
  @Log.Graph(name = "Position Encoder Droit", methodName = "getPosition", rowIndex = 3, columnIndex = 2, width = 3, height = 2)
  private CANEncoder encoderDroit;

  @Log.Graph(name = "Vitesse Encoder Gauche", methodName = "getVelocity", rowIndex = 0, columnIndex = 0, width = 3, height = 2)
  @Log.Graph(name = "Position Encoder Gauche", methodName = "getPosition", rowIndex = 0, columnIndex = 2, width = 3, height = 2)
  private CANEncoder encoderGauche;

  private CANPIDController controllerGauche;
  private CANPIDController controllerDroit;

  private PIDController pidRotation = new PIDController(0.2, 0.0, 0.0);

  private AHRS gyro;
  private SimpleOrientationHistory orientation_history;

  private DifferentialDrive drive;

  private DifferentialDriveOdometry odometry;

  private Translation2d position = new Translation2d();

  public BasePilotable() {
    if (Constants.ENABLE_CAN_BASE_PILOTABLE) {

      moteurDroit = new CANSparkMax(Ports.BASE_PILOTABLE_MOTEUR_DROIT1, MotorType.kBrushless);
      moteurDroit2 = new CANSparkMax(Ports.BASE_PILOTABLE_MOTEUR_DROIT2, MotorType.kBrushless);
      moteurDroit3 = new CANSparkMax(Ports.BASE_PILOTABLE_MOTEUR_DROIT3, MotorType.kBrushless);

      moteurGauche = new CANSparkMax(Ports.BASE_PILOTABLE_MOTEUR_GAUCHE1, MotorType.kBrushless);
      moteurGauche2 = new CANSparkMax(Ports.BASE_PILOTABLE_MOTEUR_GAUCHE2, MotorType.kBrushless);
      moteurGauche3 = new CANSparkMax(Ports.BASE_PILOTABLE_MOTEUR_GAUCHE3, MotorType.kBrushless);

      configureMasterMotor(moteurDroit, kMotorConfig);
      configureSlaveMotor(moteurDroit2, moteurDroit, kMotorConfig);
      configureSlaveMotor(moteurDroit3, moteurDroit, kMotorConfig);
      controllerDroit = moteurDroit.getPIDController();

      configureMasterMotor(moteurGauche, kMotorConfig);
      configureSlaveMotor(moteurGauche2, moteurGauche, kMotorConfig);
      configureSlaveMotor(moteurGauche3, moteurGauche, kMotorConfig);
      controllerGauche = moteurGauche.getPIDController();
      moteurGauche.setInverted(true);

      encoderDroit = moteurDroit.getEncoder();
      configureEncoder(encoderDroit);

      encoderGauche = moteurGauche.getEncoder();
      configureEncoder(encoderGauche);

      drive = new DifferentialDrive(moteurGauche, moteurDroit);
      drive.setRightSideInverted(false);
    }

    gyro = new AHRS(Port.kUSB);
    addChild("navX", gyro);
    gyro.reset();

    orientation_history = new SimpleOrientationHistory((int)(5/TimedRobot.kDefaultPeriod));
  
    odometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(getClampedHeading()));
  }

  private void configureEncoder(CANEncoder encoder) {
    handleCANError(encoder.setPositionConversionFactor(kPositionConversionFactor), "setPositionConversionFactor");
    handleCANError(encoder.setVelocityConversionFactor(kVelocityConversionFactor), "setVelocityConversionFactor");
    handleCANError(encoder.setPosition(0.0), "setPosition(0.0)");
  }

  @Override
  public void periodic() {
    orientation_history.addAngle(getGyroTimestamp(), getAngleDegrees());

    // Update the odometry in the periodic block
    if (Constants.ENABLE_CAN_BASE_PILOTABLE) {
      odometry.update(Rotation2d.fromDegrees(getClampedHeading()), encoderGauche.getPosition(),
          encoderDroit.getPosition());
      handleFaults();

      SmartDashboard.putNumber("left encoder", encoderGauche.getPosition());
      SmartDashboard.putNumber("right encoder", encoderDroit.getPosition());

      position = getPose().getTranslation();

      SmartDashboard.putNumber("angle", getAngleDegrees());
      SmartDashboard.putNumber("x", odometry.getPoseMeters().getTranslation().getX());
      SmartDashboard.putNumber("y", odometry.getPoseMeters().getTranslation().getY());
    }

  }

  public Pose2d getPose() {
    return odometry.getPoseMeters();
  }

  public double getX() {
    return position.getX();
  }

  public double getY() {
    return position.getY();
  }

  public DifferentialDriveWheelSpeeds getWheelSpeeds() {
    if (Constants.ENABLE_CAN_BASE_PILOTABLE) {
      return new DifferentialDriveWheelSpeeds(encoderGauche.getVelocity(), encoderDroit.getVelocity());
    } else {
      return new DifferentialDriveWheelSpeeds(0.0, 0.0);
    }
  }

  public void resetOdometry(Pose2d pose) { // TODO Méthode vraiment nécessaire? C'est quelque chose que l'on veut éviter
    resetEncoders();
    odometry.resetPosition(pose, Rotation2d.fromDegrees(getClampedHeading()));
  }

  public void drive(double xSpeed, double zRotation) {
    if (Constants.ENABLE_CAN_BASE_PILOTABLE) {
      drive.arcadeDrive(-xSpeed, -zRotation);
    }
  }

  /**
   * Resets the drive encoders to currently read a position of 0.
   */
  public void resetEncoders() {
    if (Constants.ENABLE_CAN_BASE_PILOTABLE) {
      encoderGauche.setPosition(0);
      encoderDroit.setPosition(0);
    }
  }
  public void resetGyro(){
    gyro.reset();
  }

  /**
   * Gets the average distance of the two encoders.
   *
   * @return the average of the two encoder readings
   */
  public double getAverageEncoderDistance() {
    if (Constants.ENABLE_CAN_BASE_PILOTABLE) {
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

  public double getPositionEncoderGauche() {
    return encoderGauche.getPosition();
}

  /**
   * Gets the right drive encoder.
   *
   * @return the right drive encoder
   */
  public CANEncoder getRightEncoder() {
    return encoderGauche;
  }

  public double getPositionEncoderDroit() {
      return encoderDroit.getPosition();
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

  public long getGyroTimestamp() {
    return gyro.getLastSensorTimestamp();
  }

  public double getAngleAtGyroTimestamp(long timestamp) {
    return orientation_history.getAngleAtTimestamp(timestamp);
  }

  /**
   * Zeroes the heading of the robot.
   */
  public void zeroHeading() {
    gyro.reset();
  }

  /**
   * @return la valeur du gyro, en valeur continue, en degrés.
   */
  public double getAngleDegrees() {
    return (GYRO_REVERSED ? -1.0 : 1.0) * gyro.getAngle();
  }

  /**
   * @return la valeur du gyro, en valeur continue, en degrés.
   */
  public double getAngleRadians() {
    return Math.toRadians(getAngleDegrees());
  }

  /**
   * Returns the heading of the robot.
   *
   * @return the robot's heading in degrees, from -180 to 180
   */
  public double getClampedHeading() {
    // return Math.IEEEremainder(getAngleDegrees(), 360);
    return getAngleDegrees();
  }

  /**
   * Returns the turn rate of the robot.
   *
   * @return The turn rate of the robot, in degrees per second
   */
  public double getTurnRate() {
    return gyro.getRate() * (GYRO_REVERSED ? -1.0 : 1.0);
  }

  public void resetPID() {
    pidRotation.reset();
  }

  private void handleFaults() {
    handleSlaveFault(moteurDroit2, moteurDroit, "Moteur Droit 2");
    handleSlaveFault(moteurDroit3, moteurDroit, "Moteur Droit 3");
    handleSlaveFault(moteurGauche2, moteurGauche, "Moteur Gauche 2");
    handleSlaveFault(moteurGauche3, moteurGauche, "Moteur Gauche 3");
  }

  private void handleSlaveFault(CANSparkMax slave, CANSparkMax master, String name) {
    if (slave.getStickyFault(CANSparkMax.FaultID.kHasReset)) {
      LogUtil.reportWarning("CAN", name + " reset!");
      handleCANError(slave.follow(master), "follow master", slave);
      handleCANError(slave.clearFaults(), "slave clearFaults", slave);
    }
  }

  public void tankDriveVolts(double leftVolts, double rightVolts) {
    if (Constants.ENABLE_CAN_BASE_PILOTABLE) {
      handleCANError(controllerGauche.setReference(leftVolts, ControlType.kVoltage), "tankDriveVolts setReference",
          moteurGauche);
      handleCANError(controllerDroit.setReference(rightVolts, ControlType.kVoltage), "tankDriveVolts setReference",
          moteurDroit);
      drive.feed();
    }
  }

  /**
   * Fais tourner le robot jusqu'à un angle absolu, à l'aide d'un PIDSVA.
   * 
   * @param angleDegrees l'objectif absolu à atteindre, en degrés.
   * @param speeds       les vitesses souhaitées, en m/s.
   * @param prevSpeeds   les vitesses souhaitées à l'itération précédente (pour
   *                     calculer l'accélération), en m/s.
   */
  public void turnToAngle(double angleDegrees, DifferentialDriveWheelSpeeds speeds,
      DifferentialDriveWheelSpeeds prevSpeeds) {
    if (Constants.ENABLE_CAN_BASE_PILOTABLE) {

      double leftFeedforward = kRotationFeedForward.calculate(speeds.leftMetersPerSecond,
          (speeds.leftMetersPerSecond - prevSpeeds.leftMetersPerSecond) / TimedRobot.kDefaultPeriod);

      double rightFeedforward = kRotationFeedForward.calculate(speeds.rightMetersPerSecond,
          (speeds.rightMetersPerSecond - prevSpeeds.rightMetersPerSecond) / TimedRobot.kDefaultPeriod);

      double pidCorrection = pidRotation.calculate(getAngleDegrees(), angleDegrees);
      SmartDashboard.putNumber("pid correction", pidCorrection);

      /*
       * Si le PID donne une correction positive, il faut tourner plus vite dans les
       * angles positifs. Une vitesse angulaire positive fait tourner le robot dans le
       * sens anti-horaire, donc il faut additionner la correction au côté droit et la
       * soustraire du côté gauche.
       */
      double leftOutput = leftFeedforward - pidCorrection;
      double rightOutput = rightFeedforward + pidCorrection;

      tankDriveVolts(leftOutput, rightOutput);

    }
  }

  public static PIDController createPIDController() {
    return new PIDController(kPDriveVel, 0, 0);
  }

  public static double toRadial(double lin) {
    return lin / (kTrackWidth / 2.0);
  }

  public static double toLinear(double a) {
    return a * (kTrackWidth / 2.0);
  }

}