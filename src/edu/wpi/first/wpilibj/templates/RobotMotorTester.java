/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotMotorTester {

	private static DriverStationLCD lcd;
	private static Talon talonFrontLeft, talonFrontRight, talonBackLeft, talonBackRight,
			talonKickerLeft, talonKickerRight, talonLoader, talonBackup;
	private static DigitalInput lifterOpticalSensor, kickerOpticalSensor;
	private static Encoder lifterEncoder, kickerEncoder1, kickerEncoder2, leftDriveEncoder;
	private static Joystick joyOperator;

	private static int counter = 0;

	static {
		lcd = DriverStationLCD.getInstance();
		joyOperator = new Joystick(FRC2014.JOYSTICK_OPERATOR_USB);

		talonKickerLeft = FRC2014.talonKickerLeft;
		talonBackLeft = FRC2014.talonBackLeft;
		talonFrontLeft = FRC2014.talonFrontLeft;
		talonKickerRight = FRC2014.talonKickerRight;
		talonBackRight = FRC2014.talonBackRight;
		talonFrontRight = FRC2014.talonFrontRight;
		talonLoader = FRC2014.talonLoader;
		talonBackup = FRC2014.talonBackup;

		kickerEncoder1 = FRC2014.kickerEncoderLeft;
		kickerEncoder2 = FRC2014.kickerEncoderRight;
		lifterEncoder = FRC2014.lifterEncoder;

		kickerOpticalSensor = FRC2014.kickerOpticalSensor;
		lifterOpticalSensor = FRC2014.lifterOpticalSensor;
	}

	public static void motorTest() {
		double zAxis = joyOperator.getZ();

		lcd.println(DriverStationLCD.Line.kUser6, 1, ("" + String.valueOf(zAxis) + "        ").substring(0, 5));
		lcd.updateLCD();
		System.out.println("motor test.  Z:" + zAxis);

		if (joyOperator.getRawButton(1)) {
			talonKickerLeft.set(zAxis);
			// I used Ternary Operators for all of these because the usage is fairly
			// easy to understand and they are short and neat in this instance.
			lcd.println(DriverStationLCD.Line.kUser5, 1, zAxis >= 0 ? "1" : "!");

		} else {
			talonKickerLeft.set(0.0);
			lcd.println(DriverStationLCD.Line.kUser5, 1, " ");
		}

		if (joyOperator.getRawButton(2)) {
			talonBackLeft.set(zAxis);
			lcd.println(DriverStationLCD.Line.kUser5, 2, zAxis >= 0 ? "2" : "@");

		} else {
			talonBackLeft.set(0.0);
			lcd.println(DriverStationLCD.Line.kUser5, 2, " ");
		}

		if (joyOperator.getRawButton(3)) {
			talonFrontLeft.set(zAxis);
			lcd.println(DriverStationLCD.Line.kUser5, 3, zAxis >= 0 ? "3" : "#");

		} else {
			talonFrontLeft.set(0.0);
			lcd.println(DriverStationLCD.Line.kUser5, 3, " ");
		}

		if (joyOperator.getRawButton(4)) {
			talonKickerRight.set(zAxis);
			lcd.println(DriverStationLCD.Line.kUser5, 4, zAxis >= 0 ? "4" : "$");

		} else {
			talonKickerRight.set(0.0);
			lcd.println(DriverStationLCD.Line.kUser5, 4, " ");
		}

		if (joyOperator.getRawButton(5)) {
			talonBackRight.set(zAxis);
			lcd.println(DriverStationLCD.Line.kUser5, 5, zAxis >= 0 ? "5" : "%");

		} else {
			talonBackRight.set(0.0);
			lcd.println(DriverStationLCD.Line.kUser5, 5, " ");
		}

		if (joyOperator.getRawButton(6)) {
			talonFrontRight.set(zAxis);
			lcd.println(DriverStationLCD.Line.kUser5, 6, zAxis >= 0 ? "6" : "^");

		} else {
			talonFrontRight.set(0.0);
			lcd.println(DriverStationLCD.Line.kUser5, 6, " ");
		}

		if (joyOperator.getRawButton(7)) {
			talonLoader.set(zAxis);
			lcd.println(DriverStationLCD.Line.kUser5, 7, zAxis >= 0 ? "7" : "&");

		} else {
			talonLoader.set(0.0);
			lcd.println(DriverStationLCD.Line.kUser5, 7, " ");
		}

		if (joyOperator.getRawButton(8)) {
			talonBackup.set(zAxis);
			lcd.println(DriverStationLCD.Line.kUser5, 8, zAxis >= 0 ? "8" : "*");

		} else {
			talonBackup.set(0.0);
			lcd.println(DriverStationLCD.Line.kUser5, 8, " ");
		}

		if (joyOperator.getRawButton(11)) {
			talonKickerLeft.set(+zAxis);
			talonKickerLeft.set(-zAxis);
			lcd.println(DriverStationLCD.Line.kUser5, 1, zAxis >= 0 ? "1" : "!");
			lcd.println(DriverStationLCD.Line.kUser5, 4, -zAxis >= 0 ? "4" : "$"); //inverted

		} // don't need to erase these since they are erased earlier

		lcd.updateLCD();
	}

	// We don't use Servos any more
//
//	/**
//	 * Tests Servos
//	 *
//	 * @param servos Motors to Test
//	 * @param joyOperator Operator Joystick
//	 * @param lcd DriverStationLCD to print to
//	 */
//	public static void servoTest(Servo[] servos, int start, Joystick joyOperator, DriverStationLCD lcd) {
//		double zAxis = joyOperator.getZ();
//		if (start < 0) {
//			start = 1;
//		}
//		lcd.println(DriverStationLCD.Line.kUser6, 1, (" " + String.valueOf(zAxis) + "        ").substring(0, 5));
//		lcd.updateLCD();
//		System.out.println("servo test.  Z:" + zAxis);
//
//		for (int i = 0; i < servos.length; i++) {
//			if (joyOperator.getRawButton(i + start)) {
//				servos[i].set((1 - zAxis) / 2);
//				lcd.println(DriverStationLCD.Line.kUser5, i + start, "" + (i + start));
//			} else {
//				servos[i].set(0.5);
//				lcd.println(DriverStationLCD.Line.kUser5, i + start, " ");
//			}
//		}
//		lcd.updateLCD();
//	}
	static void sensorTest() {
		if (joyOperator.getRawButton(FRC2014.JOYSTICK_RESET_ENCODERS_BUTTON) /*|| kickerOpticalSensor.get() == true*/) {
			kickerEncoder1.reset();
			kickerEncoder2.reset();
			lifterEncoder.reset();
		}

		String encoderString = ("" + kickerEncoder1.get() + "      ").substring(0, 4);
		encoderString += ("" + kickerEncoder2.get() + "      ").substring(0, 4);
		encoderString += ("" + lifterEncoder.get() + "      ").substring(0, 3);
		encoderString += ("" + leftDriveEncoder.get() + "      ").substring(0, 5);
		lcd.println(DriverStationLCD.Line.kUser2, 1, encoderString);

		String sensorString = "l:" + bool_0or1(lifterOpticalSensor.get()) + " ";
		sensorString += "k:" + bool_0or1(kickerOpticalSensor.get());
		lcd.println(DriverStationLCD.Line.kUser3, 1, sensorString);

		if ((counter = (counter + 1) % 100) == 0) {
			System.out.println("Kicker Encoder 1 " + kickerEncoder1.get());
		}
		System.out.println("Kicker Encoder 2 " + kickerEncoder2.get());
		System.out.println("Lifter Encoder " + lifterEncoder.get());
		System.out.println("Left Drive Encoder " + leftDriveEncoder.get());
		//           System.out.println("Right Drive Encoder " + rightDriveEncoder.get());
		System.out.println("Lifter Optical     " + lifterOpticalSensor.get());
		System.out.println("Kicker Optical     " + kickerOpticalSensor.get());
	}

	private static String bool_0or1(boolean b) {
		if (b) {
			return "1";
		} else {
			return "0";
		}
	}
}
