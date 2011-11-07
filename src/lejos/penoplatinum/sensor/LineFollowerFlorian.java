package penoplatinum.sensor;

import penoplatinum.Utils;
import penoplatinum.movement.RotationMovement;
import penoplatinum.sensor.WrappedLightSensor.Color;

/**
 * This class is meant to be used as a controller for line following. It is based upon the fact we can use a light intensity sensor
 * 
 * @author Florian
 */
public class LineFollowerFlorian {

    final RotationMovement agent = new RotationMovement();
    int rightDirection;
    double LineThresHold;
    double platformThresHold;
    private boolean lastLineWasWhite;
    boolean LineType;
    private int WHITEVAL;
    private int BROWNVAL;
    private int BLACKVAL;
    private WrappedLightSensor lightSensor;

    public LineFollowerFlorian(WrappedLightSensor sensor) {
        this.lightSensor = sensor;
    }

    public void changeSpeed(int speed) {
        agent.SPEEDFORWARD = speed;
        agent.SPEEDTURN = 160;
    }

    public void ActionLineFollower() {
        changeSpeed(280);

        while (true) {
            if (lightSensor.isColor(Color.Brown)) {
                LineFinder();
            }
            agent.MoveStraight(1.0, false);
            lastLineWasWhite = lightSensor.isColor(Color.White);
            Utils.Sleep(100);
        }
    }

    public void LineFinder() {
        int[] rotates = new int[]{-3, 8, -15, 50, -95, 50 + 120, 240, 720};
        int pos = 0;
        System.out.println("FindLine");
        agent.Stop();
        while (lightSensor.isColor(Color.Brown)) {
            if (stopped()) {
                agent.TurnOnSpotCCW(rotates[pos++]);
            }
        }
        System.out.println("FoundLine");
    }

    public boolean stopped() {
        return !agent.motorLeft.isMoving() && !agent.motorRight.isMoving();
    }
}
