package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/*
 FRC2014 Code
 Document your changes in your git commit messages 
 View commit messages by right clicking in project folder and selecting git log.
 */
//TODO:
//Key: if it has an x, it has been coded but not tested. If you test, delete the todo item
//maintenance of kicker and lifter positions X
//manual control for kicker and lifter X
//synchronize kicker motors
public class FRC2014 extends SimpleRobot {
    // <editor-fold defaultstate="collapsed" desc="Variable Definitions">

    //defining pwm constants. these go on the digital sidecar
    static final int MOTOR_FRONT_RIGHT_PWM = 6;
    static final int MOTOR_FRONT_LEFT_PWM = 3;
    static final int MOTOR_BACK_RIGHT_PWM = 5;
    static final int MOTOR_BACK_LEFT_PWM = 2;
    static final int MOTOR_KICKER_RIGHT_PWM = 4;
    static final int MOTOR_KICKER_LEFT_PWM = 1;
    static final int MOTOR_LOADER_PWM = 7;
    static final int MOTOR_BACKUP_PWM = 8;

    static final int SERVO_CAMERA_LR_PWM = 9;
    static final int SERVO_CAMERA_UD_PWM = 10;

    //defining digital io constants. these go on the digital sidecar
    static final int PRESSURE_SENSOR_PORT = 2;

    static final int KICKER_LEFT_ENCODER_PORT_A = 3; //6;
    static final int KICKER_LEFT_ENCODER_PORT_B = 4; //7;

    static final int KICKER_RIGHT_ENCODER_PORT_A = 7;
    static final int KICKER_RIGHT_ENCODER_PORT_B = 8;

    static final int LEFT_DRIVE_ENCODER_PORT_A = 5;
    static final int LEFT_DRIVE_ENCODER_PORT_B = 6;
    static final int RIGHT_DRIVE_ENCODER_PORT_A = 11;
    static final int RIGHT_DRIVE_ENCODER_PORT_B = 12;
    static final int KICKER_OPTICAL_SENSOR_PORT = 13;
    static final int LIFTER_CHECKBALL_OPTICAL_SENSOR_PORT = 14;
    static final int LIFTER_LIMIT_SWITCH_PORT = 1;

    static final int LIFTER_ENCODER_PORT_A = 9;
    static final int LIFTER_ENCODER_PORT_B = 10;

    //defining solenoid constants. these go directly on the cRIO
    static final int SOLENOID_SHIFT_HIGH_PORT = 2; //gear shifting
    static final int SOLENOID_SHIFT_LOW_PORT = 1; //gear shifting

    //defining relay constants. these go on the digital sidecar
    static final int SPIKE_PRESSURE_RELAY = 1;

    //defining joystick numbers
    static final int JOYSTICK_LEFT_USB = 1;
    static final int JOYSTICK_RIGHT_USB = 2;
    static final int JOYSTICK_OPERATOR_USB = 3;

    //defining joystick buttons
    static final int JOYSTICK_HIGH_SHIFT_BUTTON = 3; //for left and right joystick
    static final int JOYSTICK_LOW_SHIFT_BUTTON = 2; //for left and right joystick
    static final int JOYSTICK_TANKMODE_BUTTON = 6; //for left joystick
    static final int JOYSTICK_ARCADEMODE_BUTTON = 7;
    static final int JOYSTICK_SPLITARCADEMODE_BUTTON = 8;

    static final int JOYSTICK_FIRE_BUTTON = 1; //for operator joystick
    static final int JOYSTICK_AUTO_LIFT_BUTTON = 2; //for operator joystick
    static final int JOYSTICK_LIFTER_DOWN_BUTTON = 3; //for operator joystick
    static final int JOYSTICK_LOAD_BUTTON = 4; //for operator joystick
    static final int JOYSTICK_LIFTER_UP_BUTTON = 5; //for operator joystick
    static final int JOYSTICK_UNLOAD_BUTTON = 6;
    static final int JOYSTICK_MANUAL_BUTTON = 7; //for operator joystick
    static final int JOYSTICK_RESET_ENCODERS_BUTTON = 8; // The button in Test to power the Kicker Motors properly
//    static final int SET_SAMPLE_RATE_BUTTON = 10; //for operator joystick
    static final int JOYSTICK_TEST_KICKER_BUTTON = 11; // The button in Test to power the Kicker Motors properly
    static final int JOYSTICK_PASS_BUTTON = 11; //for operator joystick
    static final int JOYSTICK_TAKE_PICTURE_BUTTON = 12; //for operator joystick

