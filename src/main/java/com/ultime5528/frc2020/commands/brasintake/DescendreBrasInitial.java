/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020.commands.brasintake;

import com.ultime5528.frc2020.subsystems.BrasIntake;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class DescendreBrasInitial extends ParallelCommandGroup {

  public DescendreBrasInitial(BrasIntake brasDroit,BrasIntake brasGauche) {

    super(sequence(new MonterBras(brasDroit, -80), new WaitCommand(0.5), new DescendreBras(brasDroit)),
        sequence(new MonterBras(brasGauche, -86), new WaitCommand(0.5), new DescendreBras(brasGauche)));

  }

}
