/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.client;

import java.io.File;
import penoplatinum.Utils;
import penoplatinum.bluetooth.PCBluetoothConnection;
import penoplatinum.bluetooth.RemoteFileLogger;

/**
 *
 * @author: Team Platinum
 */
public class MainLogger {

    public static void main(String[] args) {
        PCBluetoothConnection conn = new PCBluetoothConnection();
        conn.initializeConnection();


        RemoteFileLogger logger = new RemoteFileLogger(conn, "RobotLog", new File("logs"));
        logger.startLogging();

        while (true) {
            Utils.Sleep(1000);
        }
    }
}
