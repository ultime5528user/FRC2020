/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.DescendreGrimpeur;
import frc.robot.commands.Grimper;
import frc.robot.commands.MonterGrimpeur;
import frc.robot.commands.Piloter;
import frc.robot.commands.Tirer;
import frc.robot.commands.TournerRoulette;
import frc.robot.subsystems.BasePilotable;
import frc.robot.subsystems.Grimpeur;
import frc.robot.subsystems.Roulette;
import frc.robot.subsystems.Shooter;
import io.github.oblarg.oblog.Logger;

public class RobotContainer {

  private final Joystick joystick;

  private final BasePilotable basePilotable;
  private final Grimpeur grimpeur;
  private final Shooter shooter;
  private final Roulette roulette;

  public RobotContainer() {
    joystick = new Joystick(0);

    basePilotable = new BasePilotable();
    basePilotable.setDefaultCommand(new Piloter(joystick, basePilotable));

    grimpeur = new Grimpeur();

    shooter = new Shooter();

    roulette = new Roulette();

    configureButtonBindings();

    Logger.configureLoggingAndConfig(this, true);

  }

  private void configureButtonBindings() {
    new JoystickButton(joystick, 1).whenHeld(new MonterGrimpeur(grimpeur));
    new JoystickButton(joystick, 2).whenHeld(new DescendreGrimpeur(grimpeur));
    new JoystickButton(joystick, 3).whenHeld(new Grimper(grimpeur));
    new JoystickButton(joystick, 4).whenPressed(new TournerRoulette(roulette));
    new JoystickButton(joystick, 9).whenPressed(new Tirer(shooter));
  }

  public Command getAutonomousCommand() {
    return null;
  }
}
