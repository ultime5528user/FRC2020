/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020.commands.autonome;

import java.util.List;

import com.ultime5528.frc2020.commands.basepilotable.SuivreTrajectoire;
import com.ultime5528.frc2020.commands.basepilotable.Tourner;
import com.ultime5528.frc2020.commands.basepilotable.TournerAbsolue;
import com.ultime5528.frc2020.commands.brasintake.DescendreBrasInitial;
import com.ultime5528.frc2020.commands.intake.PrendreBallon;
import com.ultime5528.frc2020.commands.intake.TransporterBallon;
import com.ultime5528.frc2020.commands.shooter.DemarrerShooter;
import com.ultime5528.frc2020.commands.shooter.ViserTirer;
import com.ultime5528.frc2020.subsystems.BasePilotable;
import com.ultime5528.frc2020.subsystems.BrasIntake;
import com.ultime5528.frc2020.subsystems.Intake;
import com.ultime5528.frc2020.subsystems.Shooter;
import com.ultime5528.frc2020.subsystems.VisionController;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class AutoSixBallons extends SequentialCommandGroup {

  public AutoSixBallons(BasePilotable basePilotable, BrasIntake brasDroit, BrasIntake brasGauche,
      VisionController vision, Shooter shooter, Intake intake) {

    super(deadline(sequence(deadline(new Tourner(basePilotable, 25, 1.5, 1.3), new DemarrerShooter(shooter, vision)),
        new ViserTirer(basePilotable, shooter, intake, vision, 1.0)

    ), new DescendreBrasInitial(brasDroit, brasGauche)),
        new TournerAbsolue(basePilotable, 0.0, 2.00, 1.5),
        deadline(
            SuivreTrajectoire.from(basePilotable, new Pose2d( 0.0, 0.0, Rotation2d.fromDegrees(0.0)), List.of(),
                new Pose2d(-5.0, 0.0, Rotation2d.fromDegrees(0.0)), 1.5, 1.5 ,true),
            sequence(new PrendreBallon(intake), new TransporterBallon(intake), new PrendreBallon(intake),
                new TransporterBallon(intake), new PrendreBallon(intake), new TransporterBallon(intake))),
        new ViserTirer(basePilotable, shooter, intake, vision, 1.0));

  }
}
