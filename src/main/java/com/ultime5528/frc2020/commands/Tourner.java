package com.ultime5528.frc2020.commands;

import com.ultime5528.frc2020.subsystems.BasePilotable;

/**
 * Tourner
 */
public class Tourner extends AbstractTourner {

    public static double kTolerance = 1;
    private double deltaAngle;
    private double angle = 0;

    public Tourner(BasePilotable basePilotable, double angle) {
        super(basePilotable);
        this.deltaAngle = angle;
    }

    @Override
    public void initialize() {
        super.initialize();
        angle = basePilotable.getGyroAngle() + deltaAngle;
    }
    
    @Override
    public double calculateGoalAngle() {
        return deltaAngle;
    }

    @Override
    public boolean isFinished() {
        return Math.abs(basePilotable.getGyroAngle() - angle) < kTolerance;
    }

}