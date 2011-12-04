/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.sensor;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;
import org.junit.Test;
import penoplatinum.Utils;
import penoplatinum.bluetooth.PCBluetoothConnection;
import penoplatinum.bluetooth.PacketTransporter;

/**
 *
 * @author: Team Platinum
 */
public class SonarTest {

    @Test
    public void testCalibrateSonar() throws IOException {
        int samplePacket = 9898286;
        int startPacket = 85765456;

        File file = new File("testOutput");
        file.mkdir();

        //file.mkdirs();
        //file.createNewFile();

        PrintStream fs = null;

        int testNum = 0;

        PCBluetoothConnection conn = new PCBluetoothConnection();
        conn.initializeConnection();
        PacketTransporter t = new PacketTransporter(conn);
        conn.RegisterTransporter(t, samplePacket);
        conn.RegisterTransporter(t, startPacket);

        Scanner s = new Scanner(System.in);


        while (!s.hasNext()) {
            int packetID = t.ReceivePacket();
            if (packetID == samplePacket) {
                float tacho = t.getReceiveStream().readFloat();
                int sensor = t.getReceiveStream().readInt();
                Utils.Log(tacho + "");
                Utils.Log(sensor + "");
                fs.println(tacho + ", " + sensor);
            } else if (packetID == startPacket) {
                testNum++;
                if (fs != null) {
                    fs.close();
                }
                file = new File("testOutput/testCalibrateSonar" + testNum + ".txt");
                fs = new PrintStream(file);

                fs.println("Test " + testNum);
                fs.println("Start Writing sonar data.");
                //new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
            }

        }
        if (fs != null) {
            fs.close();
        }
    }
}
