package edu.wpi.first.wpilibj.templates;

public class Threads {

    public static class ImageCaptureRunnable implements Runnable {

        private RobotVision.ResultReport result = null;

        public RobotVision.ResultReport getResult() {
            return result;
        }

        public void run() {
            System.out.println("Camera vision");
            result = RobotVision.cameraVision();
            System.out.println("Camera vision done");
        }
    }

    /*    public static class MoveLifterThread extends Thread {

     public final static boolean UP = true, DOWN = false;
     boolean direction;

     public void run() {
     System.out.println("Moving Lifter");
     if (direction == UP) {
     while(!BallLifter.moveUp() && FRC2014.isAutonomous);
     } else {
     while(!BallLifter.moveDown() && FRC2014.isAutonomous);
     }
     }

     public void start(boolean direction) {
     this.direction = direction;
     start();
     }
     }
     */
    /*    public static class MoveForwardThread extends Thread {

     public void run() {
     //Move Forward here
     System.out.println("Moving Forward");
     Timer autoTimer = new Timer();
     autoTimer.start();
     while (autoTimer.get() <= 1.5 && FRC2014.isAutonomous) {
     FRC2014.driver.drive(-1.0, 0.05);
     }
     FRC2014.driver.drive(0.0, 0.0);
     }
     }
     */
    /*    public static class ShootThread extends Thread {

     public void run() {
     System.out.println("Kicking");
     while (!Kicker.load() && FRC2014.isAutonomous);
     while (!Kicker.kick() && FRC2014.isAutonomous);
     }
     }
     */
}
