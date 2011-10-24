package penoplatinum.sensor;

import java.util.ArrayList;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Sound;
import penoplatinum.movement.IMovement;
import penoplatinum.movement.Utils;

public class BarcodeRuben {
    private final int SAMPLES = 5;
    private final int SWEEP = 10;
    private final int MAX_BROWN_COUNT = 5;
    private final int BARCODE_SLEEP = 20;
    
    private final double standardDist = 0.02;


    private IMovement m;
    private LightSensor l;
    public boolean cont = true;
    private int BLACKVAL;
    private int WHITEVAL;
    private int BROWNVAL;

    public BarcodeRuben(LightSensor l2, IMovement m2){
        l=l2;
        m=m2;
    }

    public void run(){
        calibrate();
        Utils.Sleep(1000);
        while(cont){
            m.MoveStraight(standardDist, false);
            LCD.drawString("Checking      ", 0, 1);
            if(!isBrown()){
                LCD.drawString("Found possible", 0, 1);
                boolean wasBlack = isBlack();
                m.TurnOnSpotCCW(SWEEP);
                if(isBrown()){
                    LCD.drawString("Found false   ", 0, 1);
                    if(!wasBlack){
                        m.TurnOnSpotCCW(-SWEEP*2);
                    }
                } else {
                    LCD.drawString("Found Barcode ", 0, 1);
                    m.TurnOnSpotCCW(-SWEEP);
                    ArrayList<Integer> rec = record();
                    LCD.drawString("End record    ", 0, 1);
                    int code = translate(rec);
                    move(code);
                    code = correct(code);
                    move(code);
                }
            }
            Utils.Sleep(BARCODE_SLEEP);
        }
    }
    private void move(int code){
        LCD.drawString("Code: "+code, 0, 2);
        return;
    }

    private ArrayList<Integer> record() {
        ArrayList<Integer> rec = new ArrayList<Integer>();
        m.MoveStraight(0.5, false);
        int brownCount=0;
        int notBrownCount=0;
        while (brownCount<MAX_BROWN_COUNT) {
            int val = getLightValue();
            if(isBrown(val)){
                brownCount++;
                //notBrownCount = 0;  //not used yet
            } else {
                //notBrownCount++;
                //if(notBrownCount>2){
                    brownCount = 0;
                //}
            }
            rec.add(val);
            Utils.Sleep(BARCODE_SLEEP);
        }
        for(int i=0;i<MAX_BROWN_COUNT;i++){
            rec.remove(rec.size()-1);
        }
        return rec;
    }


    public static byte[] barcodesRepair = getBarcodes();
    public static byte[] getBarcodes(){
        byte[] out = new byte[128];
        byte[] original = new byte[]{0,15,22,25,37,42,51,60,67,76,85,90,102,105,112,127};
        for(int i=0;i<original.length;i++){
            out[original[i]]=original[i];
            for(int j=1;j<128;j*=2){
                out[original[i]^j]=original[i];
            }
        }
        return out;
    }
    public void calibrate(){
        // calibreer de lage waarde.
        System.out.println("Zet de sensor op zwart en druk enter.");
        Button.ENTER.waitForPressAndRelease();
        LCD.drawInt(l.readValue(), 1, 0);
        l.calibrateLow();
        BLACKVAL = l.readValue();
        Sound.beep();
        Utils.Sleep(1000);

        // calibreer de hoge waarde
        System.out.println("Zet de sensor op wit en druk enter.");
        Button.ENTER.waitForPressAndRelease();
        LCD.drawInt(l.readValue(), 3, 0);
        l.calibrateHigh();
        WHITEVAL = l.readValue();
        Sound.beep();
        Utils.Sleep(1000);

        System.out.println("Zet de sensor op bruin en druk enter.");
        Button.ENTER.waitForPressAndRelease();
        BROWNVAL = l.readValue();
        Sound.beep();
        Utils.Sleep(1000);

        LCD.clear();
    }
    public int getLightValue(){
        int avg=0;
        for(int i=0;i<SAMPLES;i++){
            avg+=l.readValue();
        }
        LCD.drawString(avg/SAMPLES+"   ", 0, 0);
        return avg/SAMPLES;
    }

    public boolean isBrown(){
        return isBrown(getLightValue());
    }
    private boolean isBrown(double val) {
        return val > (BLACKVAL+BROWNVAL)/2 && val < (WHITEVAL+BROWNVAL)/2;
    }
    
    public boolean isWhite(){
        return isWhite(getLightValue());
    }
    private boolean isWhite(double val) {
        return val < (WHITEVAL+BROWNVAL)/2;
    }
    
    public boolean isBlack(){
        return isBlack(getLightValue());
    }
    private boolean isBlack(double val) {
        return val > (BLACKVAL+BROWNVAL)/2;
    }

    private int translate(ArrayList<Integer> rec) {
        int length = rec.size();
        if(length<7){
            System.out.println("Not enough data!");
            return -1;
        }
        int out = 0;
        for(int i=0;i<7;i++){
            double avg=0;
            for(int j=length*i/7;j<length*(i+1)/7;j++){
                avg+=rec.get(j);
            }
            avg/=length*(i+1)/7-length*i/7;
            out*=2;
            out += (isBlack(avg)?0:1);
        }
        return out;
    }
    private int correct(int in){
        return barcodesRepair[in];
    }
}