/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DigitalInput;
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
    static private DigitalInput limitSwitchBottom;
    static private Encoder lifterEncoder;
    static private Joystick joyOperator;
    static private boolean isCalibrated;

    static { //analogous to constructor
        joyOperator = new Joystick(FRC2014.JOYSTICK_OPERATOR_USB);
        isCalibrated = false;
        lifterMotor = new Talon(FRC2014.MOTOR_LIFT_PWM);
        limitSwitchBottom = new DigitalInput(FRC2014.LIFTER_LIMIT_SWITCH_BOTTOM);
        lifterEncoder = new Encoder(FRC2014.LIFTER_ENCODER_PORT_A, FRC2014.LIFTER_ENCODER_PORT_B);
        calibrate();
    }

    public static boolean calibrate() {
        if (limitSwitchBottom.get() == true) {
            lifterEncoder.reset();
            if (lifterEncoder.getStopped()) { //start the encoder if it hasn't been started yet
                lifterEncoder.start();
            }
            isCalibrated = true;
        } else {
            isCalibrated = false;
        }
        return isCalibrated;
    }

    public static boolean moveUp() {
        if (isCalibrated) {
//            if (lifterEncoder.get() >= FRC2014.LIFTER_ENCODER_TOP_VALUE) {
            if (joyOperator.getRawButton(10)) {
                return true;
            }
            lifterMotor.set(MOTOR_SPEED);
            return false;
        } else {
            if (calibrate()) {
                return false;
            } else {
                System.out.println("Failure");
                return false;
            }
        }
    }

    public static boolean moveDown() {
        if (limitSwitchBottom.get() == true) {
            lifterMotor.set(0);
            calibrate();
            return true;
        }
        lifterMotor.set(MOTOR_SPEED * -1);
        return false;
    }
}
