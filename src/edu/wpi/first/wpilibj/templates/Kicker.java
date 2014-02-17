package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
        isLoaded = false;
        if (kickerEncoderLeft.get() <= FRC2014.KICKER_ENCODER_TOP_POSITION && kickerEncoderLeft.get() >= FRC2014.KICKER_ENCODER_ERROR_POSITION) {
            lcd.println(DriverStationLCD.Line.kUser6, 1, "finished moving                             ");
            lcd.updateLCD();
            talonKickerLeft.set(0);
            talonKickerRight.set(0);
            isLoaded = true;
            return true;
        }
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
        isLoaded = false;
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

    public static boolean maintainKicker() {
        if (isLoaded) {
            int kickerEncoderValue = kickerEncoderLeft.get();
            int error = FRC2014.KICKER_ENCODER_TOP_POSITION - kickerEncoderValue;
            double p = (joyOperator.getThrottle() / 4) + 0.25;
            if (error < -15 || error > 15) {
                p = 0;
            }
            double motorPower;
            if (error > -3 || error < 3) {
                motorPower = 0;
            } else {
                motorPower = p * error;
            }
            SmartDashboard.putNumber("Kicker P", p);
            talonKickerLeft.set(motorPower);
            talonKickerRight.set(-1 * motorPower);
            if (error <= 1 && error >= -1) {
                return true;
            } else {
                return false;
            }
        }
        return true;
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
