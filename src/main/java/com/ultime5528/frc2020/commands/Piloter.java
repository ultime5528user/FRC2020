/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.CommandBase;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Config;

import com.ultime5528.frc2020.subsystems.BasePilotable;
import com.ultime5528.util.CubicInterpolator;

public class Piloter extends CommandBase implements Loggable {

  private BasePilotable basePilotable;
  private Joystick joystick;

  public static double kCourbureX = 0;
  public static double kDeadzoneJoystickX = 0.5;
  public static double kDeadzoneVitesseX = 0.5;

  public static double kCourbureY = 0;
  public static double kDeadzoneJoystickY = 0.5;
  public static double kDeadzoneVitesseY = 0.5;

  @Config.NumberSlider(name = "Courbure X", min = 0, max = 1, methodName = "setCourbure")
  @Config.NumberSlider(name = "Deadzone Joystick X", min = 0, max = 1, methodName = "setDeadzoneX")
  @Config.NumberSlider(name = "Deadzone Vitesse X", min = 0, max = 1, methodName = "setDeadzoneY")
  private CubicInterpolator cubicInterpolatorX = new CubicInterpolator(kCourbureX, kDeadzoneJoystickX,
      kDeadzoneVitesseX);

  @Config.NumberSlider(name = "Courbure Y", min = 0, max = 1, methodName = "setCourbure")
  @Config.NumberSlider(name = "Deadzone Joystick Y", min = 0, max = 1, methodName = "setDeadzoneX")
  @Config.NumberSlider(name = "Deadzone Vitesse Y", min = 0, max = 1, methodName = "setDeadzoneY")
  private CubicInterpolator cubicInterpolatorY = new CubicInterpolator(kCourbureY, kDeadzoneJoystickY,
      kDeadzoneVitesseY);

  public Piloter(Joystick joystick, BasePilotable basePilotable) {
    addRequirements(basePilotable);
    this.basePilotable = basePilotable;
    this.joystick = joystick;
  }

  @Override
  public void initialize() {
  }

  @Override
  public void execute() {
    basePilotable.drive(cubicInterpolatorY.interpolate(joystick.getY()),
        cubicInterpolatorX.interpolate(joystick.getX()));
  }

  @Override
  public void end(boolean interrupted) {
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
