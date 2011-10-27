/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.sensor;

import penoplatinum.barcode.LightSensorRobot;
import penoplatinum.movement.IMovement;

/**
 *
 * @author Thomas
 */
public class CalibratieTurnOnSpot {
    LightSensorRobot l;
    IMovement m;

    public CalibratieTurnOnSpot(LightSensorRobot l, IMovement m) {
        this.l = l;
        this.m = m;
    }

    public void run(){
        int turnCount=0;
        int executeCount = 0;
        boolean blackNext = false;
        while(true){
            if(m.isStopped()){
                if(executeCount!=turnCount){
                    break;
                }
                executeCount+=2;
                m.TurnOnSpotCCW(360.0, false);
            }
            if(blackNext&&!LightSensorRobot.isBrown(l.readValue())){
                turnCount++;
                blackNext = false;
            }
            if(!blackNext && LightSensorRobot.isBrown(l.readValue())){
                blackNext = true;
            }
        }
        System.out.println(turnCount+" "+executeCount);
        double afwijking = 180.0/360.0/turnCount;
        System.out.println(""+afwijking);
    }
    
}
