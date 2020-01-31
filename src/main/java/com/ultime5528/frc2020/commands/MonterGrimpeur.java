/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import com.ultime5528.frc2020.subsystems.Grimpeur;

public class MonterGrimpeur extends CommandBase {
  
  private Grimpeur grimpeur;

  public MonterGrimpeur(Grimpeur grimpeur) {
    addRequirements(grimpeur);
    this.grimpeur = grimpeur;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    grimpeur.monter();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    grimpeur.idle();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
