/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean constants. This class should not be used for any other
 * purpose. All constants should be declared globally (i.e. public static). Do
 * not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the constants are needed, to reduce verbosity.
 */
public final class Constants {

    public static final boolean ENABLE_CAN_BASE_PILOTABLE = false;
    public static final boolean ENABLE_CAN_SHOOTER = false;

    public static class Ports {

        // PWM
        public static final int GRIMPEUR_MOTEUR = 0;
        public static final int GRIMPEUR_SERVO = 1;
        public static final int ROULETTE_MOTEUR = 2;

        public static final int INTAKE_MOTEUR = 3;
        public static final int TRANSPORTEUR_MOTEUR = 4;

        // CAN

        public static final int BASE_PILOTABLE_MOTEUR_DROIT1 = 1;
        public static final int BASE_PILOTABLE_MOTEUR_DROIT2 = 2;
        public static final int BASE_PILOTABLE_MOTEUR_DROIT3 = 3;
        public static final int BASE_PILOTABLE_MOTEUR_GAUCHE1 = 4;
        public static final int BASE_PILOTABLE_MOTEUR_GAUCHE2 = 5;
        public static final int BASE_PILOTABLE_MOTEUR_GAUCHE3 = 6;
        public static final int SHOOTER_MOTEUR = 7;
        public static final int SHOOTER_MOTEUR2 = 8;

        // DIGITAL
        public static final int PHOTOCELL_BAS = 1;
        public static final int PHOTOCELL_HAUT = 2;
        

    }
}
