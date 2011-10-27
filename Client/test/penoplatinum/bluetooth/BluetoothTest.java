package penoplatinum.bluetooth;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import penoplatinum.Utils;
import static org.junit.Assert.*;

/**
 *
 * @author MHGameWork
 */
public class BluetoothTest {

    SimulatedConnection ca;
    SimulatedConnection cb;

    public BluetoothTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        ca = new SimulatedConnection();
        cb = new SimulatedConnection();
        ca.setEndPoint(cb);
        cb.setEndPoint(ca);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testRegisterTransporter() {
        int packetA = 23;

        ca.RegisterTransporter(new PacketTransporter(ca), packetA);

    }

    @Test
    public void testSendReceive() throws IOException {
        int packetA = 23;
        String msg = "Hellow Universe!";

        PacketTransporter tA = new PacketTransporter(ca);
        PacketTransporter tB = new PacketTransporter(cb);

        ca.RegisterTransporter(tA, packetA);
        cb.RegisterTransporter(tB, packetA);


        tA.getSendStream().writeUTF(msg);
        tA.SendPacket(packetA);

        assertEquals(packetA, tB.ReceivePacket());
        assertEquals(msg, tB.getReceiveStream().readUTF());
        assertEquals(0, tB.getReceiveStream().available());



    }

    @Test
    public void testSendReceiveMultiple() throws IOException {
        int packetA = 235432357;
        int packetB = 780897687;
        String msg = "Hellow Universe!";
        String msg2 = "Hellow Humans!";

        PacketTransporter tA = new PacketTransporter(ca);
        PacketTransporter tB = new PacketTransporter(cb);

        PacketTransporter sA = new PacketTransporter(ca);
        PacketTransporter sB = new PacketTransporter(cb);

        ca.RegisterTransporter(tA, packetA);
        cb.RegisterTransporter(tB, packetA);

        ca.RegisterTransporter(sA, packetB);
        cb.RegisterTransporter(sB, packetB);


        tA.getSendStream().writeUTF(msg);
        tA.SendPacket(packetA);

        assertEquals(packetA, tB.ReceivePacket());
        assertEquals(msg, tB.getReceiveStream().readUTF());
        assertEquals(0, tB.getReceiveStream().available());



        sB.getSendStream().writeUTF(msg2);
        sB.SendPacket(packetB);

        assertEquals(packetB, sA.ReceivePacket());
        assertEquals(msg2, sA.getReceiveStream().readUTF());
        assertEquals(0, sA.getReceiveStream().available());



    }

    @Test
    public void testPacketBuilder() throws IOException {

        final byte[] data = new byte[]{3, 1, 4, 1, 5, 9, 2};

        PipedOutputStream o = new PipedOutputStream();
        PipedInputStream i = new PipedInputStream(o);

        final boolean[] success = new boolean[1];

        DataOutputStream outputStream = new DataOutputStream(o);
        DataInputStream inputStream = new DataInputStream(i);

        final IPacketReceiver iPacketReceiver = new IPacketReceiver() {

            @Override
            public void onPacketReceived(int packetIdentifier, byte[] dgram, int size) {
                if (size == 0) {
                    return; //for second packet
                }
                assertEquals(3, packetIdentifier);
                assertEquals(data.length, size);
                for (int j = 0; j < size; j++) {
                    assertEquals(data[j], dgram[j]);
                }
                success[0] = true;

            }
        };

        PacketBuilder builder = new PacketBuilder(outputStream, inputStream, iPacketReceiver);

        builder.startReceiving();

        builder.sendPacket(3, data);
        builder.sendPacket(5, new byte[0]);
        //builder.sendPacket(5, new byte[0]);
        //builder.sendPacket(5, new byte[300]);

        Utils.Sleep(2000);

        assertTrue(success[0]);



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
