package penoplatinum;

import lejos.nxt.*;
import penoplatinum.movement.IMovement;

public class Veelhoek {
    private final IMovement mov;

    public Veelhoek(IMovement mov) {
        this.mov = mov;
    }

    public void veelhoekRotate(double l, int n) {
        double hoek = 360.0 / n;
        for (int i = 0; i < n; i++) {
            mov.MoveStraight(l);
            mov.TurnOnSpotCCW(hoek);
        }
        Motor.B.stop();
        Motor.C.stop();
    }
}
