package penoplatinum.bluetooth;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTConnector;
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
public class PCBluetoothTest {
    
    public PCBluetoothTest() {
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
    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    private NXTComm open;
    /**
     * Readonly, only write in main thread
     */
    private boolean receiving;
    
    @Test
    public void testBasicBluetooth() {
        while (!connect()) {
            Utils.Log("Connection failed, trying again");
            Utils.Sleep(1000);
        }
        
        Utils.Log("Connected!");
        // Connected to NXJ, perform packet ID synchronization here (possible optimization)

        while (true) {
            try {
                outputStream.writeInt(43);
                outputStream.flush();
                
                System.out.println("Received: " + inputStream.readInt());
                
                
            } catch (IOException ex) {
                Logger.getLogger(PCBluetoothTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
    }
    
    private boolean connect() {
        try {
            NXTConnector conn = new NXTConnector();
            boolean connected = conn.connectTo(NXTComm.PACKET);
            //open = (connected ? conn.getNXTComm() : null);

            outputStream = (connected ? conn.getDataOut() : null);
            inputStream = (connected ? conn.getDataIn() : null);
            return connected;
        } catch (Exception e) {
            Utils.Log(e.toString());
            return false;
        }
        
    }
    
    @Test
    public void testBluetoothButtonSend() {
        int buttonPacketId = 5988695;
        
        PCBluetoothConnection conn = new PCBluetoothConnection();
        
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
    
    @Test
    public void testSendSpeed() throws IOException {
        PCBluetoothConnection conn = new PCBluetoothConnection();
        Utils.Log("Initializeing");
        conn.initializeConnection();
        Utils.Log("Done");
        penoplatinum.bluetooth.BluetoothPerformanceTests test = new BluetoothPerformanceTests();
        test.testSendSpeed_Send(conn);
        
    }
}
