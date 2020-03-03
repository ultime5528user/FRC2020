/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020;

import com.ultime5528.frc2020.commands.autonome.AutoCinqBallons;

import com.ultime5528.frc2020.commands.basepilotable.Piloter;
import com.ultime5528.frc2020.commands.basepilotable.TournerAbsolue;
import com.ultime5528.frc2020.commands.basepilotable.Viser;
import com.ultime5528.frc2020.commands.brasintake.Balayer;
import com.ultime5528.frc2020.commands.brasintake.DescendreLesBras;
import com.ultime5528.frc2020.commands.brasintake.MonterLesBras;
import com.ultime5528.frc2020.commands.grimpeur.Grimper;
import com.ultime5528.frc2020.commands.grimpeur.GrimperSansRatchet;
import com.ultime5528.frc2020.commands.grimpeur.MonterGrimpeur;
import com.ultime5528.frc2020.commands.intake.PrendreTransporterBallon;
import com.ultime5528.frc2020.commands.intake.ViderIntake;
import com.ultime5528.frc2020.commands.shooter.Tirer;
import com.ultime5528.frc2020.commands.shooter.ViserTirer;
import com.ultime5528.frc2020.subsystems.BasePilotable;
import com.ultime5528.frc2020.subsystems.BrasIntake;
import com.ultime5528.frc2020.subsystems.Grimpeur;
import com.ultime5528.frc2020.subsystems.Intake;
import com.ultime5528.frc2020.subsystems.Roulette;
import com.ultime5528.frc2020.subsystems.Shooter;
import com.ultime5528.frc2020.subsystems.VisionController;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import io.github.oblarg.oblog.Logger;
import io.github.oblarg.oblog.annotations.Log;

public class RobotContainer {

  private final Joystick joystick;
  private final Joystick A_Pac1;
  private final Joystick A_Pac2;

  @Log.Include(include = Constants.OBLOG_MATCH)
  @Log.Graph(name = "BP Position Encoder Droit", methodName = "getPositionEncoderDroit", rowIndex = 3, columnIndex = 2, width = 3, height = 2)
  @Log.Graph(name = "BP Position Encoder Gauche", methodName = "getPositionEncoderGauche", rowIndex = 3, columnIndex = 2, width = 3, height = 2)
  @Log(name = "X Position", methodName = "getX", rowIndex = 3, columnIndex = 2, width = 3, height = 2)
  @Log(name = "Y Position", methodName = "getY", rowIndex = 3, columnIndex = 2, width = 3, height = 2)
  @Log(name = "Gyro", methodName = "getAngleDegrees", rowIndex = 3, columnIndex = 2, width = 3, height = 2)
  @Log(name = "Timestamp", methodName = "getGyroTimestamp", rowIndex = 3, columnIndex = 2, width = 3, height = 2)
  private final BasePilotable basePilotable;

  @Log.Include(include = Constants.OBLOG_MATCH)
  @Log.BooleanBox(name = "Grimpeur Droit En Haut", methodName = "estEnHaut", rowIndex = 3, columnIndex = 2, width = 3, height = 2)
  @Log.BooleanBox(name = "Grimpeur Droit En Bas", methodName = "estEnBas", rowIndex = 3, columnIndex = 2, width = 3, height = 2)
  private final Grimpeur grimpeurDroit;

  @Log.Include(include = Constants.OBLOG_MATCH)
  @Log.BooleanBox(name = "Grimpeur Gauche En Haut", methodName = "estEnHaut", rowIndex = 3, columnIndex = 2, width = 3, height = 2)
  @Log.BooleanBox(name = "Grimpeur Gauche En Bas", methodName = "estEnBas", rowIndex = 3, columnIndex = 2, width = 3, height = 2)
  private final Grimpeur grimpeurGauche;

  private final Shooter shooter;

 // private final Roulette roulette = null;

