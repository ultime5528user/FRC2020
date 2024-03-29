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

import edu.wpi.first.wpilibj2.command.CommandBase;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Config;

import com.ultime5528.util.Timer;

public class Tirer extends CommandBase implements Loggable {

  @Config
  private static double kDelay = 0.1;

  @Config.ToggleButton
  private static boolean kUseDelay = false;

  private Shooter shooter;
  private Timer timer;
  private Timer timerShooter;
  private Intake intake;
  private VisionController vision;
  private boolean initialBonneVitesse = false;
  private double tempsFin;
  private OptionalDouble lastHauteur;

  public Tirer(Shooter shooter, Intake intake, VisionController vision, double tempsFin) {
    this.shooter = shooter;
    this.intake = intake;
    this.vision = vision;
    this.timer = new Timer();
    this.timerShooter = new Timer();
    this.tempsFin = tempsFin;
    addRequirements(shooter, intake);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    timer.reset();
    timerShooter.reset();
    vision.enable();
    initialBonneVitesse = false;
    lastHauteur = OptionalDouble.empty();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    OptionalDouble hauteur = vision.getHauteurCible();

    if (hauteur.isPresent()) {
      lastHauteur = hauteur;
    }

    double vitesseGoal = shooter.tirer(lastHauteur);

    double vitesse = shooter.getVitesse();
    double erreurVitesse = Math.abs(vitesse / vitesseGoal - 1.0);
    boolean bonneVitesse = (erreurVitesse < Shooter.kToleranceVitesse);

    if (bonneVitesse && !timerShooter.isRunning()) {
      timerShooter.start();
    } else if (!bonneVitesse) {
      timerShooter.stop();
      timerShooter.reset();
    }

    double tempsBonneVitesse = timerShooter.get();

    boolean bonneVitesseLongtemps = (tempsBonneVitesse > kDelay);
    bonneVitesseLongtemps = (bonneVitesseLongtemps || initialBonneVitesse);

    if (bonneVitesseLongtemps) {
      if (!kUseDelay) {
         initialBonneVitesse = true;
      }
       intake.transporter();
       intake.prendreBallon();
    } else {
      intake.stopTransporteur();
      intake.stopIntake();
    }

    if (bonneVitesseLongtemps && !intake.hasBallonHaut() && !timer.isRunning()) {
      timer.start();
    } else if (timer.isRunning() && (!bonneVitesseLongtemps || intake.hasBallonHaut())) {
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
    timerShooter.stop();
    intake.resetBallonDansIntake();
    vision.disable();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return timer.get() >= tempsFin;
  }
}
