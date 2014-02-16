/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DigitalInput;
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
    static private DigitalInput lifterOptical;
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
        lifterOptical = FRC2014.lifterOpticalSensor;
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
        double multiplier;
        if (lifterEncoder.get() >= FRC2014.LIFTER_ENCODER_SLOW_VALUE) {
            multiplier = 0.75;
        } else {
            multiplier = 1;
        }
        double motorSpeed;
        if (lifterOptical.get() == false) {
            motorSpeed = -1.0 * multiplier;
        } else {
            motorSpeed = -0.8 * multiplier;
        }
        lifterMotor.set(motorSpeed);
        return false;
    }

    public static boolean moveDown() {
        isUp = false;
        isDown = true;
        if (lifterEncoder.get() <= FRC2014.LIFTER_ENCODER_BOTTOM_VALUE) {
            lifterMotor.set(0);
            return true;
        }
        lifterMotor.set(0.3);
        return false;
    }

    /*public static boolean maintainMotors() {
     if (isUp) {
     int error = Math.abs(FRC2014.LIFTER_ENCODER_TOP_VALUE - )
     lifterMotor.set();
     } else {
            
     }
     }*/
}
