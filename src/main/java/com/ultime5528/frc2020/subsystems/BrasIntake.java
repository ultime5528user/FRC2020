/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class BrasIntake extends SubsystemBase {

  private VictorSP moteur;
  private Encoder encoder;

  public static double kVitesseMonter = 5;
  public static double kVitesseDescendre = 3;
  
  public BrasIntake(int portMoteur, int portEncoderA, int portEncoderB) {
    
    moteur = new VictorSP(portMoteur);
    encoder = new Encoder(portEncoderA,portEncoderB);

  }

  @Override
  public void periodic() {

  }
  public void monterBras(){
    moteur.setVoltage(kVitesseMonter);

  }
  public void descendreBras(){
    moteur.setVoltage(kVitesseDescendre);
  }
  public void stopBras(){
    moteur.setVoltage(0.0);
  }
  public double getVitesse(){
   return encoder.getRate();
  }
  

}
