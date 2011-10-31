package penoplatinum.bluetooth;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTConnector;
import penoplatinum.Utils;

public class PCBluetoothConnection implements IConnection {

    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    private NXTComm open;
    private HashMap<Integer, IPacketTransporter> listenerMap = new HashMap<Integer, IPacketTransporter>();
    private PacketBuilder builder;
    /**
     * Readonly, only write in main thread
     */
    private boolean receiving;

    public void initializeConnection() {
        while (!connect()) {
            Utils.Log("Connection failed, trying again");
            Utils.Sleep(1000);
        }
        Utils.Log("Connected!");
        /*try {
            outputStream.writeInt(82);
            outputStream.flush();
        } catch (IOException ex) {
            Logger.getLogger(PCBluetoothConnection.class.getName()).log(Level.SEVERE, null, ex);
        }*/

        
        // Connected to NXJ, perform packet ID synchronization here (possible optimization)

        createPacketBuilder();

        builder.startReceiving();

    }

    private void createPacketBuilder() {
        builder = new PacketBuilder(outputStream, inputStream, new IPacketReceiver() {

            @Override
            public void onPacketReceived(int packetIdentifier, byte[] dgram, int size) {
                IPacketTransporter t = listenerMap.get(packetIdentifier);
                if (t == null) {
                    Utils.Log("Packet discarded because no transporter is registered for this type! (" + packetIdentifier + ")");
                }
                t.onPacketReceived(packetIdentifier, dgram, 0, size);

            }
        });
    }

    @Override
    public void RegisterTransporter(IPacketTransporter l, int packetIdentifier) {
        if (listenerMap.containsKey(packetIdentifier)) {
            throw new RuntimeException("A listener has already been created with given packetIdentifer");
        }

        listenerMap.put(packetIdentifier, l);

    }

    @Override
    public void SendPacket(IPacketTransporter transporter, int packetIdentifier, byte[] dgram) {
        if (listenerMap.get(packetIdentifier) != transporter) {
            throw new RuntimeException("Unauthorized packet id!");
        }
        builder.sendPacket(packetIdentifier, dgram);
        //TODO: flush??


    }

    private boolean connect() {
        try {
            NXTConnector conn = new NXTConnector();
            boolean connected = conn.connectTo(NXTComm.PACKET);
            open = (connected ? conn.getNXTComm() : null);
            
            outputStream = (connected ? new DataOutputStream(open.getOutputStream()) : null);
            inputStream = (connected ? new DataInputStream(open.getInputStream()) : null);
            return connected;
        } catch (Exception e) {
            Utils.Log(e.toString());
            return false;
        }

    }

    private void close() {
        try {
            open.close();
        } catch (IOException ex) {
        }
    }
}
