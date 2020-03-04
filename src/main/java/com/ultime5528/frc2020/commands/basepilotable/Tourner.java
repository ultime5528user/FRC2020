package com.ultime5528.frc2020.commands.basepilotable;

import com.ultime5528.frc2020.Constants;
import com.ultime5528.frc2020.subsystems.BasePilotable;
import io.github.oblarg.oblog.annotations.Config;

/**
 * Tourner
 */
@Config.Exclude(exclude = Constants.OBLOG_MATCH)
public class Tourner extends AbstractTourner {

    public static double kTolerance = 1;

    @Config
    private double deltaAngleDegrees;
    // private double angleDegrees = 0;
    private double initialAngleDegrees;

    public Tourner(BasePilotable basePilotable, double angleDegrees, double vitesse, double accel) {
        super(basePilotable, vitesse, accel);
        this.deltaAngleDegrees = angleDegrees;
        // SmartDashboard.putNumber("delta_angle", deltaAngleDegrees);
    }

    @Override
    public void initialize() {
        super.initialize();
        initialAngleDegrees = basePilotable.getAngleDegrees();
    }
    
    /**
     * Retourne l'angle absolu que l'on veut atteindre, en degrés.
     */
    @Override
    public double calculateGoalAngleDegrees() {
        // deltaAngleDegrees = SmartDashboard.getNumber("delta_angle", deltaAngleDegrees);
        return initialAngleDegrees + deltaAngleDegrees;
    }

    // @Override
    // public boolean isFinished() {
    //     // return Math.abs(basePilotable.getGyroAngle() - angle) < kTolerance;
    //     return 
    // }

}