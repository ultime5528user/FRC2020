/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020.commands.shooter;

import java.util.OptionalDouble;

import com.ultime5528.frc2020.subsystems.Shooter;
import com.ultime5528.frc2020.subsystems.VisionController;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class DemarrerShooter extends CommandBase {

  private Shooter shooter;
  private VisionController vision;
  private OptionalDouble lastHauteur;

  public DemarrerShooter(Shooter shooter, VisionController vision) {
    this.shooter = shooter;
   
    this.vision = vision;
    addRequirements(shooter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    vision.enable();
    lastHauteur = OptionalDouble.empty();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    OptionalDouble hauteur = vision.getHauteurCible();

    if (hauteur.isPresent()) {
      lastHauteur = hauteur;
    }

    shooter.tirer(lastHauteur);
    
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
