/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.*;
import edu.wpi.first.wpilibj.image.NIVision.MeasurementType;
import edu.wpi.first.wpilibj.Timer;
import java.util.Calendar;
import java.util.Date;

/**
 * Sample program to use NIVision to find rectangles in the scene that are
 * illuminated by a LED ring light (similar to the model from FIRSTChoice). The
 * camera sensitivity is set very low so as to only show light sources and
 * remove any distracting parts of the image.
 *
 * The CriteriaCollection is the set of criteria that is used to filter the set
 * of rectangles that are detected. In this example we're looking for rectangles
 * with a minimum width of 30 pixels and maximum of 400 pixels.
 *
 * The algorithm first does a color threshold operation that only takes objects
 * in the scene that have a bright green color component. Then a small object
 * filter removes small particles that might be caused by green reflection
 * scattered from other parts of the scene. Finally all particles are scored on
 * rectangularity, and aspect ratio, to determine if they are a target.
 *
 * Look in the VisionImages directory inside the project that is created for the
 * sample images.
 */
//we have chosen to use the Axis M1013 camera because it is the newest version of axis camera and has the largest field of view
public class RobotVision {

    //Camera constants used for distance calculation
    static final int Y_IMAGE_RES = 480;		//X Image resolution in pixels, should be 120, 240 or 480
    static final double AXIS_M1013 = 49;		//Axis M1013
    static final double AXIS_M1011 = 37.4;              //Axis M1011
    static final double VIEW_ANGLE = AXIS_M1013;
    static final double PI = 3.1415926535897932384626433832795028841971;

    //Score limits used for target identification
    static final int RECTANGULARITY_LIMIT = 35;
    static final int ASPECT_RATIO_LIMIT = 40;

    //Score limits used for hot target determination
    static final int TAPE_WIDTH_LIMIT = 42;
    static final int VERTICAL_SCORE_LIMIT = 50;
    static final int LR_SCORE_LIMIT = 50;

    //Minimum area of particles to be considered
    static final int AREA_MINIMUM = 150;

    //Maximum number of particles to process
    static final int MAX_PARTICLES = 8;

    static AxisCamera camera;          // the axis camera object (connected to the switch)
    static CriteriaCollection cc;      // the criteria for doing the particle filter operation
    static ConsoleImageProcess cip;

    public static class Scores {

        double rectangularity;
        double aspectRatioVertical;
        double aspectRatioHorizontal;
    }

    public static class TargetReport {

        int verticalIndex;
        int horizontalIndex;
        boolean Hot;
        double totalScore;
        double leftScore;
        double rightScore;
        double tapeWidthScore;
        double verticalScore;
    };

    public static class ResultReport { //reports results of vision search

        boolean targetExists;
        boolean isHot;
        double distance;

        public ResultReport(boolean isHot, double distance) { //creates a result report that reports a target
            this.isHot = isHot;
            this.distance = distance;
            this.targetExists = true;
        }

        public ResultReport() { //creates a result report that reports no target found
            this.isHot = false;
            this.distance = -1;
            this.targetExists = false;
        }
    }

    //used  to be robotInit
    static {
        camera = AxisCamera.getInstance();  // get an instance of the camera
        cc = new CriteriaCollection();      // create the criteria for the particle filter
        cc.addCriteria(MeasurementType.IMAQ_MT_AREA, AREA_MINIMUM, 65535, false);
        //cip = new ConsoleImageProcess();
    }

    public static double initializeCamera() { //returns time elapsed
        Timer t = new Timer();
        t.start();
        try {
            ColorImage image;
            while (t.get() < 6.5) {
                if (camera.freshImage()) {
                    image = camera.getImage();
                    if (image != null) {
//                        t.stop();
                        return t.get();
                    }
                }
            }
        } catch (AxisCameraException e) {
            return -1.0;
        } catch (NIVisionException e) {
            return -1.0;
        }
        return -1.0;
    }

