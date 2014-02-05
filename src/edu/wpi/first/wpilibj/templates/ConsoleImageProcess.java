/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;
import edu.wpi.first.wpilibj.templates.RobotVision.Scores;

/**
 *
 * @author Administrator
 */
public class ConsoleImageProcess {

    public static final int IMAGE_WIDTH = RobotVision.camera.getResolution().width;
    public static final int IMAGE_HEIGHT = RobotVision.camera.getResolution().height;
    public static final int BUFFER_HEIGHT = 30;
    public static final int BUFFER_SCREEN_HEIGHT = BUFFER_HEIGHT - 2;
    public static final int BUFFER_WIDTH = (int) (BUFFER_SCREEN_HEIGHT * IMAGE_HEIGHT / IMAGE_WIDTH);
    private char printBuffer[][] = new char[BUFFER_WIDTH][BUFFER_HEIGHT];

    public ConsoleImageProcess() {
    }

    // Get particle analysis reports from the image processing code
    public char[][] drawTargets(ParticleAnalysisReport reports[]) {
        double pixelsPerCharHoriz = IMAGE_WIDTH / BUFFER_WIDTH;
        double pixelsPerCharVert = IMAGE_HEIGHT / BUFFER_SCREEN_HEIGHT;
        String infoLines[] = new String[BUFFER_SCREEN_HEIGHT - BUFFER_HEIGHT];
        if (reports.length == 0) {
            return null;
        }
        infoLines[0] = "" + reports.length + " targets found";
        infoLines[1] = "Image W: " + reports[0].imageWidth + " H: " + reports[0].imageHeight;

        for (int y = 0; y < BUFFER_HEIGHT; y++) {
            for (int x = 0; x < BUFFER_WIDTH; x++) {

                printBuffer[x][y] = ' ';
                if (y < BUFFER_SCREEN_HEIGHT) {
                    for (int i = 0; i < reports.length; i++) {
                        ParticleAnalysisReport score = reports[i];
                        int rectLeft = (int) (score.boundingRectLeft / pixelsPerCharHoriz);
                        int rectRight = (int) ((score.boundingRectLeft + score.boundingRectWidth) / pixelsPerCharHoriz);
                        int rectTop = (int) (score.boundingRectTop / pixelsPerCharVert);
                        int rectBottom = (int) ((score.boundingRectTop + score.boundingRectHeight) / pixelsPerCharVert);

                        if (x >= rectLeft && x < rectRight - 1 && y == rectTop) {
                            printBuffer[x][y] = (char) ('A' + i);
                        }
                        if (x >= rectLeft && x < rectRight - 1 && y == rectBottom - 1) {
                            printBuffer[x][y] = (char) ('A' + i);
                        }
                        if (x == rectLeft && y >= rectTop && y < rectBottom - 1) {
                            printBuffer[x][y] = (char) ('A' + i);
                        }
                        if (x == rectRight - 1 && y >= rectTop && y < rectBottom - 1) {
                            printBuffer[x][y] = (char) ('A' + i);
                        }

                        System.out.print(printBuffer[x][y]);
                    }
                }
                else
                {
                    // print info lines here
                    printBuffer[x][y] =
                }
                System.out.print("\n");
            }
        }
        return printBuffer;
    }

}

/*
 void printGraphs()
 {
 //cout<<"Interval:"<<sampleRate<<"\t";
 initializePrintBuffers();
 int placeOfTarget = (int)((PRINT_BUFFER_SIZE-1) * target / MAX_RPM);

 // Why is this comment here?
 //  // _ becuase there are two different rpm's
 int placeOfrpm = (int)((PRINT_BUFFER_SIZE - 1) * getShooterWheelRPM()
 / MAX_RPM + .5);

 int placeOfPWM = (int)((PRINT_BUFFER_SIZE - 1)
 * fabs(GET_MOTOR_SPEED) + .5);

 //int placeOfP = (int)((PRINT_BUFFER_SIZE - 1) * kP / kPID_RANGE);
 //int placeOfI = (int)((PRINT_BUFFER_SIZE - 1) * kI / kPID_RANGE);
 //int placeOfD = (int)((PRINT_BUFFER_SIZE - 1) * kD / kPID_RANGE);

 int placeOfP = (int)((PRINT_BUFFER_SIZE - 1) * P / kPID_RANGE);
 int placeOfI = (int)((PRINT_BUFFER_SIZE - 1) * I / kPID_RANGE);
 int placeOfD = (int)((PRINT_BUFFER_SIZE - 1) * D / kPID_RANGE);


 printNumberInBuffer(placeOfTarget, 'T', 0);
 printNumberInBuffer(placeOfrpm, 'R', 0);
 printNumberInBuffer(placeOfPWM, 'W', 0);

 printNumberInBuffer(placeOfP, 'P', 1);
 printNumberInBuffer(placeOfI, 'I', 1);
 printNumberInBuffer(placeOfD, 'D', 1);

 printf("|%s|\n", printbuffer[0]);
 printf("|%s|\n", printbuffer[1]);
 printf("\n");

 #if 0
 //Cleanup
 if(placeOfTarget <= PRINT_BUFFER_SIZE && placeOfTarget >= 0 )
 {
 printbuffer[placeOfTarget] = ' ';
 }
 else if(placeOfTarget> PRINT_BUFFER_SIZE)
 {
 printbuffer[PRINT_BUFFER_SIZE] = ' ';
 }
 else
 {
 printbuffer[0] = ' ';
 }
 if(placeOfrpm <= PRINT_BUFFER_SIZE && placeOfrpm >= 0)
 {
 printbuffer[placeOfrpm] = ' ';
 }
 else if(placeOfrpm> PRINT_BUFFER_SIZE)
 {
 printbuffer[PRINT_BUFFER_SIZE] = ' ';
 }
 else
 {
 printbuffer[0] = ' ';
 }
 #else 
 int q = 0;
 for (; q < PRINT_BUFFER_SIZE; q++)
 {
 printbuffer[0][q] = ' ';
 printbuffer[1][q] = ' ';
 }
 #endif
 }
 void printNumberInBuffer(int value, char character, int index)
 {
 if ((value <= (PRINT_BUFFER_SIZE-1)) && (value >= 0 ))
 {
 printbuffer[index][value] = character;
 }
 // it's out of bounds to the left or right, draw a '?'
 else if (value > (PRINT_BUFFER_SIZE-1))
 {
 // Print the lower case of the letter if it is out of bounds
 printbuffer[index][PRINT_BUFFER_SIZE-1] = character + ('a' - 'A');
 }
 else
 {
 printbuffer[index][0] = character + ('a' - 'A');
 }
 }
 void initializePrintBuffers()
 {
 int printindex;
 for (printindex=0; printindex<PRINT_BUFFER_SIZE; printindex++)
 {
 printbuffer[0][printindex]=' ';
 printbuffer[1][printindex]=' ';
 }

 printbuffer[0][PRINT_BUFFER_SIZE]='\0';
 printbuffer[1][PRINT_BUFFER_SIZE]='\0';

 //printbuffer[0][0] = '|';
 //printbuffer[1][0] = '|';
 //printbuffer[0][PRINT_BUFFER_SIZE-1] = '|';
 //printbuffer[1][PRINT_BUFFER_SIZE-1] = '|';
 }
 */
