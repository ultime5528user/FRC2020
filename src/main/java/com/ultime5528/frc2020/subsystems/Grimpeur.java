/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020.subsystems;

import com.ultime5528.frc2020.Constants;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SendableRegistry;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Config;
import io.github.oblarg.oblog.annotations.Log;

@Log.Exclude(exclude = Constants.OBLOG_MATCH)
@Config.Exclude(exclude = Constants.OBLOG_MATCH)
public class Grimpeur extends SubsystemBase implements Loggable {

  @Log(rowIndex = 1, columnIndex = 0, methodName = "get")
  private Servo ratchet;
  @Log(rowIndex = 2, columnIndex = 0, methodName = "get")
  private VictorSP moteur;
  private DigitalInput limitSwitchHaut;
  private DigitalInput limitSwitchBas;

  private String name;

  private double inversed;

  @Log.BooleanBox(rowIndex = 0, columnIndex = 0)
  private boolean isLocked = false;

  @Config(rowIndex = 0, columnIndex = 1)
  private double ratchetLocked;
  @Config(rowIndex = 1, columnIndex = 1)
  private double ratchetUnlocked;

  @Config(rowIndex = 2, columnIndex = 1)
  public static double kVitesseDescendre = -6;
  @Config(rowIndex = 3, columnIndex = 1)
  public static double kVitesseMonter = 12;
  @Config(rowIndex = 4, columnIndex = 1)
  public static double kVitesseGrimper = -12;

  /**
   * Creates a new Grimpeur.
   */
  public Grimpeur(int portServo, int portMoteur, int portLimitSwitchHaut, int portLimitSwitchBas, double ratchetLocked,
      double ratchetUnlocked, boolean inversed, String name) {

    SendableRegistry.addLW(this, name, name);
    this.name = name;
    setName(name);
    ratchet = new Servo(portServo);
    moteur = new VictorSP(portMoteur);
    limitSwitchBas = new DigitalInput(portLimitSwitchBas);
    limitSwitchHaut = new DigitalInput(portLimitSwitchHaut);

    this.ratchetLocked = ratchetLocked;
    this.ratchetUnlocked = ratchetUnlocked;
    this.inversed = (inversed ? -1.0 : 1.0);

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
    if (!isLocked) {
      ratchet.set(ratchetUnlocked);
      moteur.setVoltage(inversed * kVitesseMonter);
    }
  }

  public void descendre() {
    ratchet.set(ratchetLocked);
    moteur.setVoltage(inversed * kVitesseDescendre);
    isLocked = true;
  }

  public void grimper() {
    ratchet.set(ratchetLocked);
    moteur.setVoltage(inversed * kVitesseGrimper);
    isLocked = true;
  }

  public void grimperSansRatchet() {
    ratchet.set(ratchetUnlocked);
    moteur.setVoltage(inversed * kVitesseGrimper);
    isLocked = false;
  }

  @Log.BooleanBox(rowIndex = 3, columnIndex = 0)
  public boolean estEnBas() {
    return !limitSwitchBas.get();
  }

  @Log.BooleanBox(rowIndex = 4, columnIndex = 0)
  public boolean estEnHaut() {
    return limitSwitchHaut.get();
  }

  public void unlockRatchet() {
    ratchet.set(ratchetUnlocked);
    isLocked = false;
  }

}
