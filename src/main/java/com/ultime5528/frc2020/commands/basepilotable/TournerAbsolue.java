package com.ultime5528.frc2020.commands.basepilotable;

import com.ultime5528.frc2020.subsystems.BasePilotable;
import io.github.oblarg.oblog.annotations.Config;

/**
 * Tourner
 */
public class TournerAbsolue extends AbstractTourner {

    @Config
    // private double deltaAngleDegrees;
    // private double angleDegrees = 0;
    private double angleDegrees;

    public TournerAbsolue(BasePilotable basePilotable, double angle, double vitesse, double accel) {
        super(basePilotable, vitesse, accel);
        this.angleDegrees = angle;
        // SmartDashboard.putNumber("delta_angle", deltaAngleDegrees);
    }

    @Override
    public void initialize() {
        super.initialize();
        // angleDegrees = basePilotable.getAngleDegrees();
    }
    
    /**
     * Retourne l'angle absolu que l'on veut atteindre, en degrés.
     */
    @Override
    public double calculateGoalAngleDegrees() {
        // deltaAngleDegrees = SmartDashboard.getNumber("delta_angle", deltaAngleDegrees);
        return angleDegrees;
    }

    // @Override
    // public boolean isFinished() {
    //     // return Math.abs(basePilotable.getGyroAngle() - angle) < kTolerance;
    //     return 
    // }

}