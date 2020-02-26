/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020.commands.brasintake;

import com.ultime5528.frc2020.subsystems.BrasIntake;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;


public class desendreBras extends SequentialCommandGroup {

   private BrasIntake brasintake;
  
  public desendreBras(BrasIntake brasintake) {
    
    super(
     new MonterBras(brasintake),
     new DescendreBras(brasintake)
    );
    this.brasintake = brasintake;
  }

}
