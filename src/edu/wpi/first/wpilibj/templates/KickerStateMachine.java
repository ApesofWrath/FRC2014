package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;

/*
 States:
  idle - not firing currently
  pickUpBall - moves ball lifter down for pickup
  moveArmUp - moves ball lifter up after it has picked up a ball
  kickerUp - moves kicker up after arm (with ball) is up
  fullyLoaded - everything is loaded
  moveArmDown - moves arm down before kicking
  kick - kicking ball
 */

/*
 Port Assignments:
 */
public class KickerStateMachine {
    // <editor-fold defaultstate="collapsed" desc="Variable Definitions">

    public final int IDLE = 0, PICK_UP_BALL = 1, MOVE_ARM_UP = 2, KICKER_UP = 3, FULLY_LOADED = 4, MOVE_ARM_DOWN = 5, KICK = 6;
    private int state = IDLE;
    private Joystick joyOperator;
    private Encoder kickerEncoder;
    private DigitalInput kickerOpticalSensor;
    private Talon kickerMotor1;
    private Talon kickerMotor2;
    private DriverStationLCD lcd;
    private DriverStation ds;

    // </editor-fold>
    public KickerStateMachine(Talon motor1, Talon motor2) {
        joyOperator = new Joystick(FRC2014.JOYSTICK_OPERATOR_USB);
//        kickerEncoder = new Encoder(FRC2014.KICKER_ENCODER1_PORT_A, FRC2014.KICKER_ENCODER_PORT_B);
        kickerOpticalSensor = new DigitalInput(FRC2014.KICKER_OPTICAL_SENSOR_PORT);
        kickerEncoder.start();
        kickerMotor1 = motor1;
        kickerMotor2 = motor2;  
        ds = DriverStation.getInstance();
        lcd = DriverStationLCD.getInstance();
        state = IDLE;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
    
    public void stateMachine() {
        // <editor-fold defaultstate="collapsed" desc="State Machine">
        //double motorSpeed;

        lcd.println(DriverStationLCD.Line.kUser2, 1, "" + kickerEncoder.get() + "   ");
        lcd.updateLCD();
        lcd.println(DriverStationLCD.Line.kUser3, 1, "" + state);
        lcd.updateLCD();
        lcd.println(DriverStationLCD.Line.kUser4, 1, "" + kickerOpticalSensor.get() + "   ");
        lcd.updateLCD();
        lcd.println(DriverStationLCD.Line.kUser5, 1, ("" + kickerMotor.get() + "     ").substring(0, 5));
        lcd.updateLCD();

        switch (state) {
            case IDLE:
                if (joyOperator.getRawButton(FRC2014.JOYSTICK_LOAD_BUTTON)) {
                    state = MOTOR_ON;
                }
                break;

            case PICK_UP_BALL:
                kickerMotor.set();
                state = MOTOR_MOVING_UP;
                break;

            case MOVE_ARM_UP:
                //   motorSpeed = ((joyOperator.getZ() + 1) / 2);
                if (kickerEncoder.get() >= FRC2014.KICKER_ENCODER_TOP_POSITION) {
                    state = MOTOR_OFF;
                }
                break;

            case KICKER_UP:
                //kickerMotor.set(0);
                state = WAIT;
                break;

            case FULLY_LOADED:
                if (joyOperator.getRawButton(FRC2014.JOYSTICK_FIRE_BUTTON)) {
                    state = KICK;
                }
                break;

            case MOVE_ARM_DOWN:
                //  motorSpeed = -((joyOperator.getZ() + 1) / 2);
                // kickerMotor.set(motorSpeed); //kicking is positive, flip value
                if (kickerEncoder.get() <= FRC2014.KICKER_ENCODER_KICK_POSITION) {
                    state = BACK_DRIVE;
                }
                break;

            case KICK:
                // motorSpeed = ((joyOperator.getZ() + 1) / 2);
                //kickerMotor.set(motorSpeed); //kicking is positive, flip value
                if (kickerEncoder.get() >= FRC2014.KICKER_ENCODER_REST_POSITION) {
                    state = ENCODER_RESET;
                }
                break;

            default:
                System.out.println("error");
                state = INIT;
                break;
        }
        // </editor-fold>
    }

}
