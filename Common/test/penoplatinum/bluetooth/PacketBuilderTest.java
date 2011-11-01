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
public class PacketBuilderTest {

    public PacketBuilderTest() {
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

    
    
    
    

}
