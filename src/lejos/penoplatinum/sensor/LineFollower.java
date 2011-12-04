package penoplatinum.sensor;

import penoplatinum.Utils;
import penoplatinum.bluetooth.PacketTransporter;
import penoplatinum.movement.RotationMovement;
import penoplatinum.sensor.WrappedLightSensor.Color;

/**
 * This class is meant to be used as a controller for line following. It is based upon the fact we can use a light intensity sensor
 * 
 * @author: Team Platinum
 */
public class LineFollower {

    final RotationMovement movement = new RotationMovement();
    int rightDirection;
    double LineThresHold;
    double platformThresHold;
    boolean LineType;
    private WrappedLightSensor lightSensor;
    private final PacketTransporter commandTransporter;

    public LineFollower(WrappedLightSensor sensor, PacketTransporter commandTransporter) {
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
            movement.driveDistance(1.0);
            Utils.Sleep(100);
        }
        movement.stop();
    }

    public void findLine() {
        int[] rotates = new int[]{-10, 20, -130, 240, -720};
        int pos = 0;
        Utils.Log("FindLine");
        movement.stop();
        while (lightSensor.isColor(Color.Brown)) {
            if (movement.isStopped()) {
                movement.turnAngle(rotates[pos++]);
            }
        }
        Utils.Log("FoundLine");
    }

}
