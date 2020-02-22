/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SendableRegistry;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Config;
import io.github.oblarg.oblog.annotations.Log;

public class Grimpeur extends SubsystemBase implements Loggable {

  private Servo ratchet;
  private VictorSP moteur;
  private DigitalInput limitSwitchHaut;
  private DigitalInput limitSwitchBas;

  private String name;

  @Config(rowIndex = 0, columnIndex = 0, width = 2, height = 2)
  private double ratchetLocked;
  @Config(rowIndex = 2, columnIndex = 0, width = 2, height = 2)
  private double ratchetUnlocked;

  @Config(rowIndex = 4, columnIndex = 0, width = 2, height = 2)
  public static double kVitesseDescendre = -6;
  @Config(rowIndex = 0, columnIndex = 2, width = 2, height = 2)
  public static double kVitesseMonter = 1;
  @Config(rowIndex = 0, columnIndex = 2, width = 2, height = 2)
  public static double kVitesseGrimper = -12;

  /**
   * Creates a new Grimpeur.
   */
  public Grimpeur(int portServo, int portMoteur, int portLimitSwitchHaut, int portLimitSwitchBas, double ratchetLocked,
      double ratchetUnlocked, String name) {
    SendableRegistry.addLW(this, name, name);
    ratchet = new Servo(portServo);
    moteur = new VictorSP(portMoteur);
    limitSwitchBas = new DigitalInput(portLimitSwitchBas);
    limitSwitchHaut = new DigitalInput(portLimitSwitchHaut);

    this.ratchetLocked = ratchetLocked;
    this.ratchetUnlocked = ratchetUnlocked;

    this.name = name;
    setName(name);

    addChild("Servo Grimpeur", ratchet);
    addChild("Moteur Grimpeur", moteur);
    addChild("Limit Switch Haut", limitSwitchHaut);
    addChild("Limit Switch Bas", limitSwitchBas);
  }

  @Override
  public void periodic() {

  }

  @Override
  public String configureLogName() {
    return name;
  }

  public void stop() {
    // ratchet.set(ratchetLocked);
    moteur.setVoltage(0);
  }

  public void monter() {
    ratchet.set(ratchetUnlocked);
    moteur.setVoltage(kVitesseMonter);
  }

  public void descendre() {
    ratchet.set(ratchetLocked);
    moteur.setVoltage(kVitesseDescendre);
  }

  public void grimper() {
    ratchet.set(ratchetLocked);
    moteur.setVoltage(kVitesseGrimper);
  }

  public void grimperSansRatchet() {
    ratchet.set(ratchetUnlocked);
    moteur.setVoltage(kVitesseGrimper);
  }

  @Log.BooleanBox(rowIndex = 0, columnIndex = 4)
  public boolean estEnBas() {
    return !limitSwitchBas.get();
  }

  @Log.BooleanBox(rowIndex = 3, columnIndex = 4)
  public boolean estEnHaut() {
    return limitSwitchHaut.get();
  }
}
