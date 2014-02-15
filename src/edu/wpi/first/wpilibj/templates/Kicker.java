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

    static private DriverStationLCD lcd;

    static { //analogous to constructor
        lcd = DriverStationLCD.getInstance();
        joyOperator = new Joystick(FRC2014.JOYSTICK_OPERATOR_USB);
        joyRight = new Joystick(FRC2014.JOYSTICK_RIGHT_USB);
        kickerLeftEncoder = FRC2014.kickerEncoder1;
        kickerRightEncoder = FRC2014.kickerEncoder2;
        kickerLeftMotor = FRC2014.talonKickerLeft;
        kickerRightMotor = FRC2014.talonKickerRight;
    }

    public static boolean load() {
        if (kickerLeftEncoder.get() <= FRC2014.KICKER_ENCODER_TOP_POSITION) {
            lcd.println(DriverStationLCD.Line.kUser5, 1, "finished moving                             ");
            lcd.updateLCD();
            kickerLeftMotor.set(0);
            kickerRightMotor.set(0);
            return true;
        }
        kickerLeftMotor.set(joyOperator.getThrottle()/2 + .5);
        kickerRightMotor.set(-1 * joyOperator.getThrottle()/2 + .5);
        lcd.println(DriverStationLCD.Line.kUser5, 1, "moving                                       ");
        lcd.updateLCD();
        return false;
    }

    public static boolean kick() {
        if (kickerLeftEncoder.get() >= FRC2014.KICKER_ENCODER_KICK_POSITION) {
            lcd.println(DriverStationLCD.Line.kUser5, 1, "finished moving                             ");
            lcd.updateLCD();
            kickerLeftMotor.set(0);
            kickerRightMotor.set(0);
            return true;
        }
        kickerLeftMotor.set(-1 * joyRight.getZ()/2 + .5);
        kickerRightMotor.set(joyRight.getZ()/2 + .5);
        lcd.println(DriverStationLCD.Line.kUser5, 1, "moving                                       ");
        lcd.updateLCD();
        return false;
    }
    
    public static void resetEncoders() {
        kickerLeftEncoder.reset();
        kickerRightEncoder.reset();
    }

    public static void stop() {
        kickerLeftMotor.set(0);
        kickerRightMotor.set(0);
    }
}
