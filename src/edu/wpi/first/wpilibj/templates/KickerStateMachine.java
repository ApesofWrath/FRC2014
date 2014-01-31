package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Talon;

/*
 States:
 init
 motor on
 motor moving up
 motor off
 wait
 kick
 back drive
 encoder reset
 reset: not called every time
 */

/*
 Port Assignments:
 */
public class KickerStateMachine {

    // <editor-fold defaultstate="collapsed" desc="Variable Definitions">
    private double kP = .1, kI = .001, kD = 0, sampleRate = 0.85;

    public final int INIT = 0, MOTOR_ON = 1, MOTOR_MOVING_UP = 2, MOTOR_OFF = 3, WAIT = 4, KICK = 5, BACK_DRIVE = 6, ENCODER_RESET = 7, RESET = 8;
    private int state = INIT;
    private Joystick joyOperator;
    private Encoder kickerEncoder;
    private DigitalInput kickerOpticalSensor;
    //public Talon kickerMotor;
    private Talon kickerMotor;
    private PIDController pid;
    private DriverStationLCD lcd;
    private DriverStation ds;

    // </editor-fold>
    public KickerStateMachine() {
        joyOperator = new Joystick(FRC2014.JOYSTICK_OPERATOR_USB);
        kickerEncoder = new Encoder(FRC2014.KICKER_ENCODER_PORT_A, FRC2014.KICKER_ENCODER_PORT_B);
        kickerOpticalSensor = new DigitalInput(FRC2014.KICKER_OPTICAL_SENSOR_PORT);
        kickerEncoder.start();
        kickerMotor = new Talon(FRC2014.MOTOR_KICK_PWM);
        ds = DriverStation.getInstance();
        lcd = DriverStationLCD.getInstance();
        pid = new PIDController(kP, kI, kD, kickerEncoder, kickerMotor, sampleRate);
        kickerEncoder.setPIDSourceParameter(PIDSource.PIDSourceParameter.kDistance);
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setSampleRate() {
        sampleRate = ds.getAnalogIn(4);
        pid.disable();
        pid = new PIDController(kP, kI, kD, kickerEncoder, kickerMotor, sampleRate);
        pid.enable();
    }

    public void setSetpoint(int setpoint) {
        kP = ds.getAnalogIn(1);
        kI = ds.getAnalogIn(2);
        kD = ds.getAnalogIn(3);
        pid.setPID(kP, kI, kD);
        pid.setSetpoint(setpoint);
        lcd.println(DriverStationLCD.Line.kUser6, 1, "Set=" + pid.getSetpoint() + "            ");
        lcd.updateLCD();
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
        if (joyOperator.getRawButton(FRC2014.JOYSTICK_RESET_BUTTON)) {
            state = RESET;
        }
        switch (state) {
            case INIT:
                if (joyOperator.getRawButton(FRC2014.JOYSTICK_LOAD_BUTTON)) {
                    state = MOTOR_ON;
                }
                if (joyOperator.getRawButton(FRC2014.SET_SAMPLE_RATE_BUTTON)) {
                    setSampleRate();

                }
                break;

            case MOTOR_ON:
                //kickerMotor.set();
                //add motor encoder safety
                state = MOTOR_MOVING_UP;
                break;

            case MOTOR_MOVING_UP:
                //   motorSpeed = ((joyOperator.getZ() + 1) / 2);
                pid.enable();
                setSetpoint(FRC2014.KICKER_ENCODER_TOP_POSITION);
                lcd.println(DriverStationLCD.Line.kUser5, 1, "" + kickerMotor.get() + "   ");
                lcd.updateLCD();
                if (kickerEncoder.get() >= FRC2014.KICKER_ENCODER_TOP_POSITION) {
                    state = MOTOR_OFF;
                }
                break;

            case MOTOR_OFF:
                kickerMotor.set(0);
                pid.disable();
                state = WAIT;
                break;

            case WAIT:
                if (joyOperator.getRawButton(FRC2014.JOYSTICK_FIRE_BUTTON)) {
                    state = KICK;
                }
                break;

            case KICK:
                //  motorSpeed = -((joyOperator.getZ() + 1) / 2);
                pid.enable();
                setSetpoint(FRC2014.KICKER_ENCODER_KICK_POSITION);
                lcd.println(DriverStationLCD.Line.kUser5, 1, "" + kickerMotor.get() + "   ");
                lcd.updateLCD();

                // kickerMotor.set(motorSpeed); //kicking is positive, flip value
                if (kickerEncoder.get() <= FRC2014.KICKER_ENCODER_KICK_POSITION) {
                    state = BACK_DRIVE;
                }
                break;

            case BACK_DRIVE:
                // motorSpeed = ((joyOperator.getZ() + 1) / 2);
                setSetpoint(FRC2014.KICKER_ENCODER_REST_POSITION);
                lcd.println(DriverStationLCD.Line.kUser5, 1, "" + kickerMotor.get() + "   ");
                lcd.updateLCD();
                //kickerMotor.set(motorSpeed); //kicking is positive, flip value
                if (kickerEncoder.get() >= FRC2014.KICKER_ENCODER_REST_POSITION) {
                    state = ENCODER_RESET;
                }
                break;

            case ENCODER_RESET:
                if (!kickerOpticalSensor.get()) {
                    pid.disable();
                    kickerMotor.set(0);
                    kickerEncoder.reset();
                    state = INIT;
                }
                break;

            case RESET:
                pid.disable();
                kickerEncoder.reset();
                state = INIT;
                break;

            default:
                System.out.println("error");
                state = INIT;
                break;
        }
        // </editor-fold>
    }

}
