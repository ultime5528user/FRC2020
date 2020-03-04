package com.ultime5528.util;

import com.revrobotics.CANError;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.PeriodicFrame;

import edu.wpi.first.wpilibj.Timer;

/**
 * SparkMaxUtil
 */
public final class SparkMaxUtil {

    public static final double kVoltageCompensation = 12.0;

    public static void handleCANError(CANError error, String message, CANSparkMax sparkMax) {
        if (error != CANError.kOk) {
            LogUtil.reportError("CANError",
                    "Error (" + error.toString() + ") on motor ID " + sparkMax.getDeviceId() + " : " + message);
        }
    }

    public static void handleCANError(CANError error, String message) {
        if (error != CANError.kOk) {
            LogUtil.reportError("CANError", "Error (" + error.toString() + ") : " + message);
        }
    }

    public static void configureSlaveMotor(CANSparkMax slave, CANSparkMax leader, SparkMaxConfig config) {
        configureSlaveMotor(slave, leader, config, false);
    }

    public static void configureSlaveMotor(CANSparkMax slave, CANSparkMax leader, SparkMaxConfig config, boolean inversed) {
        handleCANError(slave.restoreFactoryDefaults(), "restoryFactoryDefaults", slave);
        handleCANError(slave.setPeriodicFramePeriod(PeriodicFrame.kStatus0, 1000), "set status0 rate", slave);
        handleCANError(slave.setPeriodicFramePeriod(PeriodicFrame.kStatus1, 1000), "set status1 rate", slave);
        handleCANError(slave.setPeriodicFramePeriod(PeriodicFrame.kStatus2, 1000), "set status2 rate", slave);
        handleCANError(slave.follow(leader, inversed), "follow", slave);
        configureMotor(slave, config);
    }

    public static void configureMasterMotor(CANSparkMax motor, SparkMaxConfig config) {
        handleCANError(motor.restoreFactoryDefaults(), "restoryFactoryDefaults", motor);
        configureMotor(motor, config);
    }

    public static void configureMotor(CANSparkMax motor, SparkMaxConfig config) {
        handleCANError(motor.setIdleMode(config.idleMode), "setIdleMode", motor);
        handleCANError(motor.enableVoltageCompensation(kVoltageCompensation), "enableVoltageCompensation", motor);
        handleCANError(motor.setSmartCurrentLimit(config.kCurrentLimit), "setSmartCurrentLimit", motor);
        handleCANError(motor.setSecondaryCurrentLimit(config.kSecCurrentLimit), "setSecondaryCurrentLimit", motor);
        handleCANError(motor.setOpenLoopRampRate(config.kRampRate), "setOpenLoopRampRate", motor);
        handleCANError(motor.setClosedLoopRampRate(config.kRampRate), "setClosedLoopRampRate", motor);
        handleCANError(motor.burnFlash(), "burnFlash", motor);
        handleCANError(motor.clearFaults(), "clearFaults", motor);
        Timer.delay(0.250);
    }

    public static class SparkMaxConfig {
        public final IdleMode idleMode;
        public final double kRampRate;
        public final int kCurrentLimit;
        public final int kSecCurrentLimit;

        public SparkMaxConfig(IdleMode idleMode, double kRampRate, int kCurrentLimit, int kSecCurrentLimit) {
            this.idleMode = idleMode;
            this.kRampRate = kRampRate;
            this.kCurrentLimit = kCurrentLimit;
            this.kSecCurrentLimit = kSecCurrentLimit;
        }
    }
}