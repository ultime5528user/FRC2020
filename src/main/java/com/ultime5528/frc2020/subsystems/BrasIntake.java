/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SendableRegistry;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class BrasIntake extends SubsystemBase {

  private VictorSP moteur;
  private Encoder encoder;

  public static double kVitesseMonter = -8;
  public static double kVitesseDescendre = 8;
  
  public BrasIntake(int portMoteur, int portEncoderA, int portEncoderB, String name) {

    SendableRegistry.addLW(this,name,name);
    setName(name);
    
    moteur = new VictorSP(portMoteur);
    addChild("moteur", moteur);
    encoder = new Encoder(portEncoderA,portEncoderB);
    addChild("encoder", encoder);

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

  public double getPosition(){
    return encoder.getDistance();
  }  

}
