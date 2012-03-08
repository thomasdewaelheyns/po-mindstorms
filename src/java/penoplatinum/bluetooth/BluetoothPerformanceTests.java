/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.bluetooth;

import java.io.IOException;
import penoplatinum.Utils;

/**
 *
 * @author: Team Platinum
 */
public class BluetoothPerformanceTests {

    /**
     * Returns true on success
     * @param conn
     * @return
     * @throws IOException 
     */
    public boolean testSendSpeed_Send(IConnection conn) throws IOException {
        int testPacket = 5988695;
        int completePacket = 97498873;
        int resultPacket = 16246876;

        QueuedPacketTransporter t = new QueuedPacketTransporter(conn);

        conn.RegisterTransporter(t, testPacket);
        conn.RegisterTransporter(t, completePacket);
        conn.RegisterTransporter(t, resultPacket);

        byte[] data = new byte[100];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) i;
        }

        int sendTime = 5000;
        int[] intervals = new int[]{1000, 500, 400, 300, 200, 100, 50, 40, 30, 20, 10, 5, 2, 1};
        //int[] intervals = new int[]{2};

        boolean failed = false;


        for (int i = 0; i < intervals.length; i++) {
            int interval = intervals[i];
            Utils.Log("Interval: " + interval);
            Utils.Sleep(1000);


            int numPackets = sendTime / interval;

            for (int j = 0; j < numPackets; j++) {
                data[0] = (byte) (j % 256);
                t.getSendStream().write(data, 0, data.length);
                t.SendPacket(testPacket);
                Utils.Sleep(interval);
            }
            Utils.Sleep(2000);
            Utils.Log("Complete!");
            t.SendPacket(completePacket);

            int packetID = t.ReceivePacket();
            if (packetID != resultPacket) {
                Utils.Log("PROTOCOL ERROR!");
            }

            int counter = t.getReceiveStream().readInt();

            Utils.Log("Got: " + counter + " Expected:" + numPackets);
            if (counter != numPackets) {
                failed = true;
            }

        }

        return !failed;
    }

    public void testSendSpeed_Receive(IConnection conn) throws IOException {
        int testPacket = 5988695;
        int completePacket = 97498873;
        int resultPacket = 16246876;


        QueuedPacketTransporter t = new QueuedPacketTransporter(conn);

        conn.RegisterTransporter(t, testPacket);
        conn.RegisterTransporter(t, completePacket);
        conn.RegisterTransporter(t, resultPacket);


        int counter = 0;

        while (true) {
            int packetID = t.ReceivePacket();

            if (packetID == testPacket) {
                int temp = t.getReceiveStream().read();
                if (temp != (counter % 256)) {
                    Utils.Log("Packet corrupted! " + temp + " " + counter % 256);
                    break;
                }
                //Utils.Log("Received"+counter);
                counter++;

            } else if (packetID == completePacket) {
                Utils.Log("Sending Result" + counter);
                t.getSendStream().writeInt(counter);
                t.SendPacket(resultPacket);
                counter = 0;
            }
        }
    }
}