  @Log.Include(include = Constants.OBLOG_MATCH)
  @Log(name = "Nombre ballons Intake", methodName = "getNombreBallonsDansIntake", rowIndex = 3, columnIndex = 2, width = 3, height = 2)
  private final Intake intake;

  @Log.Include(include = Constants.OBLOG_MATCH)
  @Log(name = "Bras Droit Position Encoder", methodName = "getPosition", rowIndex = 3, columnIndex = 2, width = 3, height = 2)
  private final BrasIntake brasDroit;

  @Log.Include(include = Constants.OBLOG_MATCH)
  @Log(name = "Bras Gauche Position Encoder", methodName = "getPosition", rowIndex = 3, columnIndex = 2, width = 3, height = 2)
  private final BrasIntake brasGauche;

  @Log.Include(include = Constants.OBLOG_MATCH)
  @Log(name = "Vision Snapshot", methodName = "getRasbperryPiData", rowIndex = 3, columnIndex = 2, width = 3, height = 2)
  private final VisionController vision;

  private final PowerDistributionPanel pdp;

  private final Piloter piloter;
  private final TournerAbsolue tourner;

  private final Command autonomousCommand;

  public RobotContainer() {
    joystick = new Joystick(0);
    A_Pac1 = new Joystick(1);
    A_Pac2 = new Joystick(2);

    basePilotable = new BasePilotable();
    piloter = new Piloter(joystick, basePilotable);
    basePilotable.setDefaultCommand(piloter);

    grimpeurDroit = new Grimpeur(Ports.GRIMPEUR_SERVO_DROIT, Ports.GRIMPEUR_MOTEUR_DROIT,
        Ports.GRIMPEUR_DROIT_LIMIT_SWITCH_HAUT, Ports.GRIMPEUR_DROIT_LIMIT_SWITCH_BAS, 0.2, 0.55, false,
        "Grimpeur Droit");

    grimpeurGauche = new Grimpeur(Ports.GRIMPEUR_SERVO_GAUCHE, Ports.GRIMPEUR_MOTEUR_GAUCHE,
        Ports.GRIMPEUR_GAUCHE_LIMIT_SWITCH_HAUT, Ports.GRIMPEUR_GAUCHE_LIMIT_SWITCH_BAS, 0.9, 0.4, true,
        "Grimpeur Gauche");

    brasDroit = new BrasIntake(Ports.BRAS_INTAKE_DROIT, Ports.BRAS_INTAKE_DROIT_ENCODER_A,
        Ports.BRAS_INTAKE_DROIT_ENCODER_B, true, "bras droit");

    brasGauche = new BrasIntake(Ports.BRAS_INTAKE_GAUCHE, Ports.BRAS_INTAKE_GAUCHE_ENCODER_A,
        Ports.BRAS_INTAKE_GAUCHE_ENCODER_B, false, "bras gauche");
    shooter = new Shooter();

    // roulette = new Roulette();

    if (Constants.ENABLE_PDP) {
      pdp = new PowerDistributionPanel();
    } else {
      pdp = null;
    }

    intake = new Intake(pdp);

    vision = new VisionController(basePilotable::getGyroTimestamp);
    // vision.initTestCamera();

    configureButtonBindings();

    Logger.configureLoggingAndConfig(this, false);
    // LiveWindow.disableAllTelemetry();

    SmartDashboard.putData(CommandScheduler.getInstance());

    if (Constants.ENABLE_COMMAND_TROUBLESHOOTING_PRINTS) {

      CommandScheduler.getInstance()
          .onCommandInitialize(command -> System.out.println(command.getName() + " initialized"));

      CommandScheduler.getInstance().onCommandFinish(command -> System.out.println(command.getName() + " finished"));

      CommandScheduler.getInstance()
          .onCommandInterrupt(command -> System.out.println(command.getName() + " interrupted"));
    }

    tourner = new TournerAbsolue(basePilotable, 100.0, 1.25, 0.8);
    SmartDashboard.putData("Vider intake", new ViderIntake(intake).withTimeout(5.0));
    SmartDashboard.putData("Tourner 100", tourner);
    SmartDashboard.putData("Viser", new Viser(basePilotable, vision));

    CommandScheduler.getInstance().onCommandInitialize(c -> System.out.println("Initialized : " + c.getName()));
    CommandScheduler.getInstance().onCommandFinish(c -> System.out.println("Finish : " + c.getName()));
    CommandScheduler.getInstance().onCommandInterrupt(c -> System.out.println("Interrupted : " + c.getName()));

    autonomousCommand = new AutoCinqBallons(basePilotable, brasDroit, brasGauche, vision, shooter, intake);

  }

