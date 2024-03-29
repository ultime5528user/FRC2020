/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.ultime5528.frc2020;

import edu.wpi.first.wpilibj.RobotBase;

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

    public static final boolean ENABLE_CAN_BASE_PILOTABLE = RobotBase.isReal() && true;
    public static final boolean ENABLE_CAN_SHOOTER = RobotBase.isReal() && true;
    public static final boolean ENABLE_PDP = true;
    public static final boolean ENABLE_VISION = true;
    public static final boolean ENABLE_NAVX = true;
    public static final boolean ENABLE_COMMAND_TROUBLESHOOTING_PRINTS = false;

    public static final boolean OBLOG_MATCH = true;
}
