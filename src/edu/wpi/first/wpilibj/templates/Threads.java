/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

/**
 *
 * @author mark
 */
public class Threads {

	public static class ImageCaptureRunnable implements Runnable {

		private RobotVision.ResultReport result = null;

		public RobotVision.ResultReport getResult() {
			return result;
		}

		public void run() {
			result = RobotVision.cameraVision();
		}
	}

	public static class MoveLifterThread extends Thread {

		public final static boolean UP = true, DOWN = false;
		boolean direction;

		public void run() {
			if (direction == UP) {
				BallLifter.moveUp();
			} else {
				BallLifter.moveDown();
			}
		}

		public void start(boolean direction) {
			this.direction = direction;
			start();
		}
	}
	public static class MoveForwardThread extends Thread
	{
		public void run() {
			//Move Forward here
		}	
	}
	public static class ShootThread extends Thread
	{
		public void run() {
			// Shoot here
		}		
	}
}
