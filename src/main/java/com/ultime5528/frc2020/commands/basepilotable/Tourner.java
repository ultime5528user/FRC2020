package com.ultime5528.frc2020.commands.basepilotable;

import com.ultime5528.frc2020.subsystems.BasePilotable;

/**
 * Tourner
 */
public class Tourner extends AbstractTourner {

    public static double kTolerance = 1;
    private double deltaAngleDegrees;
    private double angleDegrees = 0;

    public Tourner(BasePilotable basePilotable, double angleDegrees, double vitesse, double accel) {
        super(basePilotable, vitesse, accel);
        this.deltaAngleDegrees = angleDegrees;
    }

    @Override
    public void initialize() {
        super.initialize();
        angleDegrees = basePilotable.getAngleDegrees() + deltaAngleDegrees;
    }
    
    /**
     * Retourne l'angle absolu que l'on veut atteindre, en degrés.
     */
    @Override
    public double calculateGoalAngleDegrees() {
        return angleDegrees;
    }

    // @Override
    // public boolean isFinished() {
    //     // return Math.abs(basePilotable.getGyroAngle() - angle) < kTolerance;
    //     return 
    // }

}