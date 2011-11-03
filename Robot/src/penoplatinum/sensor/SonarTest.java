/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.sensor;

import java.io.IOException;
import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.UltrasonicSensor;
import penoplatinum.Utils;
import penoplatinum.bluetooth.PacketTransporter;
import penoplatinum.bluetooth.RobotBluetoothConnection;
import penoplatinum.movement.RotationMovement;

/**
 *
 * @author MHGameWork
 */
public class SonarTest {

    private PacketTransporter samplePacketTransporter;
    private UltrasonicSensor sens;
    private RotationMovement mov;
    private final int samplePacket = 9898286;
    private final int startPacket = 85765456;

    public void distanceTest(UltrasonicSensor sens) throws IOException {
        this.sens = sens;

        RobotBluetoothConnection conn = new RobotBluetoothConnection();
        conn.initializeConnection();
        Utils.EnableRemoteLogging(conn);

        Motor.A.setSpeed(50);

        int[] rotates = new int[]{-45, -30, -15, -10, -5, 0, 5, 10, 15, 30, 45};

        while (!Button.ESCAPE.isPressed()) {
            if (Button.RIGHT.isPressed()) {
                orientSonarHead(sens, Motor.A);
            } else if (Button.ENTER.isPressed()) {


                for (int j = 0; j < 5; j++) {
                    Utils.Log("Start test");
                    Motor.A.rotate(rotates[0]);
                    for (int i = 0; i < rotates.length; i++) {
                        if (i != 0) {
                            Motor.A.rotate(rotates[i] - rotates[i - 1]);
                        }
                        Utils.Sleep(800);
                        int dist = sens.getDistance();
                        Utils.Log(rotates[i] + "," + dist);
                    }
                    Motor.A.rotate(-rotates[rotates.length - 1]);
                }


            }
        }




    }

    public static void testGetDistanceDuration(UltrasonicSensor sens) {



        while (!Button.ESCAPE.isPressed()) {
            if (Button.ENTER.isPressed()) {
                Utils.Log("Starting!");
                int count = 100000;
                long startTime = System.nanoTime();
                for (int i = 0; i < count; i++) {
                    sens.getDistance();
                }

                long endTime = System.nanoTime();
                Utils.Log("Delta: " + (endTime - startTime));
                Utils.Log("Average: " + (endTime - startTime) / (double) count);

            }
        }


    }

    public void calibrateSonar(UltrasonicSensor sens) throws IOException {
        this.sens = sens;
        mov = new RotationMovement();

        orientSonarHead(sens, Motor.A);

        RobotBluetoothConnection conn = new RobotBluetoothConnection();
        conn.initializeConnection();
        samplePacketTransporter = new PacketTransporter(conn);
        PacketTransporter t = samplePacketTransporter;
        conn.RegisterTransporter(t, samplePacket);
        conn.RegisterTransporter(t, startPacket);



        while (!Button.ESCAPE.isPressed()) {
            if (Button.ENTER.isPressed()) {
                t.SendPacket(startPacket);
                driveAndSample(20);
            }
        }




    }

    public void driveAndSample(int minDistance) throws IOException {
        PacketTransporter t = samplePacketTransporter;
        int sensor = Integer.MAX_VALUE;
        float tacho;
        while (minDistance < sensor) {
            mov.MoveStraight(2, false);
            tacho = mov.getAverageTacho();
            sensor = sens.getDistance();
            t.getSendStream().writeFloat(tacho);
            t.getSendStream().writeInt(sensor);
            t.SendPacket(samplePacket);


            Utils.Sleep(500);


        }
        mov.Stop();
    }

    public void orientSonarHead(UltrasonicSensor sens, Motor m) {
        rotateSonarToClosest(sens, m, -180, 180, 200);
        rotateSonarToClosest(sens, m, -30, 30, 40);
        rotateSonarToClosest(sens, m, -10, 10, 10);

    }

    public void rotateSonarToClosest(UltrasonicSensor sens, Motor m, int startAngle, int stopAngle, int speed) {
        int minTacho = Integer.MIN_VALUE;
        int maxTacho = Integer.MIN_VALUE;
        int minDistance = Integer.MAX_VALUE;
        m.setSpeed(speed);
        m.rotate(startAngle);
        m.setSpeed(speed);
        m.rotate(stopAngle - startAngle, true);
        while (m.isMoving()) {
            int dist = sens.getDistance();
            int tacho = m.getTachoCount();
            if (dist < minDistance) {
                minDistance = dist;
                minTacho = tacho;
            }
            if (dist == minDistance) {
                maxTacho = tacho;
            }
        }

        if (minTacho == Integer.MIN_VALUE) {
            Utils.Log("PROBLEM!");
            return;
        }
        m.setSpeed(speed);
        m.rotate((minTacho + maxTacho) / 2 - m.getTachoCount());
    }
}
