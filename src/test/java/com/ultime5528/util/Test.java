/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.util;

import java.util.List;

import com.ultime5528.util.Trajectory.MyState;

import org.junit.jupiter.api.Test;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Transform2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TrajectoryTransformTest {
  @Test
  void testTransformBy() {
    var config = new TrajectoryConfig(3, 3);
    var trajectory2 = TrajectoryGenerator.generateTrajectory(
        new Pose2d(), List.of(), new Pose2d(1, 1, Rotation2d.fromDegrees(90)),
        config
    );

    var trajectory = new com.ultime5528.util.Trajectory(trajectory2.getStates());

    var transformedTrajectory = trajectory.transformBy(
        new Transform2d(new Translation2d(1, 2), Rotation2d.fromDegrees(30)));

    // Test initial pose.
    assertEquals(new Pose2d(1, 2, Rotation2d.fromDegrees(30)),
        transformedTrajectory.sample(0).poseMeters);

    testSameShapedTrajectory(trajectory.getStates(), transformedTrajectory.getStates());
  }

  @Test
  void testRelativeTo() {
    var config = new TrajectoryConfig(3, 3);
    var trajectory2 = TrajectoryGenerator.generateTrajectory(
        new Pose2d(1, 2, Rotation2d.fromDegrees(30.0)),
        List.of(), new Pose2d(5, 7, Rotation2d.fromDegrees(90)),
        config
    );

    var trajectory = new com.ultime5528.util.Trajectory(trajectory2.getStates());

    var transformedTrajectory = trajectory.relativeTo(new Pose2d(1, 2, Rotation2d.fromDegrees(30)));

    // Test initial pose.
    assertEquals(new Pose2d(), transformedTrajectory.sample(0).poseMeters);

    testSameShapedTrajectory(trajectory.getStates(), transformedTrajectory.getStates());
  }

  void testSameShapedTrajectory(List<MyState> statesA, List<MyState> statesB) {
    for (int i = 0; i < statesA.size() - 1; i++) {
      var a1 = statesA.get(i).poseMeters;
      var a2 = statesA.get(i + 1).poseMeters;

      var b1 = statesB.get(i).poseMeters;
      var b2 = statesB.get(i + 1).poseMeters;

      assertEquals(a2.relativeTo(a1), b2.relativeTo(b1));
    }
  }
}