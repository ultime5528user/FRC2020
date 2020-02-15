/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020.commands.intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import com.ultime5528.frc2020.subsystems.Intake;

public class PrendreBallon extends CommandBase {

  private Intake intake;

  public PrendreBallon(Intake intake) {
    this.intake = intake;
    addRequirements(intake);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  @Override
  public void execute() {
    intake.prendreBallon();
    intake.actionnerBras();
  }

  @Override
  public void end(boolean interrupted) {
    intake.stopIntake();
    intake.stopBras();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return intake.hasBallonBas();
  }
}
