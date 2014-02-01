package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
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
    static final int MOTOR_LEFT_PWM = 1;
    static final int MOTOR_RIGHT_PWM = 8;
    static final int MOTOR_KICK_PWM = 3;

    //defining digital io constants
    static final int PRESSURE_SENSOR_PORT = 4;
    static final int KICKER_ENCODER_PORT_A = 6;
    static final int KICKER_ENCODER_PORT_B = 7;
    static final int KICKER_OPTICAL_SENSOR_PORT = 2;
    static final int SOLENOID_SHIFT_FORWARD_PORT = 3;
    static final int SOLENOID_SHIFT_BACKWARD_PORT = 4;

    //defining relay constants
    static final int SPIKE_PRESSURE_RELAY = 1;

    //defining joystick numbers
    static final int JOYSTICK_LEFT_USB = 1;
    static final int JOYSTICK_RIGHT_USB = 2;
    static final int JOYSTICK_OPERATOR_USB = 3;

    //defining joystick buttons
    static final int JOYSTICK_TOGGLE_DRIVE = 3;
    static final int JOYSTICK_LOAD_BUTTON = 3;
    static final int JOYSTICK_FIRE_BUTTON = 1;
    static final int JOYSTICK_RESET_BUTTON = 2;
    static final int SOLENOID_OUT_BUTTON = 5;
    static final int SOLENOID_IN_BUTTON = 4;
    static final int SET_SAMPLE_RATE_BUTTON = 6;

    //defining encoder positions
    static final int KICKER_ENCODER_TOP_POSITION = 55; //loading is positive
    static final int KICKER_ENCODER_KICK_POSITION = -16; //kick is negative
    static final int KICKER_ENCODER_REST_POSITION = 0;

    //defining pneumatic objects
    private Compressor compress;
    private DoubleSolenoid testSolenoid;

    //defining others
    private RobotDrive driver;
    private DriverStationLCD lcd;
    private DriverStation ds;
    private Joystick joyLeft;
    private Joystick joyRight;
    private Joystick joyOperator;
    private boolean isTankDrive; //true is tank drive, false is arcade drive
    static final String VERSION_NUMBER = "0.1";
    private KickerStateMachine kickerStates;

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
        
        kickerStates = new KickerStateMachine();
        
        compress = new Compressor(PRESSURE_SENSOR_PORT, SPIKE_PRESSURE_RELAY);
        //comment compressor if you are not using it
        //compress.start();
        
        testSolenoid = new DoubleSolenoid(SOLENOID_SHIFT_FORWARD_PORT, SOLENOID_SHIFT_BACKWARD_PORT);
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
            if (joyOperator.getRawButton(SOLENOID_OUT_BUTTON)) {
                testSolenoid.set(DoubleSolenoid.Value.kForward);
            } else if (joyOperator.getRawButton(SOLENOID_IN_BUTTON)) {
                testSolenoid.set(DoubleSolenoid.Value.kReverse);
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
        kickerStates.setSetpoint(KICKER_ENCODER_REST_POSITION);
        kickerStates.reset();
        kickerStates.setState(kickerStates.INIT); //makes sure robot is on init
        while (isTest() && isEnabled()) {
            kickerStates.stateMachine();
        }
    }
}
