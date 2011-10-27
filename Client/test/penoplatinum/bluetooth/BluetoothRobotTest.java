package penoplatinum.bluetooth;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import penoplatinum.Utils;

/**
 *
 * @author MHGameWork
 */
public class BluetoothRobotTest {


    public BluetoothRobotTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testBluetoothButtonSend() {
        int buttonPacketId = 5988695;

        BluetoothPCConnection conn = new BluetoothPCConnection();

        PacketTransporter t = new PacketTransporter(conn);

        conn.RegisterTransporter(t, buttonPacketId);

        conn.initializeConnection();

        while (true) {
            int packetID = t.ReceivePacket();
            if (packetID == buttonPacketId) {
                Utils.Log("Button packet received!");
            } else {
                Utils.Log("Unknown packet received");
            }
            try {
                Utils.Log(t.getReceiveStream().readDouble() + "");
            } catch (IOException ex) {
                Utils.Log(ex.toString());
            }

        }



    }
}
