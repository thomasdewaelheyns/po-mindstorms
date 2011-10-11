package penoplatinum.movement;

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
