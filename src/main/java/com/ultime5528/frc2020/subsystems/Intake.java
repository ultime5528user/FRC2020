/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020.subsystems;

import com.ultime5528.frc2020.Ports;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.LinearFilter;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {

  private VictorSP moteurIntake;
  private VictorSP moteurTransporteur;
  private VictorSP moteurBrasGauche;
  private VictorSP moteurBrasDroit;

  private DigitalInput photocellBas;
  private DigitalInput photocellHaut;

  private LinearFilter linearFilter;

  private PowerDistributionPanel pdp;

  public static final double kVitesseAvaler = 0.5;
  public static final double kVitesseTransporter = -0.5;
  public static final double kCurrentFilterTime = 2;
  public static final double kMaxCurrent = 20;
  public static final double kVitesseBrasGauche = 0.5;
  public static final double kVitesseBrasDroit = -kVitesseBrasGauche;

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
    if (linearFilter.calculate(pdp.getCurrent(Ports.PDP.INTAKE_MOTEUR_TRANSPORTEUR)) >= kMaxCurrent) {
      stopTransporteur = true;
    } else {
      stopTransporteur = false;
    }
  }

  public void prendreBallon() {
    moteurIntake.set(kVitesseAvaler);
  }

  public void transporter() {
    if (!stopTransporteur) {
      moteurTransporteur.set(kVitesseTransporter);
    } else {
      moteurTransporteur.set(0.0);
    }
  }

  public void actionnerBras() {
    moteurBrasGauche.set(kVitesseBrasGauche);
    moteurBrasDroit.set(kVitesseBrasDroit);
  }

  public void stopIntake() {
    moteurIntake.set(0.0);
  }

  public void stopTransporteur() {
    moteurTransporteur.set(0.0);
  }

  public void stopBras() {
    moteurBrasGauche.set(0.0);
    moteurBrasDroit.set(0.0);
  }

  public boolean hasBallonBas() {
    return photocellBas.get();
  }

  public boolean hasBallonHaut() {
    return photocellHaut.get();
  }

  public void transporterInverse() {
    moteurTransporteur.set(-kVitesseTransporter);
  }

  public void prendreBallonInverse() {
    moteurIntake.set(-kVitesseAvaler);
  }

}
