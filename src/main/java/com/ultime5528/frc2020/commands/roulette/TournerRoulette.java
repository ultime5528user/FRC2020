/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020.commands.roulette;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import com.ultime5528.frc2020.subsystems.Roulette;

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
    return timer.get() >= Roulette.kTempsTourner;
  }
}
