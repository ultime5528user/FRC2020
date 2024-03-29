/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020.commands.brasintake;

import com.ultime5528.frc2020.subsystems.BrasIntake;


import edu.wpi.first.wpilibj2.command.CommandBase;

public class DescendreBras extends CommandBase {
  private BrasIntake brasIntake;


  public DescendreBras(BrasIntake brasIntake) {
    this.brasIntake = brasIntake;
    addRequirements(brasIntake);

  }


  @Override
  public void initialize() {
  }


  @Override
  public void execute() {
    brasIntake.descendreBras();
  }


  @Override
  public void end(boolean interrupted) {
  brasIntake.stopBras();
  }


  @Override
  public boolean isFinished() {
    return brasIntake.getPosition() >= 0;
  }
}
