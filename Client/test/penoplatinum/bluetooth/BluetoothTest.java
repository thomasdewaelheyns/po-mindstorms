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
import static org.junit.Assert.*;

/**
 *
 * @author MHGameWork
 */
public class BluetoothTest {

    SimpleConnection ca;
    SimpleConnection cb;

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
        ca = new SimpleConnection();
        cb = new SimpleConnection();
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
}
