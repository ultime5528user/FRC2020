/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ultime5528.frc2020.Constants;
import com.ultime5528.util.LinearInterpolator;
import com.ultime5528.util.Point;

import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Config;
import io.github.oblarg.oblog.annotations.Log;

public class Shooter extends SubsystemBase implements Loggable {

  public static double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput;

  @Config(rowIndex = 1, columnIndex = 3, width = 1, height = 1)
  public static double kRPM = 2500;

  @Config(rowIndex = 1, columnIndex = 3, width = 1, height = 1)
  public static double kPrecision = 0.95;

  @Config.NumberSlider(min = 0, max = 60, rowIndex = 0, columnIndex = 3, width = 2, height = 1)
  public static double kTempsTir = 5;

  @Config(rowIndex = 1, columnIndex = 3, width = 1, height = 1)
  public static double kCourbure = 0;

  @Config(rowIndex = 1, columnIndex = 3, width = 1, height = 1)
  public static double kDeadzoneY = 0;

  @Config(rowIndex = 1, columnIndex = 3, width = 1, height = 1)
  public static double kDeadzoneX = 0;

  private CANSparkMax moteur;
  private CANSparkMax moteur2;
  @Log.Graph(name = "Vitesse Encoder Shooter", methodName = "getVelocity", rowIndex = 0, columnIndex = 0, width = 3, height = 2)
  private CANEncoder encoder;

  @Config(rowIndex = 2, columnIndex = 0, width = 1, height = 1, methodName = "setP")
  @Config(rowIndex = 2, columnIndex = 1, width = 1, height = 1, methodName = "setI")
  @Config(rowIndex = 3, columnIndex = 0, width = 1, height = 1, methodName = "setD")
  @Config(rowIndex = 3, columnIndex = 1, width = 1, height = 1, methodName = "setFF")
  private CANPIDController pidController;

  private LinearInterpolator interpolator = new LinearInterpolator(
      new Point[] { new Point(0.0, 0.0), new Point(1.0, 1.0) });

  public Shooter() {

    kP = 5e-5;
    kI = 1e-6;
    kD = 0;
    kIz = 0;
    kFF = 0;
    kMaxOutput = 1;
    kMinOutput = -1;

    if (Constants.ENABLE_CAN_SHOOTER) {
      moteur = new CANSparkMax(Constants.Ports.SHOOTER_MOTEUR, MotorType.kBrushless);
      moteur2 = new CANSparkMax(Constants.Ports.SHOOTER_MOTEUR2, MotorType.kBrushless);

      pidController = moteur.getPIDController();
      pidController.setFeedbackDevice(encoder);
      pidController.setP(kP);
      pidController.setI(kI);
      pidController.setD(kD);
      pidController.setIZone(kIz);
      pidController.setFF(kFF);
      pidController.setOutputRange(kMinOutput, kMaxOutput);

      moteur2.follow(moteur);
      moteur2.setInverted(true);

    }
  }

  @Override
  public void periodic() {

  }

  public void tirer() {
    if (Constants.ENABLE_CAN_SHOOTER) {
      if (Constants.ENABLE_VISION) {

        Vision.getLargeurCible().ifPresentOrElse(
          
          largeur -> {
            double vitesse = interpolator.interpolate(largeur);
            pidController.setReference(vitesse, ControlType.kVelocity);
          },
          
          () -> pidController.setReference(kRPM, ControlType.kVelocity) //TODO est-ce qu'on veut vraiment kRPM si on voit pas la cible?

        );

      } else {
        pidController.setReference(kRPM, ControlType.kVelocity);
      }

    }
  }

  public void stop() {
    if (Constants.ENABLE_CAN_SHOOTER) {
      pidController.setReference(0, ControlType.kVelocity);
    }
  }

  public double getVitesse() {
    return encoder.getVelocity();
  }
}
