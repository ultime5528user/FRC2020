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
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.DescendreGrimpeur;
import frc.robot.commands.Grimper;
//import frc.robot.commands.IdleGrimpeur;
import frc.robot.commands.MonterGrimpeur;
import frc.robot.commands.Piloter;
import frc.robot.subsystems.BasePilotable;
import frc.robot.subsystems.Grimpeur;

public class RobotContainer {
  private BasePilotable basePilotable;
  private Grimpeur grimpeur;

  private Joystick joystick;

  public RobotContainer() {
    //basePilotable = new BasePilotable();
    //basePilotable.setDefaultCommand(new Piloter(joystick, basePilotable));
    grimpeur = new Grimpeur();

    joystick = new Joystick(1);

    configureButtonBindings();
  }

  private void configureButtonBindings() {
    new JoystickButton(joystick, 1).whenHeld(new MonterGrimpeur(grimpeur));
    new JoystickButton(joystick, 2).whenHeld(new DescendreGrimpeur(grimpeur));
    new JoystickButton(joystick, 3).whenHeld(new Grimper(grimpeur));
  }

  public Command getAutonomousCommand() {
    return null;
  }
}
