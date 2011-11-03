package penoplatinum.sensor;

import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.UltrasonicSensor;
import penoplatinum.movement.IMovement;

public class Muurvolger {
    private UltrasonicSensor ultra;
    public IMovement move;
    public int turnAngle=45;
    private Motor vert;
    private int isLeft = -1;
    
    private int toClose = 30;
    private int toFar   = 40;
    private int WALLFORWARD = 50;
    
    private int angleClose= 10;
    private int angleFar = 10;
    private int angleTurn = 10;
    private int angleLook = 90;
    
    private double dist=0.10;

    private int SAMPLES = 5;
    private int MAX=128;
    
    public boolean debug=true;
    public boolean cont = true;

    public Muurvolger(UltrasonicSensor ultra, IMovement m, Motor v) {
        this.ultra = ultra;
        isLeft = askSensorDirection();
        move=m;
        vert=v;
    }

    public final int askSensorDirection() {
        return -1;	//+-1
    }

    public void run() {
        double avg = 0;
        for(int i=0;i<SAMPLES;i++){
            avg+=ultra.getDistance();
        }
        avg/=SAMPLES;
        if(avg>MAX){ //No Wall, move to check left
            if(debug)LCD.drawString("hoekRechts", 0, 1);
            move.TurnOnSpotCCW(isLeft * 90);
        }
        avg = 0;
        for(int i=0;i<SAMPLES;i++){
            avg+=ultra.getDistance();
        }
        avg/=SAMPLES;
        LCD.drawString(avg + "    ", 0, 0);
        if (avg >= MAX){  //No data, assume corner
            if(debug)LCD.drawString("hoekLinks ", 0, 1);
            move.TurnOnSpotCCW(isLeft * turnAngle);
        } else if (avg < toClose) {
            if(debug)LCD.drawString("toClose   ", 0, 1);
            move.TurnOnSpotCCW(-isLeft * angleClose);
        } else if (avg > toFar) {
            if(debug)LCD.drawString("toFar     ", 0, 1);
            move.TurnOnSpotCCW(isLeft * angleFar);
        } else {
            if(debug)LCD.drawString("good      ", 0, 1);
        }
        move.TurnOnSpotCCW(-isLeft * 90); //move back forward
        move.MoveStraight(dist);
    }
    public void run2(){ //oude versie door merge
        while(cont){
            double avg = 0;
            for(int i=0;i<SAMPLES;i++){
                avg+=ultra.getDistance();
            }
            avg/=SAMPLES;
            if(avg<WALLFORWARD){ //No Wall, move to check left
                if(debug)LCD.drawString("hoekRechts", 0, 1);
                move.TurnOnSpotCCW(-isLeft * angleTurn);
            }

            
            vert.rotate(isLeft * angleLook);
            avg = 0;
            for(int i=0;i<SAMPLES;i++){
                avg+=ultra.getDistance();
            }
            avg/=SAMPLES;
            if(debug){LCD.drawString(avg + "    ", 0, 0);}

            if (avg >= MAX){  //No data, assume corner
                if(debug)LCD.drawString("hoekLinks ", 0, 1);
                move.TurnOnSpotCCW(isLeft * angleTurn);
            } else
            if (avg < toClose) {
                if(debug)LCD.drawString("toClose   ", 0, 1);
                move.TurnOnSpotCCW(-isLeft * angleClose);
            } else
            if (avg > toFar) {
                if(debug)LCD.drawString("toFar     ", 0, 1);
                move.TurnOnSpotCCW(isLeft * angleFar);
            } else {
                if(debug)LCD.drawString("good      ", 0, 1);
            }
            
            vert.rotate(-isLeft * angleLook);
            move.MoveStraight(dist);
        }
        
    }
    
    
    
}