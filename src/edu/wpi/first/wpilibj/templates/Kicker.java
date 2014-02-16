package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;

/**
 *
 * @author Administrator
 */
public class Kicker {

    static final double MOTOR_SPEED = 0.2;

    static private Talon kickerLeftMotor;
    static private Talon kickerRightMotor;
    static private Encoder kickerLeftEncoder;
    static private Encoder kickerRightEncoder;
    static private Joystick joyOperator;
    static private Joystick joyRight;
    static private DigitalInput kickerOptSensor;

    protected static boolean isLoaded = false;

    static private DriverStationLCD lcd;

    static boolean oldKickOptState = true;
    static boolean newKickOptState = oldKickOptState;

    static { //analogous to constructor
        lcd = DriverStationLCD.getInstance();
        joyOperator = new Joystick(FRC2014.JOYSTICK_OPERATOR_USB);
        joyRight = new Joystick(FRC2014.JOYSTICK_RIGHT_USB);
        kickerLeftEncoder = FRC2014.kickerEncoder1;
        kickerRightEncoder = FRC2014.kickerEncoder2;
        kickerLeftMotor = FRC2014.talonKickerLeft;
        kickerRightMotor = FRC2014.talonKickerRight;
        kickerOptSensor = FRC2014.kickerOpticalSensor;

    }

    public static boolean load() {
        if (kickerLeftEncoder.get() <= FRC2014.KICKER_ENCODER_TOP_POSITION) {
            lcd.println(DriverStationLCD.Line.kUser6, 1, "finished moving                             ");
            lcd.updateLCD();
            kickerLeftMotor.set(0);
            kickerRightMotor.set(0);
            isLoaded = true;
            return true;
        }
        newKickOptState = kickerOptSensor.get();
        if (oldKickOptState && !newKickOptState) {
            resetEncoders();
        }
        oldKickOptState = newKickOptState;
        //double throttle = joyOperator.getThrottle();
        //throttle = (throttle/2.0)+0.5;
        //throttle = (throttle/-2.0)+0.5; //down == 0, up == 1
        double throttle = FRC2014.COCKING_SPEED;
        kickerLeftMotor.set(throttle);
        kickerRightMotor.set(-1.0 * throttle);
        lcd.println(DriverStationLCD.Line.kUser6, 1, "loading                                       ");
        lcd.updateLCD();
        return false;
    }

    public static boolean kick() {
        if (kickerLeftEncoder.get() >= FRC2014.KICKER_ENCODER_KICK_POSITION) {
            lcd.println(DriverStationLCD.Line.kUser6, 1, "finished moving                             ");
            lcd.updateLCD();
            kickerLeftMotor.set(0);
            kickerRightMotor.set(0);
            isLoaded = false;
            return true;
        }
        //double throttle = joyOperator.getZ();
        //throttle = (throttle/2.0)+0.5;
        //throttle = (throttle/-2.0)+0.5;  // down == 0, up == 1
        double throttle = 1.0;
        kickerLeftMotor.set(-1.0 * throttle);
        kickerRightMotor.set(throttle);
        lcd.println(DriverStationLCD.Line.kUser6, 1, "kicking                                       ");
        lcd.updateLCD();
        return false;
    }

    public static void resetEncoders() {
        kickerLeftEncoder.reset();
        kickerRightEncoder.reset();
    }

    public static void stop() {
        lcd.println(DriverStationLCD.Line.kUser6, 1, "stopping                                       ");
        lcd.updateLCD();
        kickerLeftMotor.set(0);
        kickerRightMotor.set(0);
    }
}
