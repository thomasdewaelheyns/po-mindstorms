package penoplatinum.demo;

import java.io.IOException;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import penoplatinum.RobotRunner;
import penoplatinum.Utils;
import penoplatinum.barcode.BarcodeDemoThread;
import penoplatinum.barcode.BarcodeReader;
import penoplatinum.bluetooth.PacketTransporter;
import penoplatinum.bluetooth.RobotBluetoothConnection;
import penoplatinum.movement.RotationMovement;
import penoplatinum.sensor.LineFollowerFlorian;
import penoplatinum.sensor.MuurvolgerPerpendicular;
import penoplatinum.sensor.WrappedLightSensor;
import penoplatinum.ui.UIView;

/**
 *
 * @author MHGameWork
 */
public class SensorDemo {

    public static void main(String[] args) throws Exception {

        RobotRunner.Run(new Runnable() {

            public void run() {

                Utils.Log("Demo 2 started!");

                RobotBluetoothConnection connection = new RobotBluetoothConnection();
                connection.initializeConnection();

                Utils.EnableRemoteLogging(connection, "SensorDemo");
                
                PacketTransporter commandTransporter = new PacketTransporter(connection);
                connection.RegisterTransporter(commandTransporter, UIView.COMMAND);

                WrappedLightSensor readout = new WrappedLightSensor(connection);

                while (true) {
                    commandTransporter.ReceivePacket();

                    String cmd = "";
                    try {
                        cmd = commandTransporter.getReceiveStream().readLine();
                    } catch (IOException ex) {
                        Utils.Log("CHIWAWAAA!");
                    }

                    Utils.Log("Received command: " + cmd);

                    if (cmd.equals("calibrate")) {
                        Utils.Log("Starting calibrate.");
                        readout.calibrate();
                    } else if (cmd.equals("muurvolger")) {
                        final UltrasonicSensor sens = new UltrasonicSensor(SensorPort.S2);
                        final RotationMovement mov = new RotationMovement();

                        mov.SPEEDFORWARD = 250;
                        mov.SPEEDTURN = 120;

                        MuurvolgerPerpendicular v = new MuurvolgerPerpendicular(sens, mov, Motor.A, connection);
                        v.run();
                    } else if (cmd.equals("lijnvolger")) {
                        Utils.Log("Starting lijnvolger.");
                        LineFollowerFlorian florian = new LineFollowerFlorian(readout);
                        florian.ActionLineFollower();
                    } else if (cmd.equals("barcode")) {
                        BarcodeDemoThread t = new BarcodeDemoThread(new BarcodeReader(readout), Boolean.TRUE);
                        t.start();
                    } else {
                        Utils.Log("Unknown command received! " + cmd);
                    }
                }
            }
        });

    }
}
