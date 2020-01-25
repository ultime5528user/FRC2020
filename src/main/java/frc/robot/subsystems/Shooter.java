/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Shooter extends SubsystemBase {
  
  public static double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM;
  public static double kRPM = 2500;
  public static double kTempsTir = 5;

  private CANSparkMax moteur;
  private CANEncoder encoder;
  private CANPIDController pidController;

  public Shooter() {
    moteur = new CANSparkMax(Constants.Ports.SHOOTER_MOTEUR, MotorType.kBrushless);

    pidController = moteur.getPIDController();
    pidController.setFeedbackDevice(encoder);

    kP = 5e-5;
    kI = 1e-6;
    kD = 0;
    kIz = 0;
    kFF = 0;
    kMaxOutput = 1;
    kMinOutput = -1;
    maxRPM = 5700;

    pidController.setP(kP);
    pidController.setI(kI);
    pidController.setD(kD);
    pidController.setIZone(kIz);
    pidController.setFF(kFF);
    pidController.setOutputRange(kMinOutput, kMaxOutput);

  }

  @Override
  public void periodic() {

  }

  public void tirer() {
    pidController.setReference(kRPM, ControlType.kVelocity);

  }

  public void stop() {
    pidController.setReference(0, ControlType.kVelocity);

  }
}
