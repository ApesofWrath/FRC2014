package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.DriverStation;

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

    //defining joystick buttons
    final int JOYSTICK_TOGGLE_DRIVE = 3;

    //defining others
    RobotDrive driver;
    DriverStationLCD lcd;
    DriverStation ds;
    Joystick joyLeft;
    Joystick joyRight;
    Joystick joyOperator;
    boolean isTankDrive; //true is tank drive, false is arcade drive
    final String VERSION_NUMBER = "0.1";

    // </editor-fold>
    /**
     * This function is called as soon as the robot is enabled.
     */
    public FRC2014() {
        driver = new RobotDrive(MOTOR_LEFT_PWM, MOTOR_RIGHT_PWM);
        lcd = DriverStationLCD.getInstance();
        ds = DriverStation.getInstance();

        joyLeft = new Joystick(JOYSTICK_LEFT_USB);
        joyRight = new Joystick(JOYSTICK_RIGHT_USB);
        joyOperator = new Joystick(JOYSTICK_OPERATOR_USB);

        isTankDrive = true;
    }

    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    public void autonomous() {
        lcd.println(DriverStationLCD.Line.kUser1, 1, "autonomous v" + VERSION_NUMBER);
        lcd.updateLCD();
        while (isAutonomous() && isEnabled()) {

        }
    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        lcd.println(DriverStationLCD.Line.kUser1, 1, "teleoperated v" + VERSION_NUMBER);
        lcd.updateLCD();
        boolean oldToggleDriveValue = false;

        while (isOperatorControl() && isEnabled()) {

            boolean toggleDriveValue = joyLeft.getRawButton(JOYSTICK_TOGGLE_DRIVE);
            if (toggleDriveValue && (oldToggleDriveValue == false)) { //detects state change
                isTankDrive = !isTankDrive;
            }
            if (isTankDrive) {
                driver.tankDrive(joyLeft, joyRight);
                lcd.println(DriverStationLCD.Line.kUser2, 1, "Tank Drive");
                lcd.updateLCD();
            } else {
                driver.arcadeDrive(joyLeft);
                lcd.println(DriverStationLCD.Line.kUser2, 1, "Arcade Drive");
                lcd.updateLCD();
            }
            oldToggleDriveValue = toggleDriveValue;

        }
    }

    /**
     * This function is called once each time the robot enters test mode.
     */
    public void test() {
        lcd.println(DriverStationLCD.Line.kUser1, 1, "test v" + VERSION_NUMBER);
        lcd.updateLCD();
        while (isTest() && isEnabled()) {
            
        }
    }
}