    //defining encoder positions
    static final int KICKER_ENCODER_TOP_POSITION = -170; // -162, -140 moves to "12:00", top is now -171
    static final int KICKER_ENCODER_ERROR_POSITION = -200; //if cocked without a ball
    static final int KICKER_ENCODER_KICK_POSITION = 160;
    static final int KICKER_ENCODER_REST_POSITION = -10;
    static final int KICKER_ENCODER_PASS_POSITION = 40;
    static final int LIFTER_ENCODER_TOP_VALUE = 12; //13
    static final int LIFTER_ENCODER_AUTO_KICKER_VALUE = 0; //when kicker loads while lifter is going auto
    static final int LIFTER_ENCODER_SLOW_VALUE = 0; //slow lifter as it approaches up
    static final int LIFTER_ENCODER_BOTTOM_VALUE = -56; //-58

    //defining speeds
    static final double CAMERA_SERVO_SPEED = 0.006;
    static final double COCKING_SPEED = 0.295;

    //defining directions
    static final int LIFTER_GOING_UP = 1;
    static final int LIFTER_GOING_DOWN = 2;
    static final int LIFTER_NOT_MOVING = 3;
    static final int KICKER_LOADING = 1;
    static final int KICKER_KICKING = 2;
    static final int KICKER_NOT_MOVING = 3;
    static final int KICKER_PASSING = 4;
    static final int KICKER_UNLOADING = 5;

    //p-controller constants
    static final double P_LIFTER = 0.084; //should be 0.084;
    static final double P_KICKER = 0.012;

    static final double autonDriveTime = 1.5; // drive this duration in Auton
    static final double kickerAbortTime = 1.0; // seconds
    static final double takingPhotoTime = 5.0; // seconds
    static final double lowerAbortTime = 0.75; // seconds

    //for autonomous
    static final int WANTED_NUMBER_OF_HOT_PHOTOS = 2;
    //defining pneumatic objects
    private Compressor compress;
    private DoubleSolenoid shiftingSolenoid;

    //defining others
    protected static RobotDrive driver;
    protected static DriverStationLCD lcd;
    private DriverStation ds;
    private DriverStation.Alliance ally;
    private Joystick joyLeft;
    private Joystick joyRight;
    private Joystick joyOperator;
    protected static Encoder kickerEncoderLeft, kickerEncoderRight, lifterEncoder, rightDriveEncoder, leftDriveEncoder;
    private Servo cameraUpDownServo, cameraLeftRightServo;
    static final String VERSION_NUMBER = "0.2.4";
    protected static DigitalInput kickerOpticalSensor, lifterOpticalSensor;
    //kickerOpticalSensor normally false, lifterOpticalSensor normally true, lifterLimitSwitch normally true
    // private KickerStateMachine kickerStates;
    private int driveMode;
    private int lifterDirection = LIFTER_NOT_MOVING;
    private int kickerDirection = KICKER_NOT_MOVING;

    protected static Talon talonFrontLeft, talonFrontRight, talonBackLeft, talonBackRight,
            talonKickerLeft, talonKickerRight, talonLoader, talonBackup;
    //negative value moves talonKickerLeft to kick
    //positive value moves talonKickerRight to kick
    //negative value moves talonLoader to up
    protected static DigitalInput lifterLimitSwitch;

    private boolean newKickOptState = true, oldKickOptState = true;
    private boolean newLiftOptState = false;
    private boolean oldLiftOptState = false;
    private boolean newManualState = false;
    private boolean oldManualState = false;

