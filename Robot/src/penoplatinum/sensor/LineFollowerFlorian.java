/*
 *
 */
package penoplatinum.sensor;

import lejos.nxt.*;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import penoplatinum.Utils;
import penoplatinum.movement.RotationMovement;

/**
 * This class is meant to be used as a controller for line following. It is based upon the fact we can use a light intensity sensor
 * 
 * @author Florian
 */
public class LineFollowerFlorian {

    final LightSensor light = new LightSensor(SensorPort.S1, true);
    final RotationMovement agent = new RotationMovement();
    int rightDirection;
    double LineThresHold;
    double platformThresHold;
    private boolean lastLineWasWhite;
    boolean LineType;
    private int WHITEVAL;
    private int BROWNVAL;
    private int BLACKVAL;
    private final int SAMPLES = 5;

    public LineFollowerFlorian() {
    }

    public void calibrate() {
        // calibreer de lage waarde.
        System.out.println("Zet de sensor op zwart en druk enter.");
        Button.ENTER.waitForPressAndRelease();
        LCD.drawInt(light.readValue(), 1, 0);
        light.calibrateLow();
        Sound.beep();
        Utils.Sleep(1000);

        // calibreer de hoge waarde
        System.out.println("Zet de sensor op wit en druk enter.");
        Button.ENTER.waitForPressAndRelease();
        LCD.drawInt(light.readValue(), 3, 0);
        light.calibrateHigh();
        WHITEVAL = light.readValue();
        Sound.beep();
        Utils.Sleep(1000);


        System.out.println("Zet de sensor op zwart en druk enter.");
        Button.ENTER.waitForPressAndRelease();
        BLACKVAL = light.readValue();
        Sound.beep();
        Utils.Sleep(1000);

        System.out.println("Zet de sensor op bruin en druk enter.");
        Button.ENTER.waitForPressAndRelease();
        LCD.drawInt(light.readValue(), 3, 0);
        BROWNVAL = light.readValue();
        Sound.beep();
        Utils.Sleep(1000);

        System.out.println("Juiste rijrichting?, druk rechts");
        int dir = Button.waitForPress();
        if (dir == Button.ID_RIGHT) {
            rightDirection = 1;
        } else {
            rightDirection = -1;
        }

        LCD.clear();
    }
    public void changeSpeed(int speed ){
        agent.SPEEDFORWARD=speed;
        agent.SPEEDTURN=160;
                

    }
    public void ActionLineFollower() {
        calibrate();
        changeSpeed(280);
        while (true) {
            if (isColor(Color.Brown)) {
                LineFinder();
            }
            agent.MoveStraight(1.0, false);
            lastLineWasWhite = isColor(Color.White);
            Utils.Sleep(100);
        }
    }

    public void LineFinder() {
        int[] rotates = new int[]{-3,8,-15,50, -95, 50 + 120, 240, 720};
        int pos = 0;
        System.out.println("FindLine");
        agent.Stop();
        while (isColor(Color.Brown)) {
            if (stopped()) {
                agent.TurnOnSpotCCW(rotates[pos++]);
                
//                (!lastLineWasWhite ? rightDirection : -rightDirection)
            }
        }
        System.out.println("FoundLine");
    }

    public int getLightValue() {
        int avg = 0;
        for (int i = 0; i < SAMPLES; i++) {
            avg += light.readValue();
        }
        LCD.drawString(avg / SAMPLES + "   ", 0, 0);
        return avg / SAMPLES;
    }

    public boolean stopped() {
        return !agent.motorLeft.isMoving() && !agent.motorRight.isMoving();
    }
//
//    public boolean isBrown() {
//        return isBrown(getLightValue());
//    }
//
//    private boolean isBrown(double val) {
//        return val > (BLACKVAL + BROWNVAL) / 2 && val < (WHITEVAL + BROWNVAL) / 2;
//    }
//
//    public boolean isWhite() {
//        return isWhite(getLightValue());
//    }
//
//    private boolean isWhite(double val) {
//        return val < (WHITEVAL + BROWNVAL) / 2;
//    }
//
//    public boolean isBlack() {
//        return isBlack(getLightValue());
//    }
//
//    private boolean isBlack(double val) {
//        return val > (BLACKVAL + BROWNVAL) / 2;
//    }
//    
    public enum Color{
        Black,White,Brown; 

    }
    public boolean isColor(Color col,double val) {
        switch (col) {
            case Brown:
                return val > (BLACKVAL + BROWNVAL) / 2 && val < (WHITEVAL + BROWNVAL) / 2;
            case Black:
                return val < (BLACKVAL + BROWNVAL) / 2;
            case White:
                return val > (WHITEVAL + BROWNVAL) / 2;
            
            
        }
        throw new AssertionError("Unknown op: " + this);
    }

    public boolean isColor(Color col) {
        return isColor(col, getLightValue());
    }
    public Color getCurrentColor() {
        if (isColor(Color.Brown)) {
            return Color.Brown;
        }
        if (isColor(Color.Black)) {
            return Color.Black;
        }
        return Color.White;
        
    
}
      
}
