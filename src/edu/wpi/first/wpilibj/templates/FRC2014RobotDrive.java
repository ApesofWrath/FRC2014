/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;

/**
 *
 * @author Programming
 */
public class FRC2014RobotDrive extends RobotDrive {
    
    static final double JOYSTICK_STRAIGHTNESS_ERROR = 0.1; //if joysticks are within JOYSTICK_STRAIGHTNESS_ERROR of each other you are trying to go straight
    static final int ENCODER_ERROR = 10; //if encoders are within ENCDODER_ERROR of each other you are actually going straight
    
    protected Encoder leftEncoder;
    protected Encoder rightEncoder;
    
    private int rightEncoderTurn;
    private int leftEncoderTurn; //value of left encoder after turning
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
    
    public FRC2014RobotDrive(int left, int right, Encoder leftEncoder, Encoder rightEncoder) {
        super(left, right);
        this.leftEncoder = leftEncoder;
        this.rightEncoder = rightEncoder;
        
        robotDriveInit();
    }
    
    public FRC2014RobotDrive(int frontLeft, int backLeft, int frontRight, int backRight, Encoder leftEncoder, Encoder rightEncoder) {
        super(frontLeft, backLeft, frontRight, backLeft);
        this.leftEncoder = leftEncoder;
        this.rightEncoder = rightEncoder;
        
        robotDriveInit();
    }
    
    private void robotDriveInit() { //called by all constructor
        rightEncoderTurn = rightEncoder.get();
        leftEncoderTurn = leftEncoder.get();
    }
    
    public void tankDrive(GenericHID leftStick, GenericHID rightStick) {
        if (Math.abs(leftStick.getY() - rightStick.getY()) <= JOYSTICK_STRAIGHTNESS_ERROR) //you are trying to go straight
        { // we must do correction
            int leftEncoderNoTurn = Math.abs(leftEncoderTurn - leftEncoder.get()); //assume they never turned to make the values even
            int rightEncoderNoTurn = Math.abs(rightEncoderTurn - rightEncoder.get());
            if (leftEncoderNoTurn > rightEncoderNoTurn) {
                //slow down left motor or speed up right motor
                //we need to do some testing to figure out by how much
            } else if (leftEncoderNoTurn < rightEncoderNoTurn) {
                //slow down right motor or speed up left motor
                //we need to do some testing to figure out by how much
            } else { //we are actually going straight, no correction needed
                super.tankDrive(leftStick, rightStick);
            }
        } else { //you're trying to turn
            super.tankDrive(leftStick, rightStick);
            leftEncoderTurn = leftEncoder.get();
            rightEncoderTurn = rightEncoder.get();
        }
    }
    
    public void arcadeDrive(GenericHID stick) {
        if (leftEncoder.get() > rightEncoder.get()) {
            
        } else if (leftEncoder.get() < rightEncoder.get()) {
            
        } else {
            super.arcadeDrive(stick);
        }
    }
}
