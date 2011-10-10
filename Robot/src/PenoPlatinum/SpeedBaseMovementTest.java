/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package PenoPlatinum;

import lejos.nxt.Button;

/**
 *
 * @author MHGameWork
 */
public class SpeedBaseMovementTest {

    private SpeedBasedMovement movement;

    public SpeedBaseMovementTest() {
        movement = new SpeedBasedMovement();
    }

    public void testForwardDrivingVariableSpeed() {
        movement.forward(0.2f/109f*100, 5);
        Button.waitForPress();
    }

    public void testForwardDriving() {

        movement.forward(0.50f, 2);
        Button.waitForPress();
        movement.forward(0.20f, 5);
        Button.waitForPress();
        movement.forward(0.10f, 10);
        Button.waitForPress();
        movement.forward(0.05f, 20);
        Button.waitForPress();
        movement.forward(0.025f, 40);
        Button.waitForPress();
        movement.forward(0.01f, 100);
        Button.waitForPress();
    }

    /*public void testRotation() {
    SpeedBasedMovement.turn(0.10f, 180, true);
    Button.waitForPress();
    SpeedBasedMovement.turn(0.05f, 180, true);
    Button.waitForPress();
    SpeedBasedMovement.turn(0.025f, 180, true);
    Button.waitForPress();
    SpeedBasedMovement.turn(0.01f, 180, true);
    Button.waitForPress();
    SpeedBasedMovement.turn(0.10f, 180, false);
    Button.waitForPress();
    SpeedBasedMovement.turn(0.05f, 180, false);
    Button.waitForPress();
    SpeedBasedMovement.turn(0.025f, 180, false);
    Button.waitForPress();
    SpeedBasedMovement.turn(0.01f, 180, false);
    Button.waitForPress();
    }
    
    public void testMoveStraight() {
    SpeedBasedMovement mov = new SpeedBasedMovement();
    mov.MoveStraight(1000);
    Button.waitForPress();
    mov.MoveStraight(500);
    }
    
    public void testTurnOnSpotCCW() {
    SpeedBasedMovement mov = new SpeedBasedMovement();
    mov.TurnOnSpotCCW(360);
    Button.waitForPress();
    mov.TurnOnSpotCCW(180);
    }*/
}