    private Timer redAutoLiftTimer;
    protected static boolean isAutonomous = false;
    private boolean timerStarted = false;
    // </editor-fold>
    /**
     * This function is called as soon as the robot is enabled.
     */
    public FRC2014() { //use this constructor for instantiating variables

        lcd = DriverStationLCD.getInstance();
        ds = DriverStation.getInstance();

        redAutoLiftTimer = new Timer();

        joyLeft = new Joystick(JOYSTICK_LEFT_USB);
        joyRight = new Joystick(JOYSTICK_RIGHT_USB);
        joyOperator = new Joystick(JOYSTICK_OPERATOR_USB);

        driveMode = 1; // default to tank

        kickerOpticalSensor = new DigitalInput(KICKER_OPTICAL_SENSOR_PORT);
        lifterOpticalSensor = new DigitalInput(LIFTER_CHECKBALL_OPTICAL_SENSOR_PORT);
        lifterLimitSwitch = new DigitalInput(LIFTER_LIMIT_SWITCH_PORT);

        compress = new Compressor(PRESSURE_SENSOR_PORT, SPIKE_PRESSURE_RELAY);

        // Shifter solenoids
        shiftingSolenoid = new DoubleSolenoid(SOLENOID_SHIFT_HIGH_PORT,
                SOLENOID_SHIFT_LOW_PORT);

        cameraLeftRightServo = new Servo(SERVO_CAMERA_LR_PWM);
        cameraUpDownServo = new Servo(SERVO_CAMERA_UD_PWM);
        talonFrontLeft = new Talon(MOTOR_FRONT_LEFT_PWM);
        talonFrontRight = new Talon(MOTOR_FRONT_RIGHT_PWM);
        talonBackLeft = new Talon(MOTOR_BACK_LEFT_PWM);
        talonBackRight = new Talon(MOTOR_BACK_RIGHT_PWM);
        talonKickerLeft = new Talon(MOTOR_KICKER_LEFT_PWM);
        talonKickerRight = new Talon(MOTOR_KICKER_RIGHT_PWM);
        talonLoader = new Talon(MOTOR_LOADER_PWM);
        talonBackup = new Talon(MOTOR_BACKUP_PWM);

        //kickerStates = new KickerStateMachine(talonKickerLeft, talonKickerRight);
        driver = new RobotDrive(talonFrontLeft, talonBackLeft, talonFrontRight, talonBackRight);

        kickerEncoderLeft = new Encoder(KICKER_LEFT_ENCODER_PORT_A, KICKER_LEFT_ENCODER_PORT_B);
//        kickerEncoderRight = new Encoder(KICKER_RIGHT_ENCODER_PORT_A, KICKER_RIGHT_ENCODER_PORT_B);
        lifterEncoder = new Encoder(LIFTER_ENCODER_PORT_A, LIFTER_ENCODER_PORT_B);
        rightDriveEncoder = new Encoder(RIGHT_DRIVE_ENCODER_PORT_A, RIGHT_DRIVE_ENCODER_PORT_B);
        leftDriveEncoder = new Encoder(LEFT_DRIVE_ENCODER_PORT_A, LEFT_DRIVE_ENCODER_PORT_B);

        if (joyOperator.getThrottle() >= 0) {
            ally = DriverStation.Alliance.kBlue;
        } else {
            ally = DriverStation.Alliance.kRed;
        }
        SmartDashboard.putString("Alliance", ally.name);
    }

    public void robotInit() { //use this method for setup of any kind
        driver.setSafetyEnabled(false);
        //removed camera initialization thread because it hung
        SmartDashboard.putBoolean("Camera Initialized", false);
        System.out.println("Attempting to initialize Camera.");
        double time = RobotVision.initializeCamera();
        System.out.println("Initialization completed in " + time + " seconds");
        SmartDashboard.putBoolean("Camera Initialized", (time < 0));

        driver.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        driver.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        driver.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
        driver.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);

        kickerEncoderLeft.start();
//        kickerEncoderRight.start();
        lifterEncoder.start();
        rightDriveEncoder.start();
        leftDriveEncoder.start();

        //comment compressor  out if you are not using it
        compress.start();

        SmartDashboard.putString("Version Number", VERSION_NUMBER);

