/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020.commands.shooter;

import java.util.OptionalDouble;

import com.ultime5528.frc2020.subsystems.Intake;
import com.ultime5528.frc2020.subsystems.Shooter;
import com.ultime5528.frc2020.subsystems.VisionController;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import com.ultime5528.util.Timer;

public class Tirer extends CommandBase {

  private Shooter shooter;
  private Timer timer;
  private Intake intake;
  private VisionController vision;

  public Tirer(Shooter shooter, Intake intake, VisionController vision) {
    this.shooter = shooter;
    this.intake = intake;
    this.vision = vision;
    this.timer = new Timer();
    addRequirements(shooter, intake);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    timer.reset();
    shooter.resetFilter();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    // shooter.tirer(vision.getHauteurCible());
    shooter.tirer(OptionalDouble.empty());

    double vitesse = shooter.getVitesse();
    SmartDashboard.putNumber("shooter vitesse", vitesse);

    if (vitesse >= Shooter.kRPM * Shooter.kPrecision) {
       intake.transporter();
       intake.prendreBallon();
    }

    if (!intake.hasBallonHaut() && !timer.isRunning()) {
      timer.start();
    } else if (timer.isRunning() && intake.hasBallonHaut()) {
      timer.stop();
      timer.reset();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooter.stop();
    intake.stopTransporteur();
    intake.stopIntake();
    timer.stop();
    intake.resetBallonDansIntake();;

  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return timer.get() >= 2;
  }
}
