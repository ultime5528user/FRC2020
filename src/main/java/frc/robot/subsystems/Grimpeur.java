/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Config;

public class Grimpeur extends SubsystemBase implements Loggable {

  private Servo ratchet;
  private VictorSP moteur;

  @Config
  public static double kRatchetAngleOn = 0;
  @Config
  public static double kRatchetAngleOff = 90;

  @Config
  public static double kVitesseDescendre = -1;
  @Config
  public static double kVitesseMonter = 1;
  @Config
  public static double kVitesseGrimper = 0.5;

  /**
   * Creates a new Grimpeur.
   */
  public Grimpeur() {
    ratchet = new Servo(Constants.Ports.GRIMPEUR_SERVO);
    moteur = new VictorSP(Constants.Ports.GRIMPEUR_MOTEUR);
  }

  @Override
  public void periodic() {
  }

  public void idle() {
    ratchet.setAngle(kRatchetAngleOn);
    moteur.set(0);
  }

  public void monter() {
    // ratchet.setAngle(RATCHET_OFF_ANGLE);
    ratchet.setSpeed(1.0);
    moteur.set(kVitesseMonter);
  }

  public void descendre() {
    ratchet.setAngle(kRatchetAngleOff);
    moteur.set(kVitesseDescendre);
  }

  public void grimper() {
    ratchet.setAngle(kRatchetAngleOn);
    moteur.set(kVitesseGrimper);
  }
}
