package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.RobotDrive;

/*
    FRC2014 Code
    Document your changes in your git commit messages
    View commit messages by right clicking in project folder and selecting git log.
*/

public class FRC2014 extends SimpleRobot {
    // <editor-fold defaultstate="collapsed" desc="Variable DefinitionS">

    //defining pwm constants
    final int MOTOR_LEFT_PWM = 1;
    final int MOTOR_RIGHT_PWM = 8;

    //defining digital io constants
    final int PRESSURE_SENSOR_PORT = 4;

    //defining joystick numbers
    final int JOYSTICK_LEFT_USB = 1;
    final int JOYSTICK_RIGHT_USB = 2;
    final int JOYSTICK_OPERATOR_USB = 3;

    //defining others
    RobotDrive driver;
    Joystick joyLeft;
    Joystick joyRight;
    Joystick joyOperator;

    // </editor-fold>
    /**
     * This function is called as soon as the robot is enabled.
     */
    public FRC2014() {
        driver = new RobotDrive(MOTOR_LEFT_PWM, MOTOR_RIGHT_PWM);

        joyLeft = new Joystick(JOYSTICK_LEFT_USB);
        joyRight = new Joystick(JOYSTICK_RIGHT_USB);
        joyOperator = new Joystick(JOYSTICK_OPERATOR_USB);
    }

    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    public void autonomous() {
        while (isAutonomous() && isEnabled()) {

        }
    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        while (isOperatorControl() && isEnabled()) {
            driver.tankDrive(joyLeft, joyRight);
        }
    }

    /**
     * This function is called once each time the robot enters test mode.
     */
    public void test() {
        while (isTest() && isEnabled()) {

        }
    }
}
