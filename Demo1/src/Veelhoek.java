
import Movement.RotationMovement;
import lejos.nxt.*;

public class Veelhoek {
    static void veelhoekRotate(double l, int n){
        RotationMovement mov=new RotationMovement();
        double hoek=360.0/n;
        for(int i=0;i<n;i++){
            mov.MoveStraight(l);
            mov.TurnOnSpotCCW(hoek);
        }
        Motor.B.stop();
        Motor.C.stop();
    }
}
