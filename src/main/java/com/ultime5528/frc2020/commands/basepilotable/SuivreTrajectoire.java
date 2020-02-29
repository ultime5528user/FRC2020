/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020.commands.basepilotable;

import java.util.List;

import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj2.command.Command;
import com.ultime5528.frc2020.subsystems.BasePilotable;
import com.ultime5528.util.RamseteCommand;
import com.ultime5528.util.RamseteController;

public class SuivreTrajectoire {

  public static Command from(BasePilotable basePilotable, Pose2d positionDepart, List<Translation2d> waypoints,
      Pose2d positionFin, double maxSpeed, double maxAcceleration) {

    var autoVoltageConstraint = new DifferentialDriveVoltageConstraint(
        new SimpleMotorFeedforward(BasePilotable.kS, BasePilotable.kV, BasePilotable.kA),
        BasePilotable.kDriveKinematics, 9);

    // Create config for trajectory
    TrajectoryConfig config = new TrajectoryConfig(maxSpeed, maxAcceleration)
            // Add kinematics to ensure max speed is actually obeyed
            .setKinematics(BasePilotable.kDriveKinematics)
            // Apply the voltage constraint
            .addConstraint(autoVoltageConstraint);

    // An example trajectory to follow. All units in meters.
    Trajectory exampleTrajectory = TrajectoryGenerator.generateTrajectory(
      positionDepart, waypoints, positionFin, config
    );

    RamseteCommand ramseteCommand = new RamseteCommand(exampleTrajectory, basePilotable::getPose,
        new RamseteController(BasePilotable.kRamseteB, BasePilotable.kRamseteZeta),
        BasePilotable.kFeedForward,
        BasePilotable.kDriveKinematics, basePilotable::getWheelSpeeds,
        BasePilotable.createPIDController(), BasePilotable.createPIDController(),
        // RamseteCommand passes volts to the callback
        basePilotable::tankDriveVolts, basePilotable);

    // Run path following command, then stop at the end.
    return ramseteCommand.andThen(() -> basePilotable.tankDriveVolts(0, 0));

  }

}
