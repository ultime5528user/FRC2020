package com.ultime5528.frc2020;

/**
 * Ports
 */
public class Ports {
    
    // PWM

    public static final int GRIMPEUR_MOTEUR_GAUCHE      = 2;
    public static final int INTAKE_MOTEUR_BRAS_GAUCHE   = 1;
    public static final int INTAKE_MOTEUR_TRANSPORTEUR  = 3;



    public static final int INTAKE_MOTEUR_INTAKE        = 0;
    
    
    
    public static final int GRIMPEUR_MOTEUR_DROIT       = 4;
    public static final int INTAKE_MOTEUR_BRAS_DROIT    = 5;
    public static final int GRIMPEUR_SERVO_GAUCHE       = 8;
    public static final int GRIMPEUR_SERVO_DROIT        = 9;

    public static final int ROULETTE_MOTEUR             = 6;

    // CAN
    public static final int BASE_PILOTABLE_MOTEUR_DROIT1    = 1;
    public static final int BASE_PILOTABLE_MOTEUR_DROIT2    = 2;
    public static final int BASE_PILOTABLE_MOTEUR_DROIT3    = 3;
    public static final int BASE_PILOTABLE_MOTEUR_GAUCHE1   = 4;
    public static final int BASE_PILOTABLE_MOTEUR_GAUCHE2   = 5;
    public static final int BASE_PILOTABLE_MOTEUR_GAUCHE3   = 6;
    public static final int SHOOTER_MOTEUR                  = 7;
    public static final int SHOOTER_MOTEUR2                 = 8;

    // DIGITAL
    public static final int GRIMPEUR_DROIT_LIMIT_SWITCH_HAUT    = 3;
    public static final int GRIMPEUR_DROIT_LIMIT_SWITCH_BAS     = 4;
    public static final int GRIMPEUR_GAUCHE_LIMIT_SWITCH_HAUT   = 5;
    public static final int GRIMPEUR_GAUCHE_LIMIT_SWITCH_BAS    = 6;
    public static final int INTAKE_PHOTOCELL_HAUT               = 8;
    public static final int INTAKE_PHOTOCELL_BAS                = 9;
    //RELAY
    public static final int VISION_LED = 0;              

    public static class PDP {
        public static final int INTAKE_MOTEUR_TRANSPORTEUR = 2;
    }
    
}