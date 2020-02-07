/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020.subsystems;

import com.revrobotics.ColorSensorV3;
import com.ultime5528.frc2020.Ports;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Config;

public class Roulette extends SubsystemBase implements Loggable {

  @Config
  public static double kVitesseTourner = 0.75;
  @Config
  public static double kTempsTourner = 10;
  private VictorSP moteurRoulette;

  private ColorSensorV3 capteurCouleur;
  @Config
  private final I2C.Port kCapteurCouleurPort = I2C.Port.kOnboard;

  public Roulette() {
    moteurRoulette = new VictorSP(Ports.ROULETTE_MOTEUR);
    capteurCouleur = new ColorSensorV3(kCapteurCouleurPort);
    addChild("Moteur Roulette", moteurRoulette);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("R Roulette", capteurCouleur.getRed());
    SmartDashboard.putNumber("G Roulette", capteurCouleur.getGreen());
    SmartDashboard.putNumber("B Roulette", capteurCouleur.getBlue());
  }

  public void tourner() {
    moteurRoulette.set(kVitesseTourner);
  }

  public void stop() {
    moteurRoulette.set(0.0);
  }
}
