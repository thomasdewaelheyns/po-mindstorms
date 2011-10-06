/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package PenoPlatinum;

import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;

/**
 *
 * @author MHGameWork
 */
public class CalibrateStraightLine implements IAction {

    ActionParameters parameters = new ActionParameters();

    public ActionParameters GetParameters() {
        return parameters;
    }

    public void Execute() {
        RubenMovement rub = new RubenMovement();
        rub.CalibrateWheelCircumference();
        /*IMovement movement = new RubenMovement();
        movement.MoveStraight(100);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
        }

        TouchSensor sensor = new TouchSensor(SensorPort.S4);
        System.out.println("Weeeeee");
        while (!sensor.isPressed()) {
            
        }
        System.out.println("Auch!");*/
        
        
    }
}
