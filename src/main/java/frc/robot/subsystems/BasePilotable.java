/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class BasePilotable extends SubsystemBase {

  private CANSparkMax moteurDroit;
  private CANSparkMax moteurGauche;
  private DifferentialDrive drive;

  public BasePilotable() {
    moteurDroit = new CANSparkMax(Constants.ports.PORT_MOTEUR_DROIT, MotorType.kBrushless);
    moteurGauche = new CANSparkMax(Constants.ports.PORT_MOTEUR_GAUCHE, MotorType.kBrushless);
    drive = new DifferentialDrive(moteurGauche, moteurDroit);

  }

  @Override
  public void periodic() {

  }

  public void drive(double xSpeed, double zRotation) {
    drive.arcadeDrive(xSpeed, zRotation);
  }
}
