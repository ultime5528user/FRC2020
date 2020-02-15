/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020.commands.intake;

import com.ultime5528.frc2020.subsystems.Intake;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ViderIntake extends CommandBase {

  private Intake intake;
  /**
   * Creates a new ViderIntake.
   */
  public ViderIntake(Intake intake) {
    this.intake = intake;
    addRequirements(intake);
    setName("Vider Intake");
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    intake.transporterInverse();
    intake.prendreBallonInverse();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    intake.stopIntake();
    intake.stopTransporteur();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
