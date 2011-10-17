package penoplatinum.bluetooth;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.util.HashMap;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTConnector;
import penoplatinum.Utils;

public class BluetoothCommunicatorPC {

    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    private NXTComm open;
    private HashMap<Integer, BluetoothPacketTransporter> listenerMap = new HashMap<Integer, BluetoothPacketTransporter>();

    public void initializeConnection() {
        while (!connect()) {
            Utils.Log("Connection failed, trying again");
            Utils.Sleep(1000);
        }
        // Connected to NXJ, perform packet ID synchronization here

    }

    public BluetoothPacketTransporter RegisterTransporter(int packetIdentifier) {
        BluetoothPacketTransporter l = new BluetoothPacketTransporter();
        if (listenerMap.containsKey(packetIdentifier)) {
            throw new RuntimeException("A listener has already been created with given packetIdentifer");
        }

        listenerMap.put(packetIdentifier, l);

        return l;
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

    private  void close() {
        try {
            open.close();
        } catch (IOException ex) {
        }
    }

    private boolean sendCatch(int n) {
        try {
            send(n);
        } catch (IOException ex) {
            System.out.println("IO Exception.");
            return false;
        }
        return true;
    }

    private void send(int n) throws IOException {
        System.out.println("send: " + n);
        outputStream.writeInt(n);
        outputStream.flush();
    }
}
