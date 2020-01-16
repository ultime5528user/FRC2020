/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.Piloter;
import frc.robot.subsystems.BasePilotable;


public class RobotContainer {
 Joystick joystick;
 BasePilotable basePilotable;




  public RobotContainer() {
    joystick = new Joystick(1);
    basePilotable.setDefaultCommand(new Piloter(joystick, basePilotable));
    configureButtonBindings();
  }


  private void configureButtonBindings() {
    
  }



  public Command getAutonomousCommand() {
 return null;
  }
}