        cameraLeftRightServo.set(1);
        cameraUpDownServo.set(1);
    }

    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    public void autonomous() {
        //This may cause problems becase we try to move before shifted
//        shiftingSolenoid.set(DoubleSolenoid.Value.kReverse);
        //Responsibility of the pit crew to set the shifter position(gear) as per Mr. Weissman 2/27
        // I know that this delay is short, but we shouldn't get in the practice (object of preposition -> noun -> practice)
        // of delaying
        // the main thread without it being able to escape when it is no longer enabled or
        // in autonomous
        //Timer.delay(0.1);
//        Timer d = new Timer();
//        d.start();
//        while (d.get() <= .1 && isAutonomous() && isEnabled());

        // do we need this any more?
        //isAutonomous = true;
        lcd.println(DriverStationLCD.Line.kUser1, 1, "autonomous v" + VERSION_NUMBER);
        lcd.updateLCD();
        //Sets the camera to look at the target. I think these values are still incorect
        cameraLeftRightServo.set(SmartDashboard.getNumber("Left Right Camera", 1));
        cameraUpDownServo.set(SmartDashboard.getNumber("Up Down Camera", 1));
        //Kill the watchdog
        driver.setSafetyEnabled(false);

        resetEverything();

        Timer autonomousTimer = new Timer();
        autonomousTimer.start();

        boolean takingPhoto = true;

        RobotVision.ResultReport result = null;

        // create a Runnable so that the objects will be accessible accross many threads
        System.out.println("Starting image capture thread");
        Threads.ImageCaptureRunnable icr = new Threads.ImageCaptureRunnable();
        Thread t = new Thread(icr);
        t.start();

        //Lower Lifter
        System.out.println("Lowering lifter");

        double lowerStartTime = autonomousTimer.get();
        
        // lower the loader until it hits the ground or runs out of time
        while ((autonomousTimer.get() - lowerStartTime) < lowerAbortTime
                && !BallLifter.moveDown()
                && isAutonomous()
                && isEnabled());

        //Drive Forward
        System.out.println("Driving forward");
        Timer driveForwardTimer = new Timer();
        driveForwardTimer.start();

        while ((driveForwardTimer.get() <= autonDriveTime)
                && isAutonomous()
                && isEnabled()) {
            //Turn value needs to be tuned: 0 is too little and 0.05 is too much
            FRC2014.driver.drive(-1.0, 0.01);
        }
        FRC2014.driver.drive(0.0, 0.0);

        //Load the kicker before running image processing
        System.out.println("Loading kicker");

        double kickerStartTime = autonomousTimer.get();

        // kick or wait a fixed time if something is broken
        while ((autonomousTimer.get() - kickerStartTime) < kickerAbortTime
                && !Kicker.load()
                && isAutonomous()
                && isEnabled());

        //Run this while loop until a hot target is found or 5 seconds has passed
        while (autonomousTimer.get() <= takingPhotoTime
                && takingPhoto
                && isAutonomous()
                && isEnabled()) {

            result = icr.getResult();
            if (result == null || t.isAlive()) {
                System.out.println("Result Null or thread still running");
                //Image Processing isn't done yet, so give it time to complete
                lcd.println(DriverStationLCD.Line.kUser2, 1, "Image Processing Still Running          ");
                lcd.updateLCD();
            } else {
                System.out.println("Result not Null.  Processing...");
                //Print out information about vision processing
                lcd.println(DriverStationLCD.Line.kUser2, 1, "target is " + result.targetExists);
                lcd.println(DriverStationLCD.Line.kUser3, 1, "hot : " + result.isHot);
                lcd.println(DriverStationLCD.Line.kUser4, 1, "distance is " + result.distance);
                lcd.updateLCD();

                SmartDashboard.putBoolean("Target", result.targetExists);
                SmartDashboard.putBoolean("Hot", result.isHot);
                SmartDashboard.putNumber("Distance", result.distance);

                if (result.isHot) {
                    System.out.println("Is hot");
                    takingPhoto = false;
                    // save the picture, but if it is less than 2 seconds from Teleop, don't since we aren't threading it.
                    if (autonomousTimer.get() < 9) {
                        System.out.println("Taking picture");
                        RobotVision.takePicture();
                        System.out.println("Picture taken");
                    }
                }
                // Should we be taking multiple pictures?
                // Even though we probably don't, the first picture might be a false-negative.
                if (!t.isAlive() && takingPhoto) {
                    System.out.println("Starting new Image Capture Thread.");
                    t = new Thread(icr);
                    t.start();
                }
            }
            
            System.out.println("Kicking");
            // We don't need a timer here since it is at the end and it exits when we leave autonomous
            while (!Kicker.kick() && isAutonomous() && isEnabled());
        }

        //Kick because a hot target has been found or 5 seconds have passed
    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        lcd.println(DriverStationLCD.Line.kUser1, 1, "teleoperated v" + VERSION_NUMBER);
        lcd.updateLCD();
        boolean oldPictureValue = false;
        double upDownServoValue = 0.5, leftRightServoValue = 0.5;
        driver.setSafetyEnabled(true);

        resetEverything();

        kickerDirection = KICKER_NOT_MOVING;
        lifterDirection = LIFTER_NOT_MOVING;

        while (isOperatorControl() && isEnabled()) {
//            lcd.println(DriverStationLCD.Line.kUser2, 1, "" + driveMode);
//            lcd.println(DriverStationLCD.Line.kUser4, 1, "" + joyLeft.getZ());
            lcd.println(DriverStationLCD.Line.kUser5, 1, "" + joyOperator.getThrottle());
            lcd.updateLCD();
//
            System.out.println("Kicker Encoder 1 " + kickerEncoderLeft.get());
//            System.out.println("Kicker Encoder 2 " + kickerEncoderRight.get());
            System.out.println("Lifter Encoder " + lifterEncoder.get());
            System.out.println("Left Drive Encoder " + leftDriveEncoder.get());
            System.out.println("Right Drive Encoder " + rightDriveEncoder.get());
            System.out.println("Lifter Optical     " + lifterOpticalSensor.get());
//            System.out.println("Kicker Optical     " + kickerOpticalSensor.get());

            // <editor-fold defaultstate="collapsed" desc="Drive Toggler">
            if (joyLeft.getRawButton(JOYSTICK_ARCADEMODE_BUTTON)
                    || joyRight.getRawButton(JOYSTICK_ARCADEMODE_BUTTON)) {
                driveMode = 0;
            }
            if (joyLeft.getRawButton(JOYSTICK_TANKMODE_BUTTON)
                    || joyRight.getRawButton(JOYSTICK_TANKMODE_BUTTON)) {
                driveMode = 1;
            }
            if (joyLeft.getRawButton(JOYSTICK_SPLITARCADEMODE_BUTTON)
                    || joyRight.getRawButton(JOYSTICK_SPLITARCADEMODE_BUTTON)) {
                driveMode = -1;
            }

            if (driveMode > 0) {
                driver.tankDrive(joyLeft, joyRight);
                lcd.println(DriverStationLCD.Line.kUser2, 1, "Tank Drive    ");
                lcd.updateLCD();
                SmartDashboard.putString("Drive Mode:", "Tank Drive");
            } else if (driveMode == 0) {
                driver.arcadeDrive(joyLeft);
                lcd.println(DriverStationLCD.Line.kUser2, 1, "Arcade Drive");
                lcd.updateLCD();
                SmartDashboard.putString("Drive Mode:", "Arcade Drive");
            } else { //if not + or 0 then negative
                driver.drive(joyLeft.getY(), joyRight.getX());
                lcd.println(DriverStationLCD.Line.kUser2, 1, "Split Drive");
                lcd.updateLCD();
                SmartDashboard.putString("Drive Mode:", "Split Drive");
            }

            //</editor-fold> 
            //<editor-fold defaultstate="collapsed" desc="Solenoid Shifter">
            if (joyLeft.getRawButton(JOYSTICK_HIGH_SHIFT_BUTTON) || joyRight.getRawButton(JOYSTICK_HIGH_SHIFT_BUTTON)) {
                shiftingSolenoid.set(DoubleSolenoid.Value.kForward);
                SmartDashboard.putString("Shifter Solenoid", "High Gear");
            } else if (joyLeft.getRawButton(JOYSTICK_LOW_SHIFT_BUTTON) || joyRight.getRawButton(JOYSTICK_LOW_SHIFT_BUTTON)) {
                shiftingSolenoid.set(DoubleSolenoid.Value.kReverse);
                SmartDashboard.putString("Shifter Solenoid", "Low Gear");
            }

            //</editor-fold> 
            // <editor-fold defaultstate="collapsed" desc="Camera Mover">
            double axis5 = joyOperator.getRawAxis(5);
            double axis6 = joyOperator.getRawAxis(6);

            lcd.println(DriverStationLCD.Line.kUser3, 1, "5 is " + axis5);
            lcd.println(DriverStationLCD.Line.kUser4, 1, "6 is " + axis6);
            lcd.updateLCD();

            SmartDashboard.putNumber("Axis 5", axis5);
            SmartDashboard.putNumber("Axis 6", axis6);

            if (axis5 == 1.0) {
                leftRightServoValue += CAMERA_SERVO_SPEED;
            } else if (axis5 == -1.0) {
                leftRightServoValue -= CAMERA_SERVO_SPEED;
            }
            if (axis6 == -1.0) {
                upDownServoValue += CAMERA_SERVO_SPEED;
            } else if (axis6 == 1.0) {
                upDownServoValue -= CAMERA_SERVO_SPEED;
            }

            if (leftRightServoValue > 1) {
                leftRightServoValue = 1;
            } else if (leftRightServoValue < 0) {
                leftRightServoValue = 0;
            }
            if (upDownServoValue > 1) {
                upDownServoValue = 1;
            } else if (upDownServoValue < 0) {
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
            //</editor-fold> 
            //<editor-fold desc="Lifter" defaultstate="collapsed">
            if (joyOperator.getRawButton(JOYSTICK_LIFTER_UP_BUTTON)) {
                lifterDirection = LIFTER_GOING_UP;
            }
            if (joyOperator.getRawButton(JOYSTICK_LIFTER_DOWN_BUTTON)) {
                lifterDirection = LIFTER_GOING_DOWN;
            }

            if (lifterDirection == LIFTER_GOING_UP) {
                lcd.println(DriverStationLCD.Line.kUser3, 1, "up" + "                        ");
                if (BallLifter.moveUp()) { //if moving up is finished, stop
                    lifterDirection = LIFTER_NOT_MOVING;
                    lcd.println(DriverStationLCD.Line.kUser3, 1, "none" + "                        ");
                }
            } else if (lifterDirection == LIFTER_GOING_DOWN) {
                lcd.println(DriverStationLCD.Line.kUser3, 1, "down" + "                        ");
                if (BallLifter.moveDown()) { //if moving down is finished, stop
                    lcd.println(DriverStationLCD.Line.kUser3, 1, "none" + "                        ");
                    lifterDirection = LIFTER_NOT_MOVING;
                }
            } else {
                boolean isMaintaining = !BallLifter.maintainMotors();
                SmartDashboard.putBoolean("Lifter maintaining", isMaintaining);
            }

            newLiftOptState = lifterOpticalSensor.get();
            if (oldLiftOptState != newLiftOptState) {
                BallLifter.resetEncoders();
            }
            oldLiftOptState = newLiftOptState;
            //</editor-fold>
            //<editor-fold desc="Kicker" defaultstate="collapsed">
            /*if (joyOperator.getRawButton(JOYSTICK_LOAD_BUTTON)) {
             kickerDirection = KICKER_LOADING;
             } else if (joyOperator.getRawButton(JOYSTICK_FIRE_BUTTON)) {
             lifterDirection = KICKER_KICKING;
             }*/
            if (joyOperator.getRawButton(JOYSTICK_LOAD_BUTTON)) {
                kickerDirection = KICKER_LOADING;
            } else if (joyOperator.getRawButton(JOYSTICK_FIRE_BUTTON)) {
                if (Kicker.isLoaded) {
                    kickerDirection = KICKER_KICKING;
                }
            } else if (joyOperator.getRawButton(JOYSTICK_PASS_BUTTON)) {
                if (!Kicker.isLoaded) {
                    kickerDirection = KICKER_PASSING;
                }
            } else if (joyOperator.getRawButton(JOYSTICK_UNLOAD_BUTTON)) {
                Kicker.unload();
                kickerDirection = KICKER_UNLOADING;
            }

            if (kickerDirection == KICKER_LOADING) {
                if (Kicker.load()) {
                    Kicker.stop();
                    kickerDirection = KICKER_NOT_MOVING;
                }

                newKickOptState = kickerOpticalSensor.get();
                if (oldKickOptState && !newKickOptState) {
                    Kicker.resetEncoders();
                }
                oldKickOptState = newKickOptState;
            } else if (kickerDirection == KICKER_KICKING) {
                if (Kicker.kick()) {
                    Kicker.stop();
                    kickerDirection = KICKER_NOT_MOVING;
                }
            } else if (kickerDirection == KICKER_PASSING) {
                if (Kicker.pass()) {
                    Kicker.stop();
                    kickerDirection = KICKER_NOT_MOVING;
                }
            } else if (kickerDirection == KICKER_UNLOADING) {
                if (Kicker.unload()) {
                    Kicker.stop();
                    kickerDirection = KICKER_NOT_MOVING;
                }
            } else {
                SmartDashboard.putBoolean("Maintaining Kicker", Kicker.maintainKicker());
            }

            if (kickerEncoderLeft.getRate() > 0) { //it is kicking
                newKickOptState = kickerOpticalSensor.get();
                if (!oldKickOptState && newKickOptState) {
                    Kicker.resetEncoders();
                }
                oldKickOptState = newKickOptState;
            } else if (kickerEncoderLeft.getRate() < 0) { //it is loading
                newKickOptState = kickerOpticalSensor.get();
                if (oldKickOptState && !newKickOptState) {
                    Kicker.resetEncoders();
                }
                oldKickOptState = newKickOptState;
            }

            if (joyOperator.getRawButton(JOYSTICK_RESET_ENCODERS_BUTTON)) {
                Kicker.resetEncoders();
            }
            //</editor-fold> 
            //<editor-fold desc="Auto Lift and Manual" defaultstate="collapsed">
            if (joyOperator.getRawButton(JOYSTICK_AUTO_LIFT_BUTTON)) {
                double delay = 0.04330708661417321;
                System.out.println("delay: " + delay);
                if (!lifterOpticalSensor.get()) { //if we see a ball, pick up
                    if (ally.value == DriverStation.Alliance.kBlue_val) {
                        lifterDirection = LIFTER_GOING_UP;
                    } else if (ally.value == DriverStation.Alliance.kRed_val) {
                        if (!timerStarted) {
                            redAutoLiftTimer.start();
                            timerStarted = true;
                        }
                    }
                }
                if (timerStarted && redAutoLiftTimer.get() >= delay) {
                    redAutoLiftTimer.stop();
                    lifterDirection = LIFTER_GOING_UP;
                    timerStarted = false;
                }
                if (lifterLimitSwitch.get() == false) {
                    kickerDirection = KICKER_LOADING;
                }
            } else {
                redAutoLiftTimer.stop();
                timerStarted = false;
            }

            newManualState = joyOperator.getRawButton(JOYSTICK_MANUAL_BUTTON);
            if (newManualState == true) {
                double yAxis = joyOperator.getY();
                talonKickerLeft.set(yAxis);
                talonKickerRight.set(-1 * yAxis);
            } else if (newManualState == false && oldManualState == true) {
                Kicker.stop();
            }
            oldManualState = newManualState;
            //</editor-fold> 
            SmartDashboard.putBoolean("Ball loaded:", !lifterOpticalSensor.get());
            SmartDashboard.putBoolean("Fork down:", !BallLifter.isUp);
        }
    }

    /**
     * This function is called once each time the robot enters test mode.
     */
    public void test() {
        lcd.println(DriverStationLCD.Line.kUser1, 1, "test v" + VERSION_NUMBER);
        lcd.updateLCD();
        //talonKickerLeft,//0
        //talonBackLeft, //1
        //talonFrontLeft, //2
        //talonKickerRight, //3
        //talonBackRight,//4
        //talonFrontRight, //5
        //talonLoader, //6
        //talonBackup //7

        resetEverything();

        while (isTest() && isEnabled()) {
            RobotMotorTester.motorTest();
            RobotMotorTester.sensorTest();
        }
    }

    public void resetEverything() {
        kickerEncoderLeft.reset();
//        kickerEncoderRight.reset();
        lifterEncoder.reset();
        leftDriveEncoder.reset();
        rightDriveEncoder.reset();
        BallLifter.isCalibrated = false;
        BallLifter.isUp = true;
        BallLifter.isDown = false;
        Kicker.isLoaded = false;
        newLiftOptState = lifterLimitSwitch.get();
        oldLiftOptState = newLiftOptState;
        newKickOptState = kickerOpticalSensor.get();
        oldKickOptState = newKickOptState;
        if (joyOperator.getThrottle() >= 0) {
            ally = DriverStation.Alliance.kBlue;
        } else {
            ally = DriverStation.Alliance.kRed;
        }
        SmartDashboard.putString("Alliance", ally.name);
        timerStarted = false;
    }
}
