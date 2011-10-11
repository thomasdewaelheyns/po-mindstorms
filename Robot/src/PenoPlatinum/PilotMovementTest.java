/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package PenoPlatinum;

/**
 *
 * @author MHGameWork
 */
public class PilotMovementTest {
    public void testForwardMovement()
    {
        PilotMovement mov = new PilotMovement();
        mov.MoveStraight(1);
    }
    public void testRotate()
    {
        PilotMovement mov = new PilotMovement();
        mov.TurnOnSpotCCW(90);
    }
}
