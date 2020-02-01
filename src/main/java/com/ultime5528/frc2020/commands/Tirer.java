/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020.commands;

import com.ultime5528.frc2020.subsystems.Intake;
import com.ultime5528.frc2020.subsystems.Shooter;

import edu.wpi.first.wpilibj2.command.CommandBase;
import com.ultime5528.util.Timer;

public class Tirer extends CommandBase {
  
  private Shooter shooter;
  private Timer timer;
  private Intake intake;

  public Tirer(Shooter shooter, Intake intake) {
    this.shooter = shooter;
    this.intake = intake;
    this.timer = new Timer();
    addRequirements(shooter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    timer.reset();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    shooter.tirer();
    if(shooter.getVitesse() >= Shooter.kRPM * Shooter.kPrecision){
      intake.transporter();
    }
    if(!intake.hasBallonHaut()){
      timer.start();
    } else if(timer.isRunning()){
      timer.stop();
      timer.reset();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooter.stop();
    timer.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return timer.get() >= 2;

  }
}
