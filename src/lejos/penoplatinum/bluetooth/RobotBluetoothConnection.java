package penoplatinum.bluetooth;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;
import penoplatinum.Utils;

/**
 * Responsible for sending and receiving bluetooth packets on the PC
 * TODO: fix threading
 * 
 */
public class RobotBluetoothConnection implements IConnection {

    BTConnection conn;
    DataInputStream stri;
    DataOutputStream stro;
    //private HashMap<Integer, BluetoothPacketTransporter> listenerMap = new HashMap<Integer, BluetoothPacketTransporter>();
    private ArrayList<TransporterItem> transporterItems = new ArrayList<TransporterItem>();
    private PacketBuilder builder;
    private boolean connected;

    public boolean isConnected() {
        return connected;
    }

    public boolean isConnectionCorrupt() {
        if (builder == null) {
            return false;
        }
        return builder.isErrorOccured();
    }

    public RobotBluetoothConnection() {
    }

    public void initializeConnection() {
        if (isConnected()) {
            Utils.Log("Already connected!");
            return;
        }
        while (!connect()) {
            Utils.Log("Connection failed, trying again");
            Utils.Sleep(1000);
        }
        // Connected to NXJ, perform packet ID synchronization here

        IPacketReceiver r = null;


        builder = new PacketBuilder(stro, stri, new IPacketReceiver() {

            public void onPacketReceived(int packetIdentifier, byte[] dgram, int size) {
                IPacketTransporter t = findTransporterByPacketIdentifier(packetIdentifier);
                if (t == null) {
                    Utils.Log("Unkown packet type received! (" + packetIdentifier + ")");
                    return;
                }

                t.onPacketReceived(packetIdentifier, dgram, 0, size);
            }

            public void onError(Exception ex) {
                Utils.Log("PacketBuilder error!");

                //TODO
            }
        });

        builder.startReceiving();


        connected = true;

    }

    private boolean connect() {
        Utils.Log("Connecting.");
        conn = Bluetooth.waitForConnection(3000, NXTConnection.PACKET);
        if (conn == null) {
            stri = null;
            stro = null;

            return false;
        }
        stri = conn.openDataInputStream();
        stro = conn.openDataOutputStream();
        Utils.Log("Connected: " + conn.getAddress());
        return true;
    }

    public void close() {
        connected = false;
        if (builder != null) {
            builder.stopReceiving();
        }
        builder = null;
        if (conn != null) {
            conn.close();
        }
        conn = null;
    }

    public void RegisterTransporter(IPacketTransporter transporter, int packetIdentifier) {
        synchronized (this) {
            for (int i = 0; i < transporterItems.size(); i++) {
                if (transporterItems.get(i).packetIdentifier == packetIdentifier) {
                    Utils.Log("A transporter has already been created with given packetIdentifer");
                    return;
                }

            }

            TransporterItem item = new TransporterItem(packetIdentifier, transporter);
            transporterItems.add(item);
            Utils.Log(transporterItems.size() + "");
        }
    }

    public void SendPacket(IPacketTransporter transporter, int packetIdentifier, byte[] dgram) {

        if (!isConnected()) {
            Utils.Log("No connection! Packet discarded");
            return;
        }
        //TODO: remove security check for speed
        IPacketTransporter t = null;

        t = findTransporterByPacketIdentifier(packetIdentifier);

        if (t == null) {
            Utils.Log("Unknown packet identifier!");
            return;
        }
        if (t != transporter) {
            Utils.Log("Transporter is not allowed to send packets with given identifier");
            return;
        }

        builder.sendPacket(packetIdentifier, dgram);



    }

    /**
     * Thread safe!
     * @param packetIdentifier
     * @return 
     */
    private IPacketTransporter findTransporterByPacketIdentifier(int packetIdentifier) {
        synchronized (this) {
            for (int i = 0; i < transporterItems.size(); i++) {
                final TransporterItem item = transporterItems.get(i);
                if (item.getPacketIdentifier() == packetIdentifier) {
                    return item.getTransporter();
                }
            }
            return null;
        }


    }

    private class TransporterItem {

        private int packetIdentifier;
        private IPacketTransporter transporter;

        public TransporterItem(int packetIdentifier, IPacketTransporter transporter) {
            this.packetIdentifier = packetIdentifier;
            this.transporter = transporter;
        }

        public int getPacketIdentifier() {
            return packetIdentifier;
        }

        public IPacketTransporter getTransporter() {
            return transporter;
        }
    }
}
