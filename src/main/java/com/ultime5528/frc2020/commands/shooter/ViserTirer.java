/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020.commands.shooter;

import com.ultime5528.frc2020.commands.basepilotable.Viser;
import com.ultime5528.frc2020.commands.intake.TransporterBallonHaut;
import com.ultime5528.frc2020.subsystems.BasePilotable;
import com.ultime5528.frc2020.subsystems.Intake;
import com.ultime5528.frc2020.subsystems.Shooter;
import com.ultime5528.frc2020.subsystems.VisionController;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class ViserTirer extends SequentialCommandGroup {
  
  private Shooter shooter;
  
  public ViserTirer(BasePilotable basePilotable, Shooter shooter, Intake intake, VisionController vision, double tempsFin) {
 
    super(
      deadline(
        new Viser(basePilotable, vision),
        new TransporterBallonHaut(intake),
        new DemarrerShooter(shooter, vision)
      ), 
      new Tirer(shooter, intake, vision, tempsFin));
  
    this.shooter = shooter;}

  @Override
  public void end(boolean interrupted) {
    super.end(interrupted);
    shooter.stop();
  }

}
