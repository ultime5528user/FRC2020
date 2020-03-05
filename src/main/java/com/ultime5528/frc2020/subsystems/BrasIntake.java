/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020.subsystems;

import com.ultime5528.frc2020.Constants;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SendableRegistry;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import io.github.oblarg.oblog.annotations.Config;
import io.github.oblarg.oblog.annotations.Log;

@Log.Exclude(exclude = Constants.OBLOG_MATCH)
@Config.Exclude(exclude = Constants.OBLOG_MATCH)
public class BrasIntake extends SubsystemBase {

  private VictorSP moteur;
  private Encoder encoder;
  private double inverted;

  @Config
  public static double kVitesseMonter = -7;
  
  @Config
  public static double kVitesseDescendre = 7;

  @Config
  public static double kHauteurGauche = -82;
  
  @Config
  public static double kHauteurDroit = -77;
  
  public BrasIntake(int portMoteur, int portEncoderA, int portEncoderB, boolean inverted, String name) {

    SendableRegistry.addLW(this, name, name);
    setName(name);

    this.inverted = (inverted ? -1.0 : 1.0);

    moteur = new VictorSP(portMoteur);
    addChild("moteur", moteur);
    encoder = new Encoder(portEncoderA, portEncoderB);
    addChild("encoder", encoder);

  }

  @Override
  public void periodic() {

  }

  public void monterBras() {
    moteur.setVoltage(kVitesseMonter * inverted);

  }

  public void descendreBras() {
    moteur.setVoltage(kVitesseDescendre * inverted);
  }

  public void stopBras() {
    moteur.setVoltage(0.0);
  }

  public double getVitesse() {
    return encoder.getRate();
  }

  public double getPosition() {
    return encoder.getDistance() * inverted;
  }

  public void resetEncoder() {
    encoder.reset();
  }

}
