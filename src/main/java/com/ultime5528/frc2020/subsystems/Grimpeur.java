/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020.subsystems;

import com.revrobotics.CANDigitalInput.LimitSwitch;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Config;

public class Grimpeur extends SubsystemBase implements Loggable {

  private Servo ratchet;
  private VictorSP moteur;
  private DigitalInput limitSwitchHaut;
  private DigitalInput limitSwitchBas;

  private String name;

  @Config(rowIndex = 0, columnIndex = 0, width = 2, height = 2)
  public static double kRatchetLocked = 0;
  @Config(rowIndex = 2, columnIndex = 0, width = 2, height = 2)
  public static double kRatchetUnlocked = 90;

  @Config(rowIndex = 4, columnIndex = 0, width = 2, height = 2)
  public static double kVitesseDescendre = -1;
  @Config(rowIndex = 0, columnIndex = 2, width = 2, height = 2)
  public static double kVitesseMonter = 1;
  @Config(rowIndex = 0, columnIndex = 2, width = 2, height = 2)
  public static double kVitesseGrimper = 0.5;

  /**
   * Creates a new Grimpeur.
   */
  public Grimpeur(int portServo, int portMoteur,int portLimitSwitchHaut,int portLimitSwitchBas, String name) {
    ratchet = new Servo(portServo);
    moteur = new VictorSP(portMoteur);
    limitSwitchBas = new DigitalInput(portLimitSwitchBas);
    limitSwitchHaut = new DigitalInput(portLimitSwitchHaut);


    this.name = name;
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

  public void grimperSansRatchet() {
    ratchet.setAngle(kRatchetUnlocked);
    moteur.set(kVitesseGrimper);
  }
  public boolean estEnBas(){
    return limitSwitchBas.get();
  }
  public boolean estEnHaut(){
    return limitSwitchHaut.get();
  }
}
