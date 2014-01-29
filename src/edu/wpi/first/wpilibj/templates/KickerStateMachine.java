package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;

/*
states:
init
motor on
motor moving up
motor off
wait
kick
back drive
encoder reset
*/

public class KickerStateMachine {
    
    // <editor-fold defaultstate="collapsed" desc="Variable Definitions">
    
    public final int INIT = 0, MOTOR_ON = 1, MOTOR_MOVING_UP = 2, MOTOR_OFF = 3, WAIT = 4, KICK = 5, BACK_DRIVE = 6, ENCODER_RESET = 7;
    private int state = INIT;
    private Joystick joyOperator;
    private Encoder kickerEncoder;
    private DigitalInput kickerOpticalSensor;
    
    private DriverStationLCD lcd;
    
    // </editor-fold>
    
    public KickerStateMachine() {
        joyOperator = new Joystick(FRC2014.JOYSTICK_OPERATOR_USB);
        kickerEncoder = new Encoder(FRC2014.KICKER_ENCODER_PORT_A, FRC2014.KICKER_ENCODER_PORT_B);
        kickerOpticalSensor = new DigitalInput(FRC2014.KICKER_OPTICAL_SENSOR_PORT);
        kickerEncoder.start();
        lcd = DriverStationLCD.getInstance();
    }
    
    public int getState() {
        return state;
    }
    
    public void setState(int state) {
        this.state = state;
    }
    
    public void stateMachine() {
        lcd.println(DriverStationLCD.Line.kUser2, 1, ""+kickerEncoder.get());
        lcd.updateLCD();
        lcd.println(DriverStationLCD.Line.kUser3, 1, ""+state);
        lcd.updateLCD();
        lcd.println(DriverStationLCD.Line.kUser4, 1, ""+kickerOpticalSensor.get());
        lcd.updateLCD();
        switch (state) {
            case INIT:
                if(joyOperator.getRawButton(FRC2014.JOYSTICK_LOAD_BUTTON)) {
                    state = MOTOR_ON;
                }
                break;
                
            case MOTOR_ON:
                //turn motor on
                //add motor encoder safety
                state = MOTOR_MOVING_UP;
                break;
            
            case MOTOR_MOVING_UP:
                if(kickerEncoder.get() >= FRC2014.KICKER_ENCODER_TOP_POSITION) {
                    state = MOTOR_OFF;
                }
                break;
                
            case MOTOR_OFF:
                //turn motor off
                state = WAIT;
                break;
            
            case WAIT:
                if(joyOperator.getRawButton(FRC2014.JOYSTICK_FIRE_BUTTON)) {
                    state = KICK;
                }
                break;
               
            case KICK:
                //kick position negative
                if(kickerEncoder.get() <= FRC2014.KICKER_ENCODER_KICK_POSITION) {
                    state = BACK_DRIVE;
                }
                break;
               
            case BACK_DRIVE:
                //backdrive motors
                if(kickerEncoder.get() >= FRC2014.KICKER_ENCODER_REST_POSITION) {
                    state = ENCODER_RESET;
                }
                break;
                
            case ENCODER_RESET:
                if(!kickerOpticalSensor.get()) {
                    //turn off motors
                    kickerEncoder.reset();
                    state = INIT;
                }
                break;
               
            default:
                System.out.println("error");
                state = INIT;
                break;
        }
    }
    
}
