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

public class Intake extends SubsystemBase implements Loggable {

  private VictorSP moteurIntake;
  private VictorSP moteurTransporteur;
  private VictorSP moteurBrasGauche;
  private VictorSP moteurBrasDroit;

  private DigitalInput photocellBas;
  private DigitalInput photocellHaut;

  private LinearFilter linearFilter;

  private PowerDistributionPanel pdp;

  @Config(rowIndex = 0, columnIndex = 0, width = 2, height = 1)
  private int ballonDansIntake;

  public static final double kVitesseAvaler = 4;
  public static final double kVitesseTransporter = -6.7; // -7.2
  public static final double kCurrentFilterTime = 2;
  public static final double kMaxCurrent = 12;
  public static final double kVitesseBrasGauche = -6;
  public static final double kVitesseBrasDroit = 6;
  public static final double kVitesseBrasGaucheBalayer = 10;
  public static final double kVitesseBrasDroitBalayer = -10;
  public static final double kTempsStopIntake = 0.2;

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
    if (Constants.ENABLE_PDP
        && linearFilter.calculate(pdp.getCurrent(Ports.PDP.INTAKE_MOTEUR_TRANSPORTEUR)) >= kMaxCurrent) {
      stopTransporteur = true;
    } else {
      stopTransporteur = false;
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

  public boolean hasBallonBas() {
    return photocellBas.get();
  }

  public boolean hasBallonHaut() {
    return photocellHaut.get();
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
    moteurIntake.setVoltage(-kVitesseAvaler - 0.4);
  }


}
