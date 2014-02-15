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

    static final double MOTOR_SPEED = 0.4;

    static private Talon lifterMotor;
    static private Encoder lifterEncoder;
    static private Joystick joyOperator;
    static private Joystick joyLeft;
    static protected boolean isUp;
    static protected boolean isDown;

    static private DriverStationLCD lcd;

    static { //analogous to constructor
        lcd = DriverStationLCD.getInstance();
        joyOperator = new Joystick(FRC2014.JOYSTICK_OPERATOR_USB);
        joyLeft = new Joystick(FRC2014.JOYSTICK_LEFT_USB);
        lifterMotor = FRC2014.talonLoader;
        lifterEncoder = FRC2014.lifterEncoder;
        isUp = true;
        isDown = false;
    }

    public static boolean moveUp() {
        isUp = true;
        isDown = false;
        if (lifterEncoder.get() >= FRC2014.LIFTER_ENCODER_TOP_VALUE) {
            lifterMotor.set(0);
            return true;
        }
        double throttle = joyLeft.getZ();
        throttle = (throttle/-2.0)+0.5;
        lifterMotor.set(joyLeft.getZ());
        return false;
    }

    public static boolean moveDown() {
        isUp = false;
        isDown = true;
        if (lifterEncoder.get() <= FRC2014.LIFTER_ENCODER_BOTTOM_VALUE) {
            lifterMotor.set(0);
            return true;
        }
        double throttle = joyLeft.getZ();
        throttle = (throttle/-2.0)+0.5;
        lifterMotor.set(-1 * throttle);
        return false;
    }
}
