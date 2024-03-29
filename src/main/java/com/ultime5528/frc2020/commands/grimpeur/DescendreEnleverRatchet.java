/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020.commands.grimpeur;

import com.ultime5528.frc2020.subsystems.Grimpeur;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class DescendreEnleverRatchet extends SequentialCommandGroup {
  public DescendreEnleverRatchet(Grimpeur grimpeur) {
    super(new GrimperSansRatchet(grimpeur).withTimeout(0.25), new MonterGrimpeur(grimpeur));
  }
}
