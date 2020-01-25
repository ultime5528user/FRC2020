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

public class Grimpeur extends SubsystemBase {

  private Servo ratchet;
  private VictorSP moteur;

  public static double RATCHET_ON_ANGLE = 0;
  public static double RATCHET_OFF_ANGLE = 90;

  public static double VITESSE_DESCENDRE = -1;
  public static double VITESSE_MONTER = 1;
  public static double VITESSE_GRIMPER = 0.5;

  /**
   * Creates a new Grimpeur.
   */
  public Grimpeur() {
    ratchet = new Servo(Constants.Ports.GRIMPEUR_SERVO);
    moteur = new VictorSP(Constants.Ports.GRIMPEUR_MOTEUR);
  }

  @Override
  public void periodic() { }

  public void idle(){
    ratchet.setAngle(RATCHET_ON_ANGLE);
    moteur.set(0);
  }

  public void monter(){
    // ratchet.setAngle(RATCHET_OFF_ANGLE);
    ratchet.setSpeed(1.0);
    moteur.set(VITESSE_MONTER);
  }

  public void descendre(){
    ratchet.setAngle(RATCHET_OFF_ANGLE);
    moteur.set(VITESSE_DESCENDRE);
  }

  public void grimper(){
    ratchet.setAngle(RATCHET_ON_ANGLE);
    moteur.set(VITESSE_GRIMPER);
  }
}
