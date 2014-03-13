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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Administrator
 */
public class BallLifter {

    static final double MOTOR_SPEED = 0.4;

    static private Talon talonLoader;
    static private Encoder lifterEncoder;
    static private DigitalInput lifterOpticalSensor;
    static private DigitalInput lifterLimitSwitch;
    static private Joystick joyOperator;
    static private Joystick joyLeft;
    static public boolean isUp;
    static public boolean isDown;
    static protected boolean isCalibrated = false;

    static private DriverStationLCD lcd;

    static { //analogous to constructor
        lcd = DriverStationLCD.getInstance();
        joyOperator = new Joystick(FRC2014.JOYSTICK_OPERATOR_USB);
        joyLeft = new Joystick(FRC2014.JOYSTICK_LEFT_USB);
        talonLoader = FRC2014.talonLoader;
        lifterEncoder = FRC2014.lifterEncoder;
        lifterOpticalSensor = FRC2014.lifterOpticalSensor;
        lifterLimitSwitch = FRC2014.lifterLimitSwitch;
        isUp = true;
        isDown = false;
    }

    public static boolean moveUp() {
        isUp = true;
        isDown = false;

        if (lifterLimitSwitch.get() == false) {
            talonLoader.set(0);
            isUp = true;
            isDown = false;
            return true;
        }
        double multiplier;
        //Used to change the speed of the motor based on if has passes
        /*if (lifterEncoder.get() >= SmartDashboard.getNumber("Lifter Slowdown Threshold", FRC2014.LIFTER_ENCODER_SLOW_VALUE)) {
            multiplier = SmartDashboard.getNumber("Lifter Slowdown Multiplier", 0.95);
        } else {
            multiplier = 1.00;
        }
        double motorSpeed;
        
        //Ball in lifter
        /*if (lifterOpticalSensor.get() == false) {
            motorSpeed = -1.0 * multiplier;
        } else { //Ball not in lifter
            motorSpeed = -0.95 * multiplier;
        }
        motorSpeed = -1 * multiplier;*/
        double motorSpeed = -1;
        talonLoader.set(motorSpeed);
        return false;

    }

    public static boolean moveDown() {
        isUp = false;
        isDown = false;
        if (lifterEncoder.get() <= FRC2014.LIFTER_ENCODER_BOTTOM_VALUE) {
            talonLoader.set(0);
            isUp = false;
            isDown = true;
            return true;
        }
        talonLoader.set(0.5);
        return false;
    }

    public static void resetEncoders() {
        lifterEncoder.reset();
        isCalibrated = true;
        System.out.println("Resetting");
    }
    
    public static void stopMotors() {
        talonLoader.set(0);
    }

    public static boolean maintainMotors() {
        if (lifterLimitSwitch.get() == true && isUp == true) {
           return moveUp();
        } else {
            stopMotors();
            return true;
        }
    }
}
