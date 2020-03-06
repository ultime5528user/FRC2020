/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020.subsystems;

import com.ultime5528.frc2020.Constants;
import com.ultime5528.frc2020.Ports;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.LinearFilter;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Config;
import io.github.oblarg.oblog.annotations.Log;

@Log.Exclude(exclude = Constants.OBLOG_MATCH)
@Config.Exclude(exclude = Constants.OBLOG_MATCH)
public class Intake extends SubsystemBase implements Loggable {

  @Log(methodName = "get")
  private VictorSP moteurIntake;
  @Log(methodName = "get")
  private VictorSP moteurTransporteur;
  @Log(methodName = "get")
  private VictorSP moteurBrasGauche;
  @Log(methodName = "get")
  private VictorSP moteurBrasDroit;

  private DigitalInput photocellBas;
  private DigitalInput photocellHaut;

  private LinearFilter linearFilter;

  private PowerDistributionPanel pdp;

  private boolean isGobing = false;

  @Log
  private int ballonDansIntake;
  
  @Config
  public static double kVitesseAvaler = 4;
  @Config
  public static double kVitesseTransporter = -6.7; // -7.2

  public final static double kCurrentFilterTime = 1.0;
  
  @Config
  public static double kMaxCurrent = 12;
  @Config
  public static double kVitesseBrasGauche = -6;
  @Config
  public static double kVitesseBrasDroit = 6;
  @Config
  public static double kVitesseBrasGaucheBalayer = 10;
  @Config
  public static double kVitesseBrasDroitBalayer = -10;
  @Config
  public static double kTempsStopIntake = 0.2;

  private boolean stopTransporteur = false;

  public Intake(PowerDistributionPanel pdp) {

    moteurIntake = new VictorSP(Ports.INTAKE_MOTEUR_INTAKE);
    addChild("Moteur Intake", moteurIntake);

    moteurTransporteur = new VictorSP(Ports.INTAKE_MOTEUR_TRANSPORTEUR);
    addChild("Moteur Transporteur", moteurTransporteur);

    moteurBrasGauche = new VictorSP(Ports.INTAKE_MOTEUR_BRAS_GAUCHE);
    addChild("Moteur bras gauche", moteurBrasGauche);

    moteurBrasDroit = new VictorSP(Ports.INTAKE_MOTEUR_BRAS_DROIT);
    addChild("Moteur bras droit", moteurBrasDroit);

    photocellBas = new DigitalInput(Ports.INTAKE_PHOTOCELL_BAS);
    addChild("Photocell Bas", photocellBas);

    photocellHaut = new DigitalInput(Ports.INTAKE_PHOTOCELL_HAUT);
    addChild("Photocell Haut", photocellHaut);

    linearFilter = LinearFilter.singlePoleIIR(kCurrentFilterTime, TimedRobot.kDefaultPeriod);
    this.pdp = pdp;

  }

  @Override
  public void periodic() {
    if (Constants.ENABLE_PDP && linearFilter.calculate(getCurrent()) >= kMaxCurrent) {
      stopTransporteur = true;
    } else {
      stopTransporteur = false;
    }
  }

  @Log
  private double getCurrent() {
    if (Constants.ENABLE_PDP) {
      return pdp.getCurrent(Ports.PDP.INTAKE_MOTEUR_TRANSPORTEUR);
    } else {
      return 0.0;
    }
  }

  public void prendreBallon() {
    moteurIntake.setVoltage(kVitesseAvaler);
  }

  public void transporter() {
    if (!stopTransporteur) {
      moteurTransporteur.setVoltage(kVitesseTransporter);
    } else {
      moteurTransporteur.setVoltage(0.0);
    }
  }
  
  public void actionnerBras() {
    moteurBrasGauche.setVoltage(kVitesseBrasGauche);
    moteurBrasDroit.setVoltage(kVitesseBrasDroit);
  }
  
  public void actionnerBrasInverse() {
    moteurBrasGauche.setVoltage(kVitesseBrasGaucheBalayer);
    moteurBrasDroit.setVoltage(kVitesseBrasDroitBalayer);
  }

  public void stopIntake() {
    moteurIntake.setVoltage(0.0);
  }

  public void stopTransporteur() {
    moteurTransporteur.setVoltage(0.0);
  }

  public void stopBras() {
    moteurBrasGauche.setVoltage(0.0);
    moteurBrasDroit.setVoltage(0.0);
  }

  @Log.BooleanBox
  public boolean hasBallonBas() {
    return photocellBas.get();
  }
  
  @Log.BooleanBox
  public boolean hasBallonHaut() {
    return photocellHaut.get();
  }

  public void setGobing(boolean value) {
    this.isGobing = value;
  }
 
  public boolean isGobing() {
    return isGobing;
  }

  public int getNombreBallonsDansIntake(){
    return ballonDansIntake;
  }

  public void ballonDePlus() {
    ballonDansIntake += 1;
  }

  public void resetBallonDansIntake() {
    ballonDansIntake = 0;
  }

  public void transporterInverse() {
    moteurTransporteur.setVoltage(-kVitesseTransporter);
  }

  public void prendreBallonInverse() {
    moteurIntake.setVoltage(-kVitesseAvaler - 1);
  }

}
