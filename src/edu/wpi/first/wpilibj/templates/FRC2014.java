package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/*
 FRC2014 Code
 Document your changes in your git commit messages
 View commit messages by right clicking in project folder and selecting git log.
 */
public class FRC2014 extends SimpleRobot {
    // <editor-fold defaultstate="collapsed" desc="Variable Definitions">

    //defining pwm constants
    static final int MOTOR_FRONT_RIGHT_PWM = 5;
    static final int MOTOR_FRONT_LEFT_PWM = 2;
    static final int MOTOR_BACK_RIGHT_PWM = 7;
    static final int MOTOR_BACK_LEFT_PWM = 1;
    static final int MOTOR_KICK_PWM = 3;
    static final int SERVO_CAMERA_LR_PWM = 8;
    static final int SERVO_CAMERA_UD_PWM = 9;

    //defining digital io constants
    static final int PRESSURE_SENSOR_PORT = 4;
    static final int KICKER_ENCODER_PORT_A = 6;
    static final int KICKER_ENCODER_PORT_B = 7;
    static final int KICKER_OPTICAL_SENSOR_PORT = 2;
    
    //defining solenoid constants
    static final int SOLENOID_LEFT_SHIFT_HIGH_PORT = 2; //gear shifting
    static final int SOLENOID_LEFT_SHIFT_LOW_PORT = 1; //gear shifting
    static final int SOLENOID_RIGHT_SHIFT_HIGH_PORT = 4; //gear shifting
    static final int SOLENOID_RIGHT_SHIFT_LOW_PORT = 3; //gear shifting

    //defining relay constants
    static final int SPIKE_PRESSURE_RELAY = 1;

    //defining joystick numbers
    static final int JOYSTICK_LEFT_USB = 1;
    static final int JOYSTICK_RIGHT_USB = 2;
    static final int JOYSTICK_OPERATOR_USB = 3;

    //defining joystick buttons
    static final int JOYSTICK_TOGGLE_DRIVE = 3; //for left joystick
    static final int JOYSTICK_LOAD_BUTTON = 3; //for operator joystick
    static final int JOYSTICK_FIRE_BUTTON = 1; //for operator joystick
    static final int JOYSTICK_RESET_BUTTON = 2; //for operator joystick
    static final int JOYSTICK_HIGH_SHIFT_BUTTON = 4; //for left joystick
    static final int JOYSTICK_LOW_SHIFT_BUTTON = 5; //for left joystick
    static final int SET_SAMPLE_RATE_BUTTON = 6; //for operator joystick
    static final int JOYSTICK_TAKE_PICTURE_BUTTON = 12; //for operator joystick

    //defining encoder positions
    static final int KICKER_ENCODER_TOP_POSITION = 55; //loading is positive
    static final int KICKER_ENCODER_KICK_POSITION = -16; //kick is negative
    static final int KICKER_ENCODER_REST_POSITION = 0;
    
    //defining speeds
    static final double CAMERA_SERVO_SPEED = 0.002;

    //defining pneumatic objects
    private Compressor compress;
    private DoubleSolenoid leftDriveSolenoid;
    private DoubleSolenoid rightDriveSolenoid;

    //defining others
    private RobotDrive driver;
    private DriverStationLCD lcd;
    private DriverStation ds;
    private Joystick joyLeft;
    private Joystick joyRight;
    private Joystick joyOperator;
    private Servo cameraUpDownServo, cameraLeftRightServo;
    private boolean isTankDrive; //true is tank drive, false is arcade drive
    static final String VERSION_NUMBER = "0.2.2";
    private KickerStateMachine kickerStates;

    // </editor-fold>
    
