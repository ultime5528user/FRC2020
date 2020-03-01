/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020.subsystems;

import java.util.OptionalDouble;
import java.util.function.Supplier;

import com.ultime5528.frc2020.Ports;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class VisionController extends SubsystemBase {

  private NetworkTableEntry snapshotEntry;
  private NetworkTableEntry timestampEntry;
  private NetworkTableEntry startVisionEntry;
  private NetworkTableEntry raspberryPiStartedEntry;
  private NetworkTableEntry doSynchronizeEntry;
  private NetworkTableEntry gotSynchronizeEntry;
  private Relay led;

  private static double kFOV = 49.8; // Degrés
  private static double kFocale = 1 / Math.tan(Math.toRadians(kFOV) / 2);

  private VisionSnapshot currentSnapshot;

  private boolean isEnabled = true; // TODO remettre à false ?
  private boolean hasSynchronized = false;
  private long doSynchronizeTime, lag;

  private Supplier<Long> timestampSupplier;

  public VisionController(Supplier<Long> timestampSupplier) {

    var visionTable = NetworkTableInstance.getDefault().getTable("vision");

    startVisionEntry = visionTable.getEntry("start_vision");

    snapshotEntry = visionTable.getEntry("snapshot");
    timestampEntry = visionTable.getEntry("timestamp");
    doSynchronizeEntry = visionTable.getEntry("do_synchronize");
    gotSynchronizeEntry = visionTable.getEntry("got_synchronize");

    this.timestampSupplier = timestampSupplier;

    raspberryPiStartedEntry = visionTable.getEntry("pi_started");
    raspberryPiStartedEntry.addListener(notif -> {
      if (notif.value.getBoolean()) {
        synchronize();
      }
    }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate | EntryListenerFlags.kImmediate);

    startVisionEntry.setBoolean(true);

    led = new Relay(Ports.VISION_LED);
    led.set(Value.kOn);

    readSnapshot();

    SmartDashboard.putNumber("kFocale", kFocale);
  }

  public void initTestCamera() {
    UsbCamera cam = new UsbCamera("cam", 0);
    cam.setResolution(480, 360);

    CvSource inverseSrc = CameraServer.getInstance().putVideo("Inverse", 480, 360);

    Thread visionThread = new Thread(() -> {
      Mat frame = new Mat();
      CvSink sink = new CvSink("cam_sink");
      sink.setSource(cam);
      while (!Thread.interrupted()) {
        try {
          long ret = sink.grabFrame(frame);
          if (ret != 0) {
            Core.flip(frame, frame, -1);
            inverseSrc.putFrame(frame);
          }
        } catch (Exception e) {
        }
      }
      sink.close();
    });

    visionThread.setDaemon(true);
    visionThread.start();

  }

  @Override
  public void periodic() {
    led.set(Value.kOn); // TODO ENLEVERRRRR!

    if (isEnabled) {
      readSnapshot();
    }

    timestampEntry.setNumber(timestampSupplier.get());

  }

  private void synchronize() {
    gotSynchronizeEntry.addListener(notif -> {
      long newTime = timestampSupplier.get();
      lag = (newTime - doSynchronizeTime) / 2;
      NetworkTableInstance.getDefault().getTable("vision").getEntry("lag").setNumber(lag);
      hasSynchronized = true;
    }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

    doSynchronizeTime = timestampSupplier.get();
    doSynchronizeEntry.setDouble(doSynchronizeEntry.getDouble(0.0) + 1.0);
    NetworkTableInstance.getDefault().flush();

  }

  public void enable() {
    isEnabled = true;
    led.set(Value.kOn);
  }

  public void disable() {
    isEnabled = false;
    led.set(Value.kOff);
  }

  public void readSnapshot() {
    String data = snapshotEntry.getString("0;false;0;0");
    currentSnapshot = VisionSnapshot.fromRasperryPiData(data);
  }

  public OptionalDouble getHauteurCible() {
    if (currentSnapshot.found) {
      return OptionalDouble.of(currentSnapshot.hauteur);
    } else {
      return OptionalDouble.empty();
    }
  }

  public long getLastTimestamp() {
    return currentSnapshot.timestamp + lag + (long)(TimedRobot.kDefaultPeriod / 2 * 1000);
  }

  public OptionalDouble getAngleCible() {
    if (currentSnapshot.found) {
      double x = currentSnapshot.centreX;
      double angle = Math.atan(-x / kFocale); // TODO vérifier "-x / kFocale"
      angle = Math.toDegrees(angle);
      return OptionalDouble.of(angle);
    } else {
      return OptionalDouble.empty();
    }
  }

  public static class VisionSnapshot {

    public final long timestamp;
    public final boolean found;
    public final double centreX;
    public final double hauteur;

    public VisionSnapshot() {
      this(0L, false, 0.0, 0.0);
    }

    public VisionSnapshot(long timestamp, boolean found, double centreX, double hauteur) {
      this.timestamp = timestamp;
      this.found = found;
      this.centreX = centreX;
      this.hauteur = hauteur;
    }

    /**
     * Convertis en VisionSnapshot une chaîne de caractères sous le format
     * "timestamp<long>;found<boolean>;centreX<double>;hauteur<double>"
     */
    public static VisionSnapshot fromRasperryPiData(String data) {

      VisionSnapshot snapshot = null;

      try {
        String[] splitted = data.split(";");

        snapshot = new VisionSnapshot(Long.parseLong(splitted[0]), Boolean.parseBoolean(splitted[1]),
            Double.parseDouble(splitted[2]), Double.parseDouble(splitted[3]));

      } catch (Exception e) {
        DriverStation.reportError("Erreur de formatage dans fromRasperryPiData() : " + data, false);
        snapshot = new VisionSnapshot();
      }

      return snapshot;
    }
  }
}
