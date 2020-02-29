/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020.commands.brasintake;

import com.ultime5528.frc2020.commands.intake.ActionnerBrasInverse;
import com.ultime5528.frc2020.subsystems.BrasIntake;
import com.ultime5528.frc2020.subsystems.Intake;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class Balayer extends ParallelCommandGroup {
  /**
   * Creates a new Balayer.
   */
  public Balayer(Intake intake, BrasIntake brasDroit, BrasIntake brasGauche) {
    super(
      new DescendreLesBras(brasDroit, brasGauche),
      new ActionnerBrasInverse(intake),
      new MonterLesBras(brasDroit, brasGauche)
    );
  }
}
