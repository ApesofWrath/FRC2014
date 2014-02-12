/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;

/**
 *
 * @author Programming
 */
public class FRC2014RobotDrive extends RobotDrive {

    protected Encoder leftEncoder;
    protected Encoder rightEncoder;

    // motors are m_frontLeftMotor, m_frontRightMotor, m_backLeftMotor, m_backRightMotor, they are from superclass
    public FRC2014RobotDrive(SpeedController left, SpeedController right, Encoder leftEncoder, Encoder rightEncoder) {
        super(left, right);
        this.leftEncoder = leftEncoder;
        this.rightEncoder = rightEncoder;

        robotDriveInit();
    }

    public FRC2014RobotDrive(SpeedController frontLeft, SpeedController backLeft, SpeedController frontRight, SpeedController backRight, Encoder leftEncoder, Encoder rightEncoder) {
        super(frontLeft, backLeft, frontRight, backLeft);
        this.leftEncoder = leftEncoder;
        this.rightEncoder = rightEncoder;

        robotDriveInit();
    }

    public FRC2014RobotDrive(int left, int right, int leftEncoderA, int leftEncoderB, int rightEncoderA, int rightEncoderB) {
        super(left, right);
        this.leftEncoder = new Encoder(leftEncoderA, leftEncoderB);
        this.rightEncoder = new Encoder(rightEncoderA, rightEncoderB);

        robotDriveInit();
    }

    public FRC2014RobotDrive(int frontLeft, int backLeft, int frontRight, int backRight, int leftEncoderA, int leftEncoderB, int rightEncoderA, int rightEncoderB) {
        super(frontLeft, backLeft, frontRight, backRight);
        this.leftEncoder = new Encoder(leftEncoderA, leftEncoderB);
        this.rightEncoder = new Encoder(rightEncoderA, rightEncoderB);

        robotDriveInit();
    }

    private void robotDriveInit() { //called by all constructors
        leftEncoder.start();
        rightEncoder.start();
    }

    public void tankDrive(GenericHID leftStick, GenericHID rightStick) {
        double leftEncoderRate = leftEncoder.getRate();
        double rightEncoderRate = rightEncoder.getRate();
        double leftY = leftStick.getY();
        double rightY = rightStick.getY();
        if (Math.abs(leftEncoderRate / rightEncoderRate) > Math.abs(leftY / rightY)) {
            //do correction, left is going more than right
            double leftMotor = leftY; //change leftMotor value to the correct value
            double rightMotor = rightY; //change rightMotor value to the correct value

            super.tankDrive(leftMotor, rightMotor, false); //do not square joystick values; that changes the math
        } else if (Math.abs(leftEncoderRate / rightEncoderRate) < Math.abs(leftY / rightY)) {
            //do opposite correction, right is going more than left
            double leftMotor = leftY; //change leftMotor value to the correct value
            double rightMotor = rightY; //change rightMotor value to the correct value

            super.tankDrive(leftMotor, rightMotor, false); //do not square joystick values; that changes the math
        } else { //we are actually going properly, no correction needed
            super.tankDrive(leftStick, rightStick, false); //do not square joystick values
        }
    }

    public void arcadeDrive(GenericHID stick) {

    }
}
