package com.ultime5528.util;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.shuffleboard.EventImportance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;

/**
 * LogUtil
 */
public final class LogUtil {

    public final static void reportError(String errorType, String message) {
        Shuffleboard.addEventMarker(errorType, message, EventImportance.kCritical);
        DriverStation.reportError("[" + errorType + "] " + message, true);
    }

    public final static void reportWarning(String warningType, String message) {
        Shuffleboard.addEventMarker(warningType, message, EventImportance.kHigh);
        DriverStation.reportError("[" + warningType + "] " + message, true);
    }
    
}