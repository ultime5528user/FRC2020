/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import com.ultime5528.frc2020.commands.DescendreGrimpeur;
import com.ultime5528.frc2020.commands.EnvoyerAvaler;
import com.ultime5528.frc2020.commands.Grimper;
import com.ultime5528.frc2020.commands.MonterGrimpeur;
import com.ultime5528.frc2020.commands.Piloter;
import com.ultime5528.frc2020.commands.Tirer;
import com.ultime5528.frc2020.commands.TournerRoulette;
import com.ultime5528.frc2020.subsystems.BasePilotable;
import com.ultime5528.frc2020.subsystems.Grimpeur;
import com.ultime5528.frc2020.subsystems.Intake;
import com.ultime5528.frc2020.subsystems.Roulette;
import com.ultime5528.frc2020.subsystems.Shooter;
import io.github.oblarg.oblog.Logger;

public class RobotContainer {

  private final Joystick joystick;

  private final BasePilotable basePilotable;
  private final Grimpeur grimpeur;
  private final Shooter shooter;
  private final Roulette roulette;
  private final Intake intake;

  public RobotContainer() {
    joystick = new Joystick(0);

    basePilotable = new BasePilotable();
    basePilotable.setDefaultCommand(new Piloter(joystick, basePilotable));

    grimpeur = new Grimpeur();

    shooter = new Shooter();

    roulette = new Roulette();

    intake = new Intake();
    

    configureButtonBindings();

    Logger.configureLoggingAndConfig(this, true);

  }

  private void configureButtonBindings() {
    new JoystickButton(joystick, 1).whenHeld(new MonterGrimpeur(grimpeur));
    new JoystickButton(joystick, 2).whenHeld(new DescendreGrimpeur(grimpeur));
    new JoystickButton(joystick, 3).whenHeld(new Grimper(grimpeur));
    new JoystickButton(joystick, 4).whenPressed(new TournerRoulette(roulette));
    new JoystickButton(joystick, 9).whenPressed(new Tirer(shooter, intake));
    new JoystickButton(joystick, 5).toggleWhenPressed(new EnvoyerAvaler(intake));
  }

  public Command getAutonomousCommand() {
    return null;
  }
}