package edu.wpi.first.wpilibj.templates;

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

    public static class MoveForwardThread extends Thread {

        public void run() {
            //Move Forward here
        }
    }

    public static class ShootThread extends Thread {

        public void run() {
            while (Kicker.load());
            while (Kicker.kick());
        }
    }
}
