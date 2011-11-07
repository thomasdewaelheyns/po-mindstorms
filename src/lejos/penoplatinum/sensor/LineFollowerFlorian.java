package penoplatinum.sensor;

import penoplatinum.Utils;
import penoplatinum.bluetooth.PacketTransporter;
import penoplatinum.movement.RotationMovement;
import penoplatinum.sensor.WrappedLightSensor.Color;

/**
 * This class is meant to be used as a controller for line following. It is based upon the fact we can use a light intensity sensor
 * 
 * @author Florian
 */
public class LineFollowerFlorian {

    final RotationMovement movement = new RotationMovement();
    int rightDirection;
    double LineThresHold;
    double platformThresHold;
    private boolean lastLineWasWhite;
    boolean LineType;
    private int WHITEVAL;
    private int BROWNVAL;
    private int BLACKVAL;
    private WrappedLightSensor lightSensor;
    private final PacketTransporter commandTransporter;

    public LineFollowerFlorian(WrappedLightSensor sensor, PacketTransporter commandTransporter) {
        this.lightSensor = sensor;
        this.commandTransporter = commandTransporter;
    }

    public void changeSpeed(int speed) {
        movement.SPEEDFORWARD = speed;
        movement.SPEEDTURN = 160;
    }

    public void ActionLineFollower() {
        changeSpeed(280);

        while (commandTransporter.ReceiveAvailablePacket() == -1) {
            if (lightSensor.isColor(Color.Brown)) {
                findLine();
            }
            movement.MoveStraight(1.0, false);
            lastLineWasWhite = lightSensor.isColor(Color.White);
            Utils.Sleep(100);
        }
        movement.Stop();
    }

    public void findLine() {
        int[] rotates = new int[]{-10, 20, -130, 240, -720};
        int pos = 0;
        Utils.Log("FindLine");
        movement.Stop();
        while (lightSensor.isColor(Color.Brown)) {
            if (stopped()) {
                movement.TurnOnSpotCCW(rotates[pos++], false);
            }
        }
        Utils.Log("FoundLine");
    }

    public boolean stopped() {
        return !movement.motorLeft.isMoving() && !movement.motorRight.isMoving();
    }
}
