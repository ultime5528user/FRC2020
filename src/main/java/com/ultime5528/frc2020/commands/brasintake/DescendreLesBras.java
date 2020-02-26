/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020.commands.brasintake;

import com.ultime5528.frc2020.subsystems.BrasIntake;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

public class DescendreLesBras extends ParallelCommandGroup {

  public DescendreLesBras(BrasIntake brasDroit,BrasIntake brasGauche) {

    super( new DescendreBras(brasDroit),
        new DescendreBras(brasGauche));

  }

}
