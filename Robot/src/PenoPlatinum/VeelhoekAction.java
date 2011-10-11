package PenoPlatinum;
import lejos.nxt.Motor;

public class VeelhoekAction implements IAction {

    static
    {
        //TODO: fix
        ActionContainer.AddAction(new VeelhoekAction());
    }

    static void veelhoek(int number, int time) {
        int angle = 180 * (number - 2) / number;
        for (int i = 0; i < number; i++) {
            forward(time);
            Utils.Sleep(1000);
            turn(angle);
            Utils.Sleep(1000);
        }
    }
    static void veelhoekRotate(double l, int n){
        RubenMovement mov=new RubenMovement();
        double hoek=360.0/n;
        for(int i=0;i<n;i++){
            mov.MoveStraight(l);
            mov.TurnOnSpotCCW(hoek);
        }
        Motor.B.stop();
        Motor.C.stop();
    }

    static void forward(int time) {
        int speed = 720;
        Motor.B.setSpeed(speed);
        Motor.C.setSpeed(speed);
        Motor.B.forward();
        Motor.C.forward();
        Utils.Sleep(time);
    }

    static void turn(int angle) {
        int speed = 360;
        int sleep = 1500 * angle / 360;
        Motor.B.setSpeed(speed);
        Motor.C.setSpeed(speed);
        Motor.B.forward();
        Motor.C.backward();
        Utils.Sleep(sleep);
    }

    private ActionParameters parameters = new ActionParameters();
    @Override
    public ActionParameters GetParameters() {
        return parameters;
    }

    @Override
    public void Execute() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
