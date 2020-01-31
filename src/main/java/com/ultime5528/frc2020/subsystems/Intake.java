/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SendableRegistry;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ultime5528.frc2020.Constants;

public class Intake extends SubsystemBase {

  private VictorSP moteurIntake;
  private VictorSP moteurTransporteur;

  private DigitalInput photocellBas;
  private DigitalInput photocellHaut;

  public static final double kVitesseAvaler = 0.5;
  public static final double kVitesseTransporter = 0.5;

  public Intake() {

    moteurIntake = new VictorSP(Constants.Ports.INTAKE_MOTEUR);
    moteurTransporteur = new VictorSP(Constants.Ports.TRANSPORTEUR_MOTEUR);
    SendableRegistry.addLW(moteurIntake, getSubsystem(), "Moteur Intake");
    SendableRegistry.addLW(moteurTransporteur, getSubsystem(), "Moteur Transporteur");

    photocellBas = new DigitalInput(Constants.Ports.PHOTOCELL_BAS);
    photocellHaut = new DigitalInput(Constants.Ports.PHOTOCELL_HAUT);
    SendableRegistry.addLW(photocellHaut, getSubsystem(), "Photocell Haut");
    SendableRegistry.addLW(photocellBas, getSubsystem(), "Photocell Bas");

  }

  @Override
  public void periodic() {

  }

  public void avaler() {
    moteurIntake.set(kVitesseAvaler);
  }

  public void transporter() {
    moteurTransporteur.set(kVitesseTransporter);
  }

  public void stopIntake() {
    moteurIntake.set(0.0);
  }

  public void stopTransporteur() {
    moteurTransporteur.set(0.0);
  }
public boolean hasBallonBas(){
  return photocellBas.get();
}
public boolean hasBallonHaut(){
  return photocellHaut.get();
}
}
