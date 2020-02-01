/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020.subsystems;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SendableRegistry;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Config;

public class Grimpeur extends SubsystemBase implements Loggable {

  private Servo ratchet;
  private VictorSP moteur;

  @Config
  public static double kRatchetLocked = 0;
  @Config
  public static double kRatchetUnlocked = 90;

  @Config
  public static double kVitesseDescendre = -1;
  @Config
  public static double kVitesseMonter = 1;
  @Config
  public static double kVitesseGrimper = 0.5;

  /**
   * Creates a new Grimpeur.
   */
  public Grimpeur(int portServo, int portMoteur, String name) {
    ratchet = new Servo(portServo);
    moteur = new VictorSP(portMoteur);
    SendableRegistry.addLW(ratchet, getSubsystem(), "Servo Grimpeur");
    SendableRegistry.addLW(moteur, getSubsystem(), "Moteur Grimpeur");

  }

  @Override
  public void periodic() {
  }

  public void stop() {
    ratchet.setAngle(kRatchetLocked);
    moteur.set(0);
  }

  public void monter() {
    ratchet.setAngle(kRatchetUnlocked);
    moteur.set(kVitesseMonter);
  }

  public void descendre() {
    ratchet.setAngle(kRatchetLocked);
    moteur.set(kVitesseDescendre);
  }

  public void grimper() {
    ratchet.setAngle(kRatchetLocked);
    moteur.set(kVitesseGrimper);
  }
}
