/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020.commands.intake;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import com.ultime5528.frc2020.subsystems.Intake;

public class PrendreTransporterBallon extends SequentialCommandGroup {
  private Intake intake;

  public PrendreTransporterBallon(Intake intake) {

    super(new PrendreBallon(intake), new TransporterBallon(intake));
    this.intake = intake;
  }

  @Override
  public void end(boolean interrupted) {
    super.end(interrupted);
    if (!interrupted && !intake.hasBallonBas()) {
      schedule();

    } 
  }

}