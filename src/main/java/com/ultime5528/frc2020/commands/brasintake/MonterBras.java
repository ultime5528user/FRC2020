/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020.commands.brasintake;

import com.ultime5528.frc2020.subsystems.BrasIntake;
import com.ultime5528.util.Timer;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class MonterBras extends CommandBase {
  private Timer timer;
  private BrasIntake brasIntake;


  public MonterBras(BrasIntake brasIntake, Timer timer) {
    this.brasIntake = brasIntake;
    addRequirements(brasIntake);
  }

  @Override
  public void initialize() {
    timer.reset();
  }

  @Override
  public void execute() {
    brasIntake.monterBras();
   /* if (  && !timer.isRunning()) {
      timer.start();
    } else if (timer.isRunning() && ) {
      timer.stop();
      timer.reset();
    }
  }*/


  }

  @Override
  public void end(boolean interrupted) {
  }

 
  @Override
  public boolean isFinished() {
    return false;
  }
}