    public static boolean takePicture(String prefix) {
        if (camera.freshImage()) {
            try {
                Calendar cal = Calendar.getInstance();
                Date d = cal.getTime();
                ColorImage image = camera.getImage();
                image.write("/" + prefix + "img" + d.getTime() + ".bmp");
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public static ResultReport cameraVision() {
        return vision(true, "");
    }

    //for tests
    public static ResultReport imageVision(String imagePath) {
        return vision(false, imagePath);
    }

    private static ResultReport vision(boolean isCamera, String imagePath) {
        TargetReport target = new TargetReport();
        int verticalTargets[] = new int[MAX_PARTICLES];
        int horizontalTargets[] = new int[MAX_PARTICLES];
        int verticalTargetCount, horizontalTargetCount;
        ResultReport returnValue = new ResultReport();
        try {
            /**
             * Do the image capture with the camera and apply the algorithm
             * described above. This sample will either get images from the
             * camera or from an image file stored in the top level directory in
             * the flash memory on the cRIO. The file name in this case is
             * "testImage.jpg"
             *
             */
            ColorImage image = null;
            if (isCamera) {
                if (camera.freshImage()) {
                    image = camera.getImage();
                } else {
                    return null;
                }
            } else {
                System.out.println(imagePath);
            }

            //System.out.println("image: "+image);
            //image.write("/image.bmp");
            //original values 105, 137, 230, 255, 133, 183
            //seeming to work values are 143, 227, 0, 255, 231, 255
            BinaryImage thresholdImage = image.thresholdHSV(81, 255, 100, 255, 215, 255);   // keep only green objects
            //thresholdImage.write("/threshold.bmp");
            BinaryImage filteredImage = thresholdImage.particleFilter(cc);           // filter out small particles
            //System.out.println("filteredImage: "+filteredImage);
            //filteredImage.write("/filteredImage.bmp");

            //iterate through each particle and score to see if it is a target
            Scores scores[] = new Scores[filteredImage.getNumberParticles()];
            horizontalTargetCount = verticalTargetCount = 0;

            //System.out.println("particles: "+filteredImage.getNumberParticles());
            if (filteredImage.getNumberParticles() > 0) {
                //ParticleAnalysisReport[] analysisReports = new ParticleAnalysisReport[filteredImage.getNumberParticles()]; //for ConsoleImageProcess
                for (int i = 0; i < MAX_PARTICLES && i < filteredImage.getNumberParticles(); i++) {
                    ParticleAnalysisReport report = filteredImage.getParticleAnalysisReport(i);
                    //analysisReports[i] = report; //for ConsoleImageProcess

                    scores[i] = new Scores();

                    //Score each particle on rectangularity and aspect ratio
                    scores[i].rectangularity = scoreRectangularity(report);
                    scores[i].aspectRatioVertical = scoreAspectRatio(filteredImage, report, i, true);
                    scores[i].aspectRatioHorizontal = scoreAspectRatio(filteredImage, report, i, false);

                    //Check if the particle is a horizontal target, if not, check if it's a vertical target
                    if (scoreCompare(scores[i], false)) { //we have found a horizontal target
                        System.out.println("particle: " + i + "is a Horizontal Target centerX: " + report.center_mass_x + "centerY: " + report.center_mass_y);
                        horizontalTargets[horizontalTargetCount++] = i; //Add particle to target array and increment count
                    } else if (scoreCompare(scores[i], true)) { //we have found a verticle target
                        image.write("/image" + Calendar.getInstance().getTime().getTime() + ".bmp"); //write what it sees for debugging
                        filteredImage.write("/filteredImage" + Calendar.getInstance().getTime().getTime() + ".bmp");
                        System.out.println("particle: " + i + "is a Vertical Target centerX: " + report.center_mass_x + "centerY: " + report.center_mass_y);
                        verticalTargets[verticalTargetCount++] = i;  //Add particle to target array and increment count
                    } else {
                        System.out.println("particle: " + i + "is not a Target centerX: " + report.center_mass_x + "centerY: " + report.center_mass_y);
                    }
                    System.out.println("rect: " + scores[i].rectangularity + "ARHoriz: " + scores[i].aspectRatioHorizontal);
                    System.out.println("ARVert: " + scores[i].aspectRatioVertical);
                }

                //cip.drawTargets(analysisReports); //uses ConsoleImageProcess to attempt to draw particles.
                //Zero out scores and set verticalIndex to first target in case there are no horizontal targets
                target.totalScore = target.leftScore = target.rightScore = target.tapeWidthScore = target.verticalScore = 0;
                target.verticalIndex = verticalTargets[0];
                //System.out.println("vertical "+verticalTargetCount);
                for (int i = 0; i < verticalTargetCount; i++) {
                    ParticleAnalysisReport verticalReport = filteredImage.getParticleAnalysisReport(verticalTargets[i]);
                    for (int j = 0; j < horizontalTargetCount; j++) {
                        ParticleAnalysisReport horizontalReport = filteredImage.getParticleAnalysisReport(horizontalTargets[j]);
                        double horizWidth, horizHeight, vertWidth, leftScore, rightScore, tapeWidthScore, verticalScore, total;

                        //Measure equivalent rectangle sides for use in score calculation
                        horizWidth = NIVision.MeasureParticle(filteredImage.image, horizontalTargets[j], false, MeasurementType.IMAQ_MT_EQUIVALENT_RECT_LONG_SIDE);
                        vertWidth = NIVision.MeasureParticle(filteredImage.image, verticalTargets[i], false, MeasurementType.IMAQ_MT_EQUIVALENT_RECT_SHORT_SIDE);
                        horizHeight = NIVision.MeasureParticle(filteredImage.image, horizontalTargets[j], false, MeasurementType.IMAQ_MT_EQUIVALENT_RECT_SHORT_SIDE);

                        //Determine if the horizontal target is in the expected location to the left of the vertical target
                        leftScore = ratioToScore(1.2 * (verticalReport.boundingRectLeft - horizontalReport.center_mass_x) / horizWidth);
                        //Determine if the horizontal target is in the expected location to the right of the  vertical target
                        rightScore = ratioToScore(1.2 * (horizontalReport.center_mass_x - verticalReport.boundingRectLeft - verticalReport.boundingRectWidth) / horizWidth);
                        //Determine if the width of the tape on the two targets appears to be the same
                        tapeWidthScore = ratioToScore(vertWidth / horizHeight);
                        //Determine if the vertical location of the horizontal target appears to be correct
                        verticalScore = ratioToScore(1 - (verticalReport.boundingRectTop - horizontalReport.center_mass_y) / (4 * horizHeight));
                        total = leftScore > rightScore ? leftScore : rightScore;
                        total += tapeWidthScore + verticalScore;

                        //If the target is the best detected so far store the information about it
                        if (total > target.totalScore) {
                            target.horizontalIndex = horizontalTargets[j];
                            target.verticalIndex = verticalTargets[i];
                            target.totalScore = total;
                            target.leftScore = leftScore;
                            target.rightScore = rightScore;
                            target.tapeWidthScore = tapeWidthScore;
                            target.verticalScore = verticalScore;
                        }
                    }
                    //Determine if the best target is a Hot target
                    target.Hot = hotOrNot(target);
                }

                if (verticalTargetCount > 0) {
                    //Information about the target is contained in the "target" structure
                    //To get measurement information such as sizes or locations use the
                    //horizontal or vertical index to get the particle report as shown below
                    ParticleAnalysisReport distanceReport = filteredImage.getParticleAnalysisReport(target.verticalIndex);
                    double distance = computeDistance(filteredImage, distanceReport, target.verticalIndex);
                    if (target.Hot) {
                        returnValue = new ResultReport(true, distance);
                    } else {
                        returnValue = new ResultReport(false, distance);
                    }
                }

            }

            /**
             * all images in Java must be freed after they are used since they
             * are allocated out of C data structures. Not calling free() will
             * cause the memory to accumulate over each pass of this loop.
             */
            filteredImage.free();
            thresholdImage.free();
            image.free();

        } catch (AxisCameraException ex) {        // this is needed if the camera.getImage() is called
            ex.printStackTrace();
        } catch (NIVisionException ex) {
            ex.printStackTrace();
        }

        return returnValue;
    }

    /**
     * Computes the estimated distance to a target using the height of the
     * particle in the image. For more information and graphics showing the math
     * behind this approach see the Vision Processing section of the
     * ScreenStepsLive documentation.
     *
     * @param image  The image to use for measuring the particle estimated
     *               rectangle
     * @param report The Particle Analysis Report for the particle
     * @param outer  True if the particle should be treated as an outer target,
     *               false to treat it as a center target
     * <p>
     * @return The estimated distance to the target in Inches.
     */
    static double computeDistance(BinaryImage image, ParticleAnalysisReport report, int particleNumber) throws NIVisionException {
        double rectLong, height;
        int targetHeight;

        rectLong = NIVision.MeasureParticle(image.image, particleNumber, false, MeasurementType.IMAQ_MT_EQUIVALENT_RECT_LONG_SIDE);
        //using the smaller of the estimated rectangle long side and the bounding rectangle height results in better performance
        //on skewed rectangles
        height = Math.min(report.boundingRectHeight, rectLong);
        targetHeight = 32;

        return Y_IMAGE_RES * targetHeight / (height * 12 * 2 * Math.tan(VIEW_ANGLE * Math.PI / (180)));
    }

    /**
     * Computes a score (0-100) comparing the aspect ratio to the ideal aspect
     * ratio for the target. This method uses the equivalent rectangle sides to
     * determine aspect ratio as it performs better as the target gets skewed by
     * moving to the left or right. The equivalent rectangle is the rectangle
     * with sides x and y where particle area= x*y and particle perimeter= 2x+2y
     *
     * @param image  The image containing the particle to score, needed to
     *               perform additional measurements
     * @param report The Particle Analysis Report for the particle, used for the
     *               width, height, and particle number
     * @param outer  Indicates whether the particle aspect ratio should be
     *               compared to the ratio for the inner target or the outer
     * <p>
     * @return The aspect ratio score (0-100)
     */
    public static double scoreAspectRatio(BinaryImage image, ParticleAnalysisReport report, int particleNumber, boolean vertical) throws NIVisionException {
        double rectLong, rectShort, aspectRatio, idealAspectRatio;

        rectLong = NIVision.MeasureParticle(image.image, particleNumber, false, MeasurementType.IMAQ_MT_EQUIVALENT_RECT_LONG_SIDE);
        rectShort = NIVision.MeasureParticle(image.image, particleNumber, false, MeasurementType.IMAQ_MT_EQUIVALENT_RECT_SHORT_SIDE);
        idealAspectRatio = vertical ? (4.0 / 32) : (23.5 / 4);	//Vertical reflector 4" wide x 32" tall, horizontal 23.5" wide x 4" tall

        //Divide width by height to measure aspect ratio
        if (report.boundingRectWidth > report.boundingRectHeight) {
            //particle is wider than it is tall, divide long by short
            aspectRatio = ratioToScore((rectLong / rectShort) / idealAspectRatio);
        } else {
            //particle is taller than it is wide, divide short by long
            aspectRatio = ratioToScore((rectShort / rectLong) / idealAspectRatio);
        }
        return aspectRatio;
    }

    /**
     * Compares scores to defined limits and returns true if the particle
     * appears to be a target
     *
     * @param scores The structure containing the scores to compare
     * @param outer  True if the particle should be treated as an outer target,
     *               false to treat it as a center target
     *
     * @return True if the particle meets all limits, false otherwise
     */
    static boolean scoreCompare(Scores scores, boolean vertical) {
        boolean isTarget = true;
        isTarget &= scores.rectangularity > RECTANGULARITY_LIMIT;
        //System.out.println("rect "+scores.rectangularity+", "+RECTANGULARITY_LIMIT);
        if (vertical) {
            isTarget &= scores.aspectRatioVertical > ASPECT_RATIO_LIMIT;
            //System.out.println("asp "+scores.aspectRatioVertical+", "+ASPECT_RATIO_LIMIT);
        } else {
            isTarget &= scores.aspectRatioHorizontal > ASPECT_RATIO_LIMIT;
            //System.out.println("asp "+scores.aspectRatioHorizontal+", "+ASPECT_RATIO_LIMIT);
        }

        return isTarget;
    }

    /**
     * Computes a score (0-100) estimating how rectangular the particle is by
     * comparing the area of the particle to the area of the bounding box
     * surrounding it. A perfect rectangle would cover the entire bounding box.
     *
     * @param report The Particle Analysis Report for the particle to score
     * <p>
     * @return The rectangularity score (0-100)
     */
    static double scoreRectangularity(ParticleAnalysisReport report) {
        if (report.boundingRectWidth * report.boundingRectHeight != 0) {
            return 100 * report.particleArea / (report.boundingRectWidth * report.boundingRectHeight);
        } else {
            return 0;
        }
    }

    /**
     * Converts a ratio with ideal value of 1 to a score. The resulting function
     * is piecewise linear going from (0,0) to (1,100) to (2,0) and is 0 for all
     * inputs outside the range 0-2
     */
    static double ratioToScore(double ratio) {
        return (Math.max(0, Math.min(100 * (1 - Math.abs(1 - ratio)), 100)));
    }

    /**
     * Takes in a report on a target and compares the scores to the defined
     * score limits to evaluate if the target is a hot target or not.
     *
     * Returns True if the target is hot. False if it is not.
     */
    static boolean hotOrNot(TargetReport target) {
        boolean isHot = true;

        isHot &= target.tapeWidthScore >= TAPE_WIDTH_LIMIT;
        isHot &= target.verticalScore >= VERTICAL_SCORE_LIMIT;
        isHot &= (target.leftScore > LR_SCORE_LIMIT) | (target.rightScore > LR_SCORE_LIMIT);

        return isHot;
    }

}