    /**
     * This function is called as soon as the robot is enabled.
     */
    public FRC2014() { //use this constructor for instantiating variables
        driver = new RobotDrive(MOTOR_FRONT_LEFT_PWM, MOTOR_BACK_LEFT_PWM, MOTOR_FRONT_RIGHT_PWM, MOTOR_BACK_RIGHT_PWM);
        driver.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        driver.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        driver.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
        driver.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
        
        lcd = DriverStationLCD.getInstance();
        ds = DriverStation.getInstance();

        joyLeft = new Joystick(JOYSTICK_LEFT_USB);
        joyRight = new Joystick(JOYSTICK_RIGHT_USB);
        joyOperator = new Joystick(JOYSTICK_OPERATOR_USB);

        isTankDrive = true;

        //kickerStates = new KickerStateMachine();
        compress = new Compressor(PRESSURE_SENSOR_PORT, SPIKE_PRESSURE_RELAY);

        leftDriveSolenoid = new DoubleSolenoid(SOLENOID_LEFT_SHIFT_HIGH_PORT, SOLENOID_LEFT_SHIFT_LOW_PORT);
        rightDriveSolenoid = new DoubleSolenoid(SOLENOID_RIGHT_SHIFT_HIGH_PORT, SOLENOID_RIGHT_SHIFT_LOW_PORT);

        cameraLeftRightServo = new Servo(SERVO_CAMERA_LR_PWM);
        cameraUpDownServo = new Servo(SERVO_CAMERA_UD_PWM);
    }
    
   
    public void robotInit() { //use this method for setup of any kind
        /*SmartDashboard.putBoolean("Camera Initialized", false);
        System.out.println("Attempting to initialize Camera.");
        double time = RobotVision.initializeCamera();
        System.out.println("Initialization completed in "+time+" seconds");
        SmartDashboard.putBoolean("Camera Initialized", (time!=-1.0));
        */
        //comment compressor if you are not using it
        //compress.start();
        
        SmartDashboard.putString("Version Number", VERSION_NUMBER);
    }

    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    public void autonomous() {
        lcd.println(DriverStationLCD.Line.kUser1, 1, "autonomous v" + VERSION_NUMBER);
        lcd.updateLCD();
        cameraLeftRightServo.set(SmartDashboard.getNumber("Left Right Camera", .5));
        cameraUpDownServo.set(SmartDashboard.getNumber("Up Down Camera", .5));
//        RobotVision.ResultReport results;
//         for(int i=0; i<3; i++) {
//         results = RobotVision.cameraVision();
//         if(results.targetExists && results.isHot){
//         kickerStates.reset();
//         kickerStates.setState(kickerStates.KICK);
//         while(kickerStates.getState()!=kickerStates.INIT) {
//         kickerStates.stateMachine();
//         }
//         break;
//         }
//         else {
//         cameraLeftRightServo.set(.5+(DIRECTION*.1));
//         cameraUpDownServo.set(.5+(DIRECTION*.1))
//         }
//         }
//         //turnFortyFiveDegrees();
//         kickerStates.reset();
//         kickerStates.setState(kickerStates.KICK);
//         while(kickerStates.getState()!=kickerStates.INIT) {
//         kickerStates.stateMachine();
//         }
        while (isAutonomous() && isEnabled()) {
            RobotVision.ResultReport results = RobotVision.cameraVision();
            if (results == null) {
                lcd.println(DriverStationLCD.Line.kUser2, 1, "failure                        ");
                lcd.updateLCD();
                continue;
            }
            lcd.println(DriverStationLCD.Line.kUser2, 1, "target is " + results.targetExists);
            lcd.println(DriverStationLCD.Line.kUser3, 1, "hot : " + results.isHot);
            lcd.println(DriverStationLCD.Line.kUser4, 1, "distance is " + results.distance);
            lcd.updateLCD();
        }
    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        lcd.println(DriverStationLCD.Line.kUser1, 1, "teleoperated v" + VERSION_NUMBER);
        lcd.updateLCD();
        boolean oldToggleDriveValue = false, oldPictureValue = false;
        double upDownServoValue = 0.5, leftRightServoValue = 0.5;

        while (isOperatorControl() && isEnabled()) {

            // <editor-fold defaultstate="collapsed" desc="Drive Toggler">
            boolean toggleDriveValue = joyLeft.getRawButton(JOYSTICK_TOGGLE_DRIVE);
            if (toggleDriveValue && (oldToggleDriveValue == false)) { //detects state change
                isTankDrive = !isTankDrive;
            }
            if (isTankDrive) {
                driver.tankDrive(joyLeft, joyRight);
                lcd.println(DriverStationLCD.Line.kUser2, 1, "Tank Drive    ");
                lcd.updateLCD();
                SmartDashboard.putString("Drive Mode", "Tank Drive");
            } else {
                driver.arcadeDrive(joyLeft);
                lcd.println(DriverStationLCD.Line.kUser2, 1, "Arcade Drive");
                lcd.updateLCD();
                SmartDashboard.putString("Drive Mode", "Arcade Drive");
            }
            oldToggleDriveValue = toggleDriveValue;
            
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="Solenoid Shifter">
            /*
            if (joyLeft.getRawButton(JOYSTICK_HIGH_SHIFT_BUTTON)) {
                leftDriveSolenoid.set(DoubleSolenoid.Value.kForward);
                rightDriveSolenoid.set(DoubleSolenoid.Value.kForward);
                SmartDashboard.putBoolean("Gear Solenoid", true);
            } else if (joyLeft.getRawButton(JOYSTICK_LOW_SHIFT_BUTTON)) {
                leftDriveSolenoid.set(DoubleSolenoid.Value.kReverse);
                rightDriveSolenoid.set(DoubleSolenoid.Value.kReverse);
                SmartDashboard.putBoolean("Gear Solenoid", false);
            }
            
            */
            //</editor-fold>
            
            // <editor-fold defaultstate="collapsed" desc="Camera Mover">
            /*
            double axis5 = joyOperator.getRawAxis(5);
            double axis6 = joyOperator.getRawAxis(6);
            
            lcd.println(DriverStationLCD.Line.kUser3, 1, "5 is "+axis5);
            lcd.println(DriverStationLCD.Line.kUser4, 1, "6 is "+axis6);
            lcd.updateLCD();
            
            SmartDashboard.putNumber("Axis 5", axis5);
            SmartDashboard.putNumber("Axis 6", axis6);
            
            if (axis5 == 1.0) {
                leftRightServoValue+=CAMERA_SERVO_SPEED;
            } else if (axis5 == -1.0) {
                leftRightServoValue-=CAMERA_SERVO_SPEED;
            }
            if (axis6 == 1.0) {
                upDownServoValue+=CAMERA_SERVO_SPEED;
            } else if (axis6 == -1.0) {
                upDownServoValue-=CAMERA_SERVO_SPEED;
            }
            
            if (leftRightServoValue>1) {
                leftRightServoValue = 1;
            } else if (leftRightServoValue<0) {
                leftRightServoValue = 0;
            }
            if (upDownServoValue>1) {
                upDownServoValue = 1;
            } else if (upDownServoValue<0) {
                upDownServoValue = 0;
            }
            
            cameraLeftRightServo.set(leftRightServoValue);
            cameraUpDownServo.set(upDownServoValue);
            
            if (joyOperator.getRawButton(JOYSTICK_TAKE_PICTURE_BUTTON) && (!oldPictureValue)) {
                if (RobotVision.takePicture()) {
                    oldPictureValue = true;
                } else {
                    oldPictureValue = false;
                }
            } else {
                oldPictureValue = false;
            }
            */
            //</editor-fold>
        }
    }

    /**
     * This function is called once each time the robot enters test mode.
     */
    public void test() {
        lcd.println(DriverStationLCD.Line.kUser1, 1, "test v" + VERSION_NUMBER);
        lcd.updateLCD();
        while (isTest() && isEnabled()) {
//            kickerStates.setSetpoint(KICKER_ENCODER_REST_POSITION);
//            kickerStates.reset();
//            kickerStates.setState(kickerStates.INIT); //makes sure robot is on init
//            while (isTest() && isEnabled()) {
//            kickerStates.stateMachine();
         }
    }
}