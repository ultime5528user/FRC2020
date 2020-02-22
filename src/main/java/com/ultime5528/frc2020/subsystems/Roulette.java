/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020.subsystems;

import java.util.Map;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorSensorV3;
import com.ultime5528.frc2020.Ports;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.SimpleWidget;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.util.Color8Bit;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Config;

public class Roulette extends SubsystemBase implements Loggable {

  @Config(rowIndex = 0, columnIndex = 0, width = 2, height = 1)
  public static double kVitesseTourner = 9;
  @Config(rowIndex = 1, columnIndex = 0, width = 2, height = 1)
  public static double kTempsTourner = 10;
  private VictorSP moteurRoulette;
  private ColorSensorV3 capteurCouleur;
  private SimpleWidget colorWidget;
  private NetworkTableEntry colorWidgetEntry;
  private boolean receivedColor = false;
  private final Color kBlueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429);
  private final Color kGreenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240);
  private final Color kRedTarget = ColorMatch.makeColor(0.561, 0.232, 0.114);
  private final Color kYellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113);

  @Config(rowIndex = 2, columnIndex = 0, width = 2, height = 1)
  private final I2C.Port kCapteurCouleurPort = I2C.Port.kOnboard;

  public Roulette() {
    moteurRoulette = new VictorSP(Ports.ROULETTE_MOTEUR);
    capteurCouleur = new ColorSensorV3(kCapteurCouleurPort);
    addChild("Moteur Roulette", moteurRoulette);
    // Create Boolean widget that displays the color
    colorWidget = Shuffleboard.getTab("Roulette").add("Color", false);
    colorWidget.withPosition(0, 4);
    colorWidget.withProperties(Map.of("colorWhenFalse", "black"));
    colorWidgetEntry = colorWidget.getEntry();
  }

  @Override
  public void periodic() {
    if (!receivedColor) {
      String strColor = "G"; 
      //DriverStation.getInstance().getGameSpecificMessage();
      if (!strColor.isEmpty()) {

        Color color = null;

        switch (strColor.charAt(0)) {
        case 'B':
          color = Color.kRed;
          break;
        case 'G':
          color = Color.kYellow;
          break;
        case 'R':
          color = Color.kBlue;
          break;
        case 'Y':
          color = Color.kGreen;
          break;
        }

        if (color != null) {
          Color8Bit color8Bit = new Color8Bit(color);
          colorWidget.withProperties(
              Map.of("colorWhenTrue", String.format("#%02X%02X%02X", color8Bit.red, color8Bit.green, color8Bit.blue)));
          colorWidgetEntry.setBoolean(true);
          receivedColor = true;
        }

      }
    }

  }

  public void tourner() {
    moteurRoulette.setVoltage(kVitesseTourner);
  }

  public void stop() {
    moteurRoulette.setVoltage(0.0);
  }
}
