package PenoPlatinum;
import lejos.nxt.Motor;

/**
 *
 * @author MHGameWork
 */
public class VeelhoekAction implements IAction {

    static
    {
        //TODO: fix
        ActionContainer.AddAction(new VeelhoekAction());
    }
    
    public static void main(String[] args) {
        veelhoek(3, 1000);
    }

    static void veelhoek(int number, int time) {
        int angle = 180 * (number - 2) / number;
        for (int i = 0; i < number; i++) {
            forward(time);
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
            turn(angle);
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
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
        try {
            Thread.sleep(time);
        } catch (Exception e) {
        }
    }

    static void turn(int angle) {
        int speed = 360;
        int sleep = 1500 * angle / 360;
        Motor.B.setSpeed(speed);
        Motor.C.setSpeed(speed);
        Motor.B.forward();
        Motor.C.backward();
        try {
            Thread.sleep(sleep);
        } catch (Exception e) {
        }
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
