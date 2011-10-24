package penoplatinum.sensor;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import penoplatinum.movement.IMovement;
import lejos.nxt.LightSensor;
import lejos.nxt.Sound;
import penoplatinum.movement.Utils;

public class LijnVolgerRuben {
    IMovement m;
    LightSensor l;
    boolean cont = true;

    private final int SWEEP_ANGLE=5;
    private final int SAMPLES = 5;

    private final int WHITE = 80;
    private final int BLACK = 50;

    public LijnVolgerRuben(IMovement m, LightSensor l) {
        this.m = m;
        this.l = l;
    }
    public void calibrate(){
        // calibreer de lage waarde.
        System.out.println("Zet de sensor op zwart en druk enter.");
        Button.ENTER.waitForPressAndRelease();
        LCD.drawInt(l.readValue(), 1, 0);
        l.calibrateLow();
        
        Sound.beep();
        Utils.Sleep(1000);

        // calibreer de hoge waarde
        System.out.println("Zet de sensor op wit en druk enter.");
        Button.ENTER.waitForPressAndRelease();
        LCD.drawInt(l.readValue(), 3, 0);
        l.calibrateHigh();

        Sound.beep();
        Utils.Sleep(1000);
    }

    public void run(){
        while(cont){
            int sweep=0;
            while(isBrown()){
                m.TurnOnSpotCCW(sweep*SWEEP_ANGLE);
                sweep = -sweep +(sweep>0?-1:1);
            }
            m.MoveStraight(0.05);
        }
    }
    public int getLightValue(){
        int avg=0;
        for(int i=0;i<SAMPLES;i++){
            avg+=l.readValue();
        }
        return avg/SAMPLES;
    }
    public boolean isBrown(){
        int val=getLightValue();
        return val>BLACK && val<WHITE;
    }
    public boolean isWhite(){
        return getLightValue()<WHITE;
    }
    public boolean isBlack(){
        return getLightValue()>BLACK;
    }
}
