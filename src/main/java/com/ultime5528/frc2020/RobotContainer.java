/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableRegistry;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import com.ultime5528.frc2020.commands.grimpeur.DescendreEnleverRatchet;
import com.ultime5528.frc2020.commands.grimpeur.DescendreGrimpeur;
import com.ultime5528.frc2020.commands.intake.PrendreTransporterBallon;
import com.ultime5528.frc2020.commands.intake.ViderIntake;
import com.ultime5528.frc2020.commands.grimpeur.Grimper;
import com.ultime5528.frc2020.commands.grimpeur.GrimperSansRatchet;
import com.ultime5528.frc2020.commands.grimpeur.MonterGrimpeur;
import com.ultime5528.frc2020.commands.shooter.Tirer;
import com.ultime5528.frc2020.commands.basepilotable.Tourner;
import com.ultime5528.frc2020.commands.basepilotable.Viser;
import com.ultime5528.frc2020.commands.brasintake.DescendreBras;
import com.ultime5528.frc2020.commands.brasintake.MonterBras;
import com.ultime5528.frc2020.commands.basepilotable.Piloter;
import com.ultime5528.frc2020.commands.roulette.TournerRoulette;

import com.ultime5528.frc2020.subsystems.BasePilotable;
import com.ultime5528.frc2020.subsystems.BrasIntake;
import com.ultime5528.frc2020.subsystems.Grimpeur;
import com.ultime5528.frc2020.subsystems.Intake;
import com.ultime5528.frc2020.subsystems.Roulette;
import com.ultime5528.frc2020.subsystems.Shooter;
import com.ultime5528.frc2020.subsystems.VisionController;

import io.github.oblarg.oblog.Logger;

public class RobotContainer {

  private final Joystick joystick;

  private final BasePilotable basePilotable;
  private final Grimpeur grimpeurDroit;
  private final Grimpeur grimpeurGauche;
  private final Shooter shooter;
  private final Roulette roulette = null;
  private final Intake intake;
  public final BrasIntake brasDroit, brasGauche;
  private final VisionController vision;

  private final PowerDistributionPanel pdp;

  private final Piloter piloter;
  private final Tourner tourner;

  public RobotContainer() {
    joystick = new Joystick(0);

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
        Ports.BRAS_INTAKE_DROIT_ENCODER_B, "bras droit");
    brasGauche = new BrasIntake(Ports.BRAS_INTAKE_GAUCHE, Ports.BRAS_INTAKE_GAUCHE_ENCODER_A,
        Ports.BRAS_INTAKE_GAUCHE_ENCODER_B, "bras gauche");
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

    // Logger.configureLoggingAndConfig(this, false);
    // LiveWindow.disableAllTelemetry();

    SmartDashboard.putData(CommandScheduler.getInstance());

    if (Constants.ENABLE_COMMAND_TROUBLESHOOTING_PRINTS) {

      CommandScheduler.getInstance()
          .onCommandInitialize(command -> System.out.println(command.getName() + " initialized"));

      CommandScheduler.getInstance().onCommandFinish(command -> System.out.println(command.getName() + " finished"));

      CommandScheduler.getInstance()
          .onCommandInterrupt(command -> System.out.println(command.getName() + " interrupted"));

    }
    tourner = new Tourner(basePilotable, 100.0, 0.75, 0.5);
    SmartDashboard.putData("Vider intake", new ViderIntake(intake).withTimeout(5.0));
    SmartDashboard.putData("Tourner 100", tourner);
    SmartDashboard.putData("Viser", new Viser(basePilotable, vision));
  }

  private void configureButtonBindings() {

    SmartDashboard.putData("Monter bras", new MonterBras(brasDroit));
    SmartDashboard.putData("Descendre bras", new DescendreBras(brasDroit));

    new JoystickButton(joystick, 2).toggleWhenPressed(new Viser(basePilotable, vision));

    new JoystickButton(joystick, 7).whenHeld(new MonterGrimpeur(grimpeurDroit));
    new JoystickButton(joystick, 8).whenHeld(new GrimperSansRatchet(grimpeurDroit));
    new JoystickButton(joystick, 9).whenHeld(new Grimper(grimpeurDroit));

    new JoystickButton(joystick, 10).whenHeld(new MonterGrimpeur(grimpeurGauche));
    new JoystickButton(joystick, 11).whenHeld(new GrimperSansRatchet(grimpeurGauche));
    new JoystickButton(joystick, 12).whenHeld(new Grimper(grimpeurGauche));

    // new JoystickButton(joystick, 3).toggleWhenPressed(new
    // TournerRoulette(roulette));
    new JoystickButton(joystick, 4).whenPressed(new Tirer(shooter, intake, vision));
    new JoystickButton(joystick, 5).toggleWhenPressed(new PrendreTransporterBallon(intake));
    // new JoystickButton(joystick, 7)
    // new JoystickButton(joystick, 7)

  }

  public Command getAutonomousCommand() {
    return null;
  }

  public void unlockRatchets() {
    grimpeurGauche.unlockRatchet();
    grimpeurDroit.unlockRatchet();
  }

}
