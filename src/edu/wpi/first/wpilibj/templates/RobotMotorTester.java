/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Talon;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotMotorTester {

    /**
     * Tests Motors
     *
     * @param motors Motors to Test
     * @param joyOperator Operator Joystick
     * @param lcd DriverStationLCD to print to
     */
    public static void motorTest(Talon[] motors, int start, Joystick joyOperator, DriverStationLCD lcd) {
        double zAxis = joyOperator.getZ();
        if (start < 0) {
            start = 1;
        }
        lcd.println(DriverStationLCD.Line.kUser6, 1, (" " + String.valueOf(zAxis) + "        ").substring(0, 5));
        lcd.updateLCD();
        System.out.println("motor test.  Z:" + zAxis);

        for (int i = 0; i < motors.length; i++) {
            if (!joyOperator.getRawButton(11)) {
                motors[0].set(+0.0);
                motors[3].set(-0.0);
                lcd.println(DriverStationLCD.Line.kUser5, 1, " ");
                lcd.println(DriverStationLCD.Line.kUser5, 4, " ");


            }
            if (joyOperator.getRawButton(i + start)) {
                motors[i].set(zAxis);
                lcd.println(DriverStationLCD.Line.kUser5, i + start, "" + (i + start));
            } else {
                motors[i].set(0);
                lcd.println(DriverStationLCD.Line.kUser5, i + start, " ");
            }
            if (joyOperator.getRawButton(11)) {
                motors[0].set(+zAxis);
                motors[3].set(-zAxis);
                lcd.println(DriverStationLCD.Line.kUser5, 1, "1");
                lcd.println(DriverStationLCD.Line.kUser5, 4, "4");

            }
        }
        lcd.updateLCD();
    }

    /**
     * Tests Servos
     *
     * @param servos Motors to Test
     * @param joyOperator Operator Joystick
     * @param lcd DriverStationLCD to print to
     */
    public static void servoTest(Servo[] servos, int start, Joystick joyOperator, DriverStationLCD lcd) {
        double zAxis = joyOperator.getZ();
        if (start < 0) {
            start = 1;
        }
        lcd.println(DriverStationLCD.Line.kUser6, 1, (" " + String.valueOf(zAxis) + "        ").substring(0, 5));
        lcd.updateLCD();
        System.out.println("servo test.  Z:" + zAxis);

        for (int i = 0; i < servos.length; i++) {
            if (joyOperator.getRawButton(i + start)) {
                servos[i].set((1 - zAxis) / 2);
                lcd.println(DriverStationLCD.Line.kUser5, i + start, "" + (i + start));
            } else {
                servos[i].set(0.5);
                lcd.println(DriverStationLCD.Line.kUser5, i + start, " ");
            }
        }
        lcd.updateLCD();
    }
}
