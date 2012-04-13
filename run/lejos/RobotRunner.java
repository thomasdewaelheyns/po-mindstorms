/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum;

import penoplatinum.util.Utils;
import lejos.nxt.Button;
import lejos.nxt.Sound;
import lejos.nxt.comm.Bluetooth;
import penoplatinum.bluetooth.RobotBluetoothConnection;

/**
 *
 * @author: Team Platinum
 */
public class RobotRunner {

    public static void Run(Runnable runnable) {
        RobotRunner runner = new RobotRunner(runnable);
        runner.start();
    }
    private final Runnable runnable;
    RobotBluetoothConnection connection;

    public RobotRunner(Runnable runnable) {
        this.runnable = runnable;
        connection = new RobotBluetoothConnection();
    }

    private void start() {

        Utils.EnableRemoteLogging(connection,"RobotRunner");



        Thread t = new Thread(new Runnable() {

            public void run() {
                Thread bluetoothThread = getBluetoothConnectThread();
                bluetoothThread.start();

                while (!connection.isConnected()) {
                    Utils.Sleep(100);
                }
                Utils.Log("Hi!");
                runnable.run();

            }
        });

        t.setDaemon(true);
        t.run();



        //Utils.Log("WaitForDone!");        Button.waitForPress();
        while (!Button.ESCAPE.isPressed() && t.isAlive()) {
            Utils.Sleep(500);
        }
        System.exit(0);
    }

    private Thread getBluetoothConnectThread() {



        Thread t = new Thread(new Runnable() {

            public void run() {
                while (true) {
                    while (!connection.isConnectionCorrupt() && connection.isConnected()) {
                        Utils.Sleep(1000);
                    }
                    byte[] data = Bluetooth.getConnectionStatus();
                    System.out.println(data.length);
                    for (int i = 0; i < data.length; i++) {
                        System.out.println(data[i]);
                    }
                    Bluetooth.cancelInquiry(); // Maybe not needed
                    connection.close();
                    Bluetooth.reset();
                    connection.initializeConnection();
                    playConnectedSound();
                }


            }

            private boolean needsReconnect() {
                return connection.isConnectionCorrupt() || !connection.isConnected();
            }

            private void playConnectedSound() {
                Sound.playNote(Sound.PIANO, 440, 500);
                Sound.playNote(Sound.PIANO, 880, 500);
                Sound.playNote(Sound.PIANO, 784, 250);
                Sound.playNote(Sound.PIANO, 880, 250);
            }
        });
        t.setName("BlueConn");
        t.setDaemon(true);
        return t;
    }
}
