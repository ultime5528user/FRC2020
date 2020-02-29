/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020.commands.autonome;

import java.util.List;

import com.ultime5528.frc2020.commands.basepilotable.SuivreTrajectoire;
import com.ultime5528.frc2020.commands.basepilotable.Viser;
import com.ultime5528.frc2020.commands.shooter.DemarrerShooter;
import com.ultime5528.frc2020.commands.shooter.Tirer;
import com.ultime5528.frc2020.commands.shooter.ViserTirer;
import com.ultime5528.frc2020.subsystems.BasePilotable;
import com.ultime5528.frc2020.subsystems.Intake;
import com.ultime5528.frc2020.subsystems.Shooter;
import com.ultime5528.frc2020.subsystems.VisionController;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class AutoTirer extends SequentialCommandGroup {

    public AutoTirer(BasePilotable basePilotable, VisionController vision, Shooter shooter, Intake intake) {
        super(
            sequence(
                SuivreTrajectoire.from(basePilotable, 
                new Pose2d(1.5, 0.0, Rotation2d.fromDegrees(0.0)),
                List.of(), 
                new Pose2d(0.0, 0.0, Rotation2d.fromDegrees(0.0)), 
                1.0, 0.5, true
            ),
            new ViserTirer(basePilotable, shooter, intake, vision)
        );
    }
}
