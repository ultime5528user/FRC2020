/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Roulette extends SubsystemBase {

  public static double kVitesseTourner = 0.75;
  public static double kTempsTourner = 10;
  
  private VictorSP moteurRoulette;

  public Roulette() {
    moteurRoulette = new VictorSP(Constants.Ports.ROULETTE_MOTEUR);

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void tourner() {
    moteurRoulette.set(kVitesseTourner);
  }

  public void stop() {
    moteurRoulette.set(0.0);
  }
}
