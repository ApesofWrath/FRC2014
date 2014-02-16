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

    static private Talon talonKickerLeft;
    static private Talon talonKickerRight;
    static private Encoder kickerEncoderLeft;
    static private Encoder kickerEncoderRight;
    static private Joystick joyOperator;
    static private Joystick joyRight;
    static private DigitalInput kickerOpticalSensor;

    protected static boolean isLoaded = false;

    static private DriverStationLCD lcd;

    static boolean oldKickOptState = true;
    static boolean newKickOptState = oldKickOptState;

    static { //analogous to constructor
        lcd = DriverStationLCD.getInstance();
        joyOperator = new Joystick(FRC2014.JOYSTICK_OPERATOR_USB);
        joyRight = new Joystick(FRC2014.JOYSTICK_RIGHT_USB);
        kickerEncoderLeft = FRC2014.kickerEncoderLeft;
        kickerEncoderRight = FRC2014.kickerEncoderRight;
        talonKickerLeft = FRC2014.talonKickerLeft;
        talonKickerRight = FRC2014.talonKickerRight;
        kickerOpticalSensor = FRC2014.kickerOpticalSensor;

    }

    public static boolean load() {
        if (kickerEncoderLeft.get() <= FRC2014.KICKER_ENCODER_TOP_POSITION) {
            lcd.println(DriverStationLCD.Line.kUser6, 1, "finished moving                             ");
            lcd.updateLCD();
            talonKickerLeft.set(0);
            talonKickerRight.set(0);
            isLoaded = true;
            return true;
        }
        newKickOptState = kickerOpticalSensor.get();
        if (oldKickOptState && !newKickOptState) {
            resetEncoders();
        }
        oldKickOptState = newKickOptState;
        //double throttle = joyOperator.getThrottle();
        //throttle = (throttle/2.0)+0.5;
        //throttle = (throttle/-2.0)+0.5; //down == 0, up == 1
        double throttle = FRC2014.COCKING_SPEED;
        talonKickerLeft.set(throttle);
        talonKickerRight.set(-1.0 * throttle);
        lcd.println(DriverStationLCD.Line.kUser6, 1, "loading                                       ");
        lcd.updateLCD();
        return false;
    }

    public static boolean kick() {
        if (kickerEncoderLeft.get() >= FRC2014.KICKER_ENCODER_KICK_POSITION) {
            lcd.println(DriverStationLCD.Line.kUser6, 1, "finished moving                             ");
            lcd.updateLCD();
            talonKickerLeft.set(0);
            talonKickerRight.set(0);
            isLoaded = false;
            return true;
        }
        //double throttle = joyOperator.getZ();
        //throttle = (throttle/2.0)+0.5;
        //throttle = (throttle/-2.0)+0.5;  // down == 0, up == 1
        double throttle = 1.0;
        talonKickerLeft.set(-1.0 * throttle);
        talonKickerRight.set(throttle);
        lcd.println(DriverStationLCD.Line.kUser6, 1, "kicking                                       ");
        lcd.updateLCD();
        return false;
    }

    public static void resetEncoders() {
        kickerEncoderLeft.reset();
        kickerEncoderRight.reset();
    }

    public static void stop() {
        lcd.println(DriverStationLCD.Line.kUser6, 1, "stopping                                       ");
        lcd.updateLCD();
        talonKickerLeft.set(0);
        talonKickerRight.set(0);
    }
}
