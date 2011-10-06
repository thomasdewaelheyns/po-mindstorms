
import java.io.DataInputStream;
import java.io.DataOutputStream;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;


/**
 *
 * @author MHGameWork
 */
public class ActionFrameworkServer {
    private DataInputStream dis;
    private DataOutputStream dos;

    public boolean initializeBluetooth() {
        BTConnection conn = Bluetooth.waitForConnection(5000, NXTConnection.PACKET);
        //TODO: test
        if (conn == null) return false;
        dis = conn.openDataInputStream();
        DataInputStream str = dis;
        dos = conn.openDataOutputStream();
        DataOutputStream stro = dos;
        
        sendActions();
        
        return true;
        
        
    }
    
    private void sendActions()
    {
        
    }
}
