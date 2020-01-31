/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.BasePilotable;

public class Piloter extends CommandBase {

  BasePilotable basePilotable;
  Joystick joystick;
  public Piloter(Joystick joystick, BasePilotable basePilotable) {
    addRequirements(basePilotable);
    this.basePilotable = basePilotable;
    this.joystick = joystick;

  }


  @Override
  public void initialize() {
  }


  @Override
  public void execute() {
    basePilotable.drive(joystick.getY(), joystick.getX());
  }


  @Override
  public void end(boolean interrupted) {
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
