//package edu.wpi.first.wpilibj.templates;
//
//import edu.wpi.first.wpilibj.DigitalInput;
//import edu.wpi.first.wpilibj.DriverStation;
//import edu.wpi.first.wpilibj.DriverStationLCD;
//import edu.wpi.first.wpilibj.Encoder;
//import edu.wpi.first.wpilibj.Joystick;
//import edu.wpi.first.wpilibj.Talon;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//
///*
// States:
// idle - not firing currently
// pickUpBall - moves ball lifter down for pickup
// moveArmUp - moves ball lifter up after it has picked up a ball
// kickerUp - moves kicker up after arm (with ball) is up
// armed - everything is loaded and ready to shoot
// moveArmDown - moves arm down before kicking
// kick - kicking ball
// */
//
///*
//ATTENTION: AS OF 2/14/14, THIS CODE IS COMPLETELY OBSOLETE. USE KICKER.JAVA INSTEAD
//*/
//
///*
// Port Assignments:
// */
//public class KickerStateMachine {
//    // <editor-fold defaultstate="collapsed" desc="Variable Definitions">
//
//    public final int IDLE = 0, PICK_UP_BALL = 1, MOVE_ARM_UP = 2, KICKER_UP = 3, ARMED = 4, MOVE_ARM_DOWN = 5, KICK = 6;
//    private int state = IDLE;
//    private Joystick joyOperator;
//    private Encoder kickerEncoder1;
//    private Encoder kickerEncoder2;
//    private Encoder lifterEncoder;
//    private DigitalInput kickerOpticalSensor;
//    private DigitalInput checkBallOpticalSensor; //checks if a ball is loaded
//    private Talon kickerMotor1;
//    private Talon kickerMotor2;
//    private Talon lifterMotor;
//    private DriverStationLCD lcd;
//    private DriverStation ds;
//
//    // </editor-fold>
//    public KickerStateMachine(Talon motor1, Talon motor2) {
//        joyOperator = new Joystick(FRC2014.JOYSTICK_OPERATOR_USB);
//        kickerEncoder1 = new Encoder(FRC2014.KICKER_LEFT_ENCODER_PORT_A, FRC2014.KICKER_LEFT_ENCODER_PORT_B);
//        kickerEncoder2 = new Encoder(FRC2014.KICKER_RIGHT_ENCODER_PORT_A, FRC2014.KICKER_RIGHT_ENCODER_PORT_B);
//        lifterEncoder = new Encoder(FRC2014.LIFTER_ENCODER_PORT_A, FRC2014.LIFTER_ENCODER_PORT_B);
//        kickerOpticalSensor = new DigitalInput(FRC2014.KICKER_OPTICAL_SENSOR_PORT);
//        checkBallOpticalSensor = new DigitalInput(FRC2014.KICKER_OPTICAL_SENSOR);
//        kickerEncoder1.start();
//        kickerEncoder2.start();
//        lifterEncoder.start();
//        kickerMotor1 = motor1;
//        kickerMotor2 = motor2;
//        ds = DriverStation.getInstance();
//        lcd = DriverStationLCD.getInstance();
//        state = IDLE;
//    }
//
//    public int getState() {
//        return state;
//    }
//
//    public void setState(int state) {
//        this.state = state;
//    }
//
//    public void stateMachine() {
//        // <editor-fold defaultstate="collapsed" desc="State Machine">
//        SmartDashboard.putNumber("Kicker encoder 1", kickerEncoder1.get());
//        SmartDashboard.putNumber("Kicker encoder 2", kickerEncoder2.get());
//        SmartDashboard.putNumber("Lifter encoder", lifterEncoder.get());
//        SmartDashboard.putBoolean("kicker optical sensor", kickerOpticalSensor.get());
//        SmartDashboard.putBoolean("check ball sensor", checkBallOpticalSensor.get());
//        SmartDashboard.putBoolean("", true);
//        lcd.println(DriverStationLCD.Line.kUser3, 1, "" + state);
//        lcd.updateLCD();
//        lcd.println(DriverStationLCD.Line.kUser4, 1, "" + kickerOpticalSensor.get() + "   ");
//        lcd.updateLCD();
//        lcd.println(DriverStationLCD.Line.kUser5, 1, ("" + kickerMotor1.get() + "     ").substring(0, 5));
//        lcd.updateLCD();
//
//        switch (state) {
//            case IDLE:
//                
//                if (FRC2014.isInManualControl == false) {
//                    if (BallLifter.moveUp() == true) { //if this is true, we are done moving.
//                        state = PICK_UP_BALL;
//                    }
//                } else { //manual control
//                    double joyValue = joyOperator.getY();
//                    if (joyValue>0) {
//                        BallLifter.moveUp(joyValue);
//                    } else if (joyValue<0) {
//                        BallLifter.moveDown(joyValue);
//                    } else {
//                        BallLifter.stopMotor();
//                    }
//                }
//                
//                break;
//
//            case PICK_UP_BALL:
//
//                if (joyOperator.getRawButton(FRC2014.JOYSTICK_LIFTER_UP_DOWN_TOGGLE_BUTTON)) {
//                    state = MOVE_ARM_UP;
//                }
//                break;
//
//            case MOVE_ARM_UP:
//
//                if (BallLifter.moveUp() == true) { //if this is true, we are done moving.
//                    state = KICKER_UP;
//                }
//                break;
//
//            case KICKER_UP:
//                if (checkBallOpticalSensor.get() == true) { //if the light beam is interrupted, it returns true
//                    if (kickerEncoder1.get() >= FRC2014.KICKER_ENCODER_TOP_POSITION) {
//                        kickerMotor1.set(0);
//                        kickerMotor2.set(0);
//                        state = ARMED;
//                    } else {
//                        kickerMotor1.set(joyOperator.getAxis(Joystick.AxisType.kThrottle));
//                        kickerMotor2.set(-1 * joyOperator.getAxis(Joystick.AxisType.kThrottle));
//                    }
//                } else {
//                    state = IDLE;
//                }
//                break;
//
//            case ARMED:
//
//                if (joyOperator.getRawButton(FRC2014.JOYSTICK_FIRE_BUTTON)) {
//                    state = MOVE_ARM_DOWN;
//                }
//                break;
//
//            case MOVE_ARM_DOWN:
//
//                if (BallLifter.moveDown()) { //returns true if finished moving
//                    state = KICK;
//                }
//                break;
//
//            case KICK:
//
//                if (kickerEncoder1.get() <= FRC2014.KICKER_ENCODER_KICK_POSITION) {
//                    kickerMotor1.set(0);
//                    kickerMotor2.set(0);
//                    state = IDLE;
//                } else {
//                    kickerMotor1.set(-1 * joyOperator.getAxis(Joystick.AxisType.kThrottle));
//                    kickerMotor2.set(joyOperator.getAxis(Joystick.AxisType.kThrottle));
//                }
//                break;
//
//            default:
//                System.out.println("error");
//                state = IDLE;
//                break;
//        }
//        // </editor-fold>
//    }
//
//}
