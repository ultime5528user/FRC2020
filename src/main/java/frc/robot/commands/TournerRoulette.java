/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Roulette;

public class TournerRoulette extends CommandBase {
  private Roulette roulette;
  private Timer timer;

  public TournerRoulette(Roulette roulette) {
    this.roulette = roulette;
    this.timer = new Timer();

    addRequirements(roulette);

  }

  @Override
  public void initialize() {
    timer.reset();
    timer.start();

  }

  @Override
  public void execute() {
    roulette.tourner();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    timer.stop();
    roulette.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return timer.get() >= Roulette.kTempsTourner; // TODO regarder si c'est le bon temps (28 tour de moteur devrait
                                                  // faire l'affaire)
  }
}
