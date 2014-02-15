/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;

/**
 *
 * @author Administrator
 */
public class BallLifter {

    static final double MOTOR_SPEED = 0.2;

    static private Talon lifterMotor;
    static private Encoder lifterEncoder;
    static private Joystick joyOperator;
    static private Joystick joyLeft;

    static private DriverStationLCD lcd;

    static { //analogous to constructor
        lcd = DriverStationLCD.getInstance();
        joyOperator = new Joystick(FRC2014.JOYSTICK_OPERATOR_USB);
        joyLeft = new Joystick(FRC2014.JOYSTICK_LEFT_USB);
        lifterMotor = FRC2014.talonLoader;
        lifterEncoder = FRC2014.lifterEncoder;
    }

    public static boolean moveUp() {
        if (lifterEncoder.get() >= FRC2014.LIFTER_ENCODER_TOP_VALUE) {
            lifterMotor.set(0);
            return true;
        }
        lifterMotor.set(joyLeft.getZ());
        return false;
    }

    public static boolean moveDown() {
        if (lifterEncoder.get() <= FRC2014.LIFTER_ENCODER_BOTTOM_VALUE) {
            lifterMotor.set(0);
            return true;
        }
        lifterMotor.set(joyLeft.getZ() * -1);
        return false;
    }
}
