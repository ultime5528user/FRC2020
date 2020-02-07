/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.LinearFilter;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SendableRegistry;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ultime5528.frc2020.Constants;
import com.ultime5528.frc2020.RobotContainer;

public class Intake extends SubsystemBase {

  private VictorSP moteurIntake;
  private VictorSP moteurTransporteur;

  private DigitalInput photocellBas;
  private DigitalInput photocellHaut;

  private LinearFilter linearFilter;

  private PowerDistributionPanel pdp;

  public static final double kVitesseAvaler = 0.5;
  public static final double kVitesseTransporter = 0.5;
  public static final double kCurrentFilterTime = 2;
  public static final double kMaxCurrent = 20;

  private boolean stopTransporteur = false;



  public Intake(PowerDistributionPanel pdp) {

    moteurIntake = new VictorSP(Constants.Ports.INTAKE_MOTEUR);
    moteurTransporteur = new VictorSP(Constants.Ports.TRANSPORTEUR_MOTEUR);
    SendableRegistry.addLW(moteurIntake, getSubsystem(), "Moteur Intake");
    SendableRegistry.addLW(moteurTransporteur, getSubsystem(), "Moteur Transporteur");

    photocellBas = new DigitalInput(Constants.Ports.INTAKE_PHOTOCELL_BAS);
    photocellHaut = new DigitalInput(Constants.Ports.INTAKE_PHOTOCELL_HAUT);
    SendableRegistry.addLW(photocellHaut, getSubsystem(), "Photocell Haut");
    SendableRegistry.addLW(photocellBas, getSubsystem(), "Photocell Bas");

    linearFilter = LinearFilter.singlePoleIIR(kCurrentFilterTime, TimedRobot.kDefaultPeriod);
    this.pdp = pdp;

  }

  @Override
  public void periodic() {
   if( linearFilter.calculate(pdp.getCurrent(Constants.PDP.INTAKE_MOTEUR_TRANSPORTEUR)) >= kMaxCurrent){
    stopTransporteur = true;
   } else{
    stopTransporteur = false;
   }
  }

  public void avaler() {
    moteurIntake.set(kVitesseAvaler);
  }

  public void transporter() {
    if(!stopTransporteur){
    moteurTransporteur.set(kVitesseTransporter);
  } else {moteurTransporteur.set(0.0);}
  }

  public void stopIntake() {
    moteurIntake.set(0.0);
  }

  public void stopTransporteur() {
    moteurTransporteur.set(0.0);
  }

  public boolean hasBallonBas() {
    return photocellBas.get();
  }

  public boolean hasBallonHaut() {
    return photocellHaut.get();
  }
}
