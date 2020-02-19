package com.ultime5528.util;

import com.revrobotics.CANError;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.PeriodicFrame;

/**
 * SparkMaxUtil
 */
public final class SparkMaxUtil {

    public static final double kRampRate = 1.0;
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

    public static void configureSlaveMotor(CANSparkMax slave, CANSparkMax leader) {
        configureSlaveMotor(slave, leader, false);
    }

    public static void configureSlaveMotor(CANSparkMax slave, CANSparkMax leader, boolean inversed) {
        handleCANError(slave.restoreFactoryDefaults(), "restoryFactoryDefaults", slave);
        handleCANError(slave.setPeriodicFramePeriod(PeriodicFrame.kStatus0, 1000), "set status0 rate", slave);
        handleCANError(slave.setPeriodicFramePeriod(PeriodicFrame.kStatus1, 1000), "set status1 rate", slave);
        handleCANError(slave.setPeriodicFramePeriod(PeriodicFrame.kStatus2, 1000), "set status2 rate", slave);
        handleCANError(slave.follow(leader, inversed), "follow", slave);
        configureMotor(slave);
    }

    public static void configureMasterMotor(CANSparkMax motor) {
        handleCANError(motor.restoreFactoryDefaults(), "restoryFactoryDefaults", motor);
        configureMotor(motor);
    }

    public static void configureMotor(CANSparkMax motor) {
        handleCANError(motor.setIdleMode(IdleMode.kCoast), "setIdleMode", motor);
        handleCANError(motor.enableVoltageCompensation(kVoltageCompensation), "enableVoltageCompensation", motor);
        handleCANError(motor.setOpenLoopRampRate(kRampRate), "setClosedLoopRampRate", motor);
        handleCANError(motor.burnFlash(), "burnFlash", motor);
        handleCANError(motor.clearFaults(), "clearFaults", motor);
        Timer.delay(.250); //TODO Tester voir si shooter marche
    }

}