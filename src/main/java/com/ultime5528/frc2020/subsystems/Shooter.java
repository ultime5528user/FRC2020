/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020.subsystems;

import java.util.OptionalDouble;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ultime5528.frc2020.Constants;
import com.ultime5528.frc2020.Ports;
import com.ultime5528.util.LinearInterpolator;
import com.ultime5528.util.Point;
import com.ultime5528.util.SparkMaxUtil;
import com.ultime5528.util.SparkMaxUtil.SparkMaxConfig;

import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Config;
import io.github.oblarg.oblog.annotations.Log;

@Log.Exclude(exclude = Constants.OBLOG_MATCH)
@Config.Exclude(exclude = Constants.OBLOG_MATCH)
public class Shooter extends SubsystemBase implements Loggable {

  public static double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput;

  @Config(rowIndex = 1, columnIndex = 2, width = 2, height = 1)
  public static double kRPM = 3500;

  @Config(rowIndex = 2, columnIndex = 2, width = 2, height = 1)
  public static double kToleranceVitesse = 0.05;

  @Config.NumberSlider(min = 0, max = 60, rowIndex = 0, columnIndex = 2, width = 2, height = 1)
  public static double kTempsTir = 5;
  private CANSparkMax moteur;
  private CANSparkMax moteur2;

  public static final SparkMaxConfig kMotorConfig = new SparkMaxConfig(0.1, 40, 50);

  @Log.Graph(name = "Vitesse Encoder Shooter", methodName = "getVelocity", rowIndex = 0, columnIndex = 4, width = 5, height = 5)
  private CANEncoder encoder;

  @Config(rowIndex = 0, columnIndex = 0, width = 2, height = 2, methodName = "setP", methodTypes = { double.class })
  @Config(rowIndex = 2, columnIndex = 0, width = 2, height = 2, methodName = "setI", methodTypes = { double.class })
  @Config(rowIndex = 0, columnIndex = 2, width = 2, height = 2, methodName = "setD", methodTypes = { double.class })
  @Config(rowIndex = 2, columnIndex = 2, width = 2, height = 2, methodName = "setFF", methodTypes = { double.class })
  private CANPIDController pidController;

  private LinearInterpolator interpolator = new LinearInterpolator(new Point[] { new Point(0.077, 3100),
      new Point(0.082, 2900), new Point(0.0916, 2600), new Point(0.0958, 2500), new Point(0.1, 2400),
      new Point(0.105, 2400), new Point(0.11, 2400), new Point(0.116, 2350), new Point(0.12, 2325),
      new Point(0.129, 2325), new Point(0.133, 2300), new Point(0.141, 2290), new Point(0.145, 2290),
      new Point(0.15, 2270), new Point(0.155, 2250), new Point(0.159, 2240), new Point(0.164, 2230),
      new Point(0.171, 2230), new Point(0.179, 2220), new Point(0.1875, 2210), new Point(0.197, 2200) });

  public Shooter() {

    kP = 0.0004;
    kI = 1e-6;
    kD = 0;
    kIz = 0;
    kFF = 0;
    kMaxOutput = 1;
    kMinOutput = -1;

    if (Constants.ENABLE_CAN_SHOOTER) {
      moteur = new CANSparkMax(Ports.SHOOTER_MOTEUR, MotorType.kBrushless);
      moteur2 = new CANSparkMax(Ports.SHOOTER_MOTEUR2, MotorType.kBrushless);

      SparkMaxUtil.configureMasterMotor(moteur, kMotorConfig);
      SparkMaxUtil.configureSlaveMotor(moteur2, moteur, kMotorConfig, true);

      encoder = moteur.getEncoder();
      pidController = moteur.getPIDController();
      pidController.setP(kP);
      pidController.setI(kI);
      pidController.setD(kD);
      pidController.setIZone(kIz);
      pidController.setFF(kFF);
      pidController.setOutputRange(kMinOutput, kMaxOutput);

    }
  }

  @Override
  public void periodic() {
  }

  public double tirer(OptionalDouble optHauteur) {
    double vitesse = kRPM;
    if (Constants.ENABLE_CAN_SHOOTER) {
      if (Constants.ENABLE_VISION && optHauteur.isPresent()) {
        double hauteur = optHauteur.getAsDouble();
        vitesse = interpolator.interpolate(hauteur);
        SmartDashboard.putNumber("Vitesse", vitesse);
        SmartDashboard.putNumber("Hauteur", hauteur);
      } 
      pidController.setReference(vitesse, ControlType.kVelocity);
    }

    return vitesse;
  }

  public void stop() {
    if (Constants.ENABLE_CAN_SHOOTER) {
      pidController.setReference(0, ControlType.kVoltage);
    }
  }

  public double getVitesse() {
    if (Constants.ENABLE_CAN_SHOOTER) {
      return encoder.getVelocity();
    } else {
      return 0.0;
    }
  }

}