  private void configureButtonBindings() {

    new JoystickButton(joystick, 9).toggleWhenPressed(new ViserTirer(basePilotable, shooter, intake, vision, 2.0));

    // new JoystickButton(A_Pac2, 2).toggleWhenPressed(new Viser(basePilotable,
    // vision));

    new Trigger(() -> A_Pac1.getRawAxis(1) < -0.5).whileActiveOnce(new MonterGrimpeur(grimpeurDroit));
    new Trigger(() -> A_Pac1.getRawAxis(1) > 0.5).whileActiveOnce(new GrimperSansRatchet(grimpeurDroit));
    new JoystickButton(A_Pac1, 2).whenHeld(new Grimper(grimpeurDroit));

    new Trigger(() -> A_Pac1.getRawAxis(0) > 0.5).whileActiveOnce(new MonterGrimpeur(grimpeurGauche));
    new Trigger(() -> A_Pac1.getRawAxis(0) < -0.5).whileActiveOnce(new GrimperSansRatchet(grimpeurGauche));
    new JoystickButton(A_Pac1, 1).whenHeld(new Grimper(grimpeurGauche));

    // new JoystickButton(joystick, 3).toggleWhenPressed(new
    // TournerRoulette(roulette));
    new JoystickButton(A_Pac2, 1).toggleWhenPressed(new Tirer(shooter, intake, vision, 2.0));
    new JoystickButton(A_Pac2, 2).toggleWhenPressed(new Viser(basePilotable, vision));

    new JoystickButton(A_Pac1, 5).toggleWhenPressed(new PrendreTransporterBallon(intake, brasDroit, brasGauche));
    new JoystickButton(A_Pac1, 4).toggleWhenPressed(new MonterLesBras(brasDroit, brasGauche));
    new JoystickButton(A_Pac1, 3).toggleWhenPressed(new DescendreLesBras(brasDroit, brasGauche));
    new JoystickButton(A_Pac1, 6).toggleWhenPressed(new InstantCommand(this::resetEncodersBras, brasDroit, brasGauche));
    new JoystickButton(A_Pac1, 8).whenHeld(new Balayer(intake, brasDroit, brasGauche));
    new JoystickButton(A_Pac1, 7).toggleWhenPressed(new ViderIntake(intake));
  }

  public Command getAutonomousCommand() {
    // return new DescendreBrasInitial(brasDroit, brasGauche);
    // return SuivreTrajectoire.from(basePilotable,
    // new Pose2d(0.0, 0.0, Rotation2d.fromDegrees(0.0)),
    // List.of(),
    // new Pose2d(3.0, -2.0, Rotation2d.fromDegrees(-90.0)
    // ), 0.5, 0.5);
    return autonomousCommand;
  }

  public void unlockRatchets() {
    grimpeurGauche.unlockRatchet();
    grimpeurDroit.unlockRatchet();
  }

  public void resetEncodersBras() {
    brasDroit.resetEncoder();
    brasGauche.resetEncoder();
  }

  public void resetBasePilotable() {
    basePilotable.resetGyro();
    basePilotable.resetOdometry(new Pose2d());
  }

}
