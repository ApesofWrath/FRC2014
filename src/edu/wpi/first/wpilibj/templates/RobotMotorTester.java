/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.Talon;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotMotorTester extends SimpleRobot {

    static final int MOTOR_FRONT_RIGHT_PWM = 2;
    static final int MOTOR_FRONT_LEFT_PWM = 3;
    static final int MOTOR_BACK_RIGHT_PWM = 5;
    static final int MOTOR_BACK_LEFT_PWM = 2;
    static final int MOTOR_KICKER_LEFT_PWM = 1;
    static final int MOTOR_KICKER_RIGHT_PWM = 4;
    static final int MOTOR_LOADER_PWM = 7;
    static final int MOTOR_BACKUP_PWM = 8;
    static final int JOYSTICK_LEFT_USB = 1;
    static final int JOYSTICK_RIGHT_USB = 2;
    static final int JOYSTICK_OPERATOR_USB = 3;
//    static final int ENCODER_LEFT_PORTA = 1;
//    static final int ENCODER_LEFT_PORTB = 2;
//    static final int ENCODER_RIGHT_PORTA = 3;
//    static final int ENCODER_RIGHT_PORTB = 4;

    Joystick joyLeft, joyRight, joyOperator;
//    Encoder encoderLeft, encoderRight;
    Talon talonFrontLeft, talonFrontRight, talonBackLeft, talonBackRight,
            talonKickerLeft, talonKickerRight, talonLoader, talonBackup;

    DriverStationLCD lcd;

    public RobotMotorTester() {
        joyLeft = new Joystick(JOYSTICK_LEFT_USB);
        joyRight = new Joystick(JOYSTICK_RIGHT_USB);
        joyOperator = new Joystick(JOYSTICK_OPERATOR_USB);
        talonFrontLeft = new Talon(MOTOR_FRONT_LEFT_PWM);
        talonFrontRight = new Talon(MOTOR_FRONT_RIGHT_PWM);
        talonBackLeft = new Talon(MOTOR_BACK_LEFT_PWM);
        talonBackRight = new Talon(MOTOR_BACK_RIGHT_PWM);
        talonKickerLeft = new Talon(MOTOR_KICKER_LEFT_PWM);
        talonKickerRight = new Talon(MOTOR_KICKER_RIGHT_PWM);
        talonLoader = new Talon(MOTOR_LOADER_PWM);
        talonBackup = new Talon(MOTOR_BACKUP_PWM);

//        encoderLeft = new Encoder(ENCODER_LEFT_PORTA, ENCODER_LEFT_PORTB);
//        encoderRight = new Encoder(ENCODER_RIGHT_PORTA, ENCODER_RIGHT_PORTB);
        lcd = DriverStationLCD.getInstance();
    }

    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    public void autonomous() {

    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        double maxValue = 1;
        while (isOperatorControl() && isEnabled()) {
            if (joyLeft.getRawButton(6)) {
                maxValue = 1;
            } else if (joyLeft.getRawButton(7)) {
                maxValue = .75;
            } else if (joyLeft.getRawButton(8)) {
                maxValue = .5;
            } else if (joyLeft.getRawButton(9)) {
                maxValue = .25;
            }

            talonFrontLeft.set(joyLeft.getY() * maxValue);
            talonFrontRight.set(joyRight.getY() * maxValue);
            talonBackLeft.set(joyLeft.getY() * maxValue);
            talonBackRight.set(joyRight.getY() * maxValue);

//          lcd.println(DriverStationLCD.Line.kUser1, 1, "Left encoder rate "+encoderLeft.getRate()+"                ");
//          lcd.println(DriverStationLCD.Line.kUser2, 1, "Right encoder rate "+encoderRight.getRate()+"                ");
            lcd.println(DriverStationLCD.Line.kUser3, 1, "Left motor " + joyLeft.getY() * maxValue + "                ");
            lcd.println(DriverStationLCD.Line.kUser4, 1, "Right motor " + joyRight.getY() * maxValue + "                ");
//          lcd.println(DriverStationLCD.Line.kUser5, 1, "Max value "+maxValue);
            lcd.updateLCD();
        }
    }

    /**
     * This function is called once each time the robot enters test mode.
     */
    public void motorTest() {
        double zAxis = joyOperator.getZ();
        lcd.println(DriverStationLCD.Line.kUser6,1, " " + String.valueOf(zAxis).substring(0, 5) + "        ");
        lcd.updateLCD();
        
        if (joyOperator.getRawButton(1)) {
            talonKickerLeft.set(zAxis);
            lcd.println(DriverStationLCD.Line.kUser5,1,"1");
        }
        if (joyOperator.getRawButton(2)) {
            talonBackLeft.set(zAxis);
            lcd.println(DriverStationLCD.Line.kUser5,2,"2");
        }
        if (joyOperator.getRawButton(3)) {
            talonFrontLeft.set(zAxis);
            lcd.println(DriverStationLCD.Line.kUser5,3,"3");
        }
        if (joyOperator.getRawButton(4)) {
            talonKickerRight.set(zAxis);
            lcd.println(DriverStationLCD.Line.kUser5,4,"4");
        }
        if (joyOperator.getRawButton(5)) {
            talonBackRight.set(zAxis);
            lcd.println(DriverStationLCD.Line.kUser5,5,"5");
        }
        if (joyOperator.getRawButton(6)) {
            talonFrontRight.set(zAxis);
            lcd.println(DriverStationLCD.Line.kUser5,6,"6");
        }
        if (joyOperator.getRawButton(7)) {
            talonLoader.set(zAxis);
            lcd.println(DriverStationLCD.Line.kUser5,7,"7");
        }
        if (joyOperator.getRawButton(8)) {
            talonBackup.set(zAxis);
            lcd.println(DriverStationLCD.Line.kUser5,8,"8");
        }
        lcd.updateLCD();
    }
}
