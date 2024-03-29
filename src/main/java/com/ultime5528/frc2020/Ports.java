package com.ultime5528.frc2020;

/**
 * Ports
 */
public class Ports {
    
    // PWM
    
    
    public static final int GRIMPEUR_MOTEUR_DROIT       = 0;
    public static final int GRIMPEUR_SERVO_GAUCHE       = 1;
    public static final int INTAKE_MOTEUR_INTAKE        = 2;
    public static final int GRIMPEUR_SERVO_DROIT        = 3;
    public static final int INTAKE_MOTEUR_BRAS_DROIT    = 4;
    public static final int GRIMPEUR_MOTEUR_GAUCHE      = 5;
    public static final int INTAKE_MOTEUR_BRAS_GAUCHE   = 6;
    public static final int INTAKE_MOTEUR_TRANSPORTEUR  = 7;
    public static final int BRAS_INTAKE_DROIT           = 9; 
    public static final int BRAS_INTAKE_GAUCHE          = 8;
    
    
    
    
    
    public static final int ROULETTE_MOTEUR             = -1;
    

    


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
    public static final int BRAS_INTAKE_DROIT_ENCODER_A         = 3;
    public static final int BRAS_INTAKE_DROIT_ENCODER_B         = 2;
    public static final int BRAS_INTAKE_GAUCHE_ENCODER_A        = 1;
    public static final int BRAS_INTAKE_GAUCHE_ENCODER_B        = 0;
    public static final int GRIMPEUR_DROIT_LIMIT_SWITCH_HAUT    = 4;
    public static final int GRIMPEUR_DROIT_LIMIT_SWITCH_BAS     = 5;
    public static final int GRIMPEUR_GAUCHE_LIMIT_SWITCH_HAUT   = 6;
    public static final int GRIMPEUR_GAUCHE_LIMIT_SWITCH_BAS    = 7;
    public static final int INTAKE_PHOTOCELL_HAUT               = 8;
    public static final int INTAKE_PHOTOCELL_BAS                = 9;
    
    //RELAY
    public static final int VISION_LED = 0;              

    public static class PDP {
        public static final int INTAKE_MOTEUR_TRANSPORTEUR = 8;
    }
    
}