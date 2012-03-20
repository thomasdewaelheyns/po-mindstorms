package penoplatinum.bluetooth;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTCommLogListener;
import lejos.pc.comm.NXTConnector;
import lejos.pc.comm.NXTInfo;
import penoplatinum.util.Utils;

/**
 * 
 * @author: Team Platinum
 */
public class PCBluetoothConnection implements IConnection {
    private final String ROBOT_NAME = Utils.PROPERTIES.getProperty("robot.name");

    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    private NXTConnector open;
    private HashMap<Integer, IPacketTransporter> listenerMap = new HashMap<Integer, IPacketTransporter>();
    private PacketBuilder builder;
    /**
     * Readonly, only write in main thread
     */
    private boolean receiving;
    private boolean connected;

    public boolean isConnected() {
        return connected;
    }

    public void initializeConnection() {
        if (isConnected()) {
            close();
        }

        if (open != null) {
            try {
                open.close();
            } catch (IOException ex) {
                Logger.getLogger(PCBluetoothConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        while (!connect()) {
            Utils.Log("Connection failed, trying again");
            Utils.Sleep(1000);
            Utils.Log("Restarting connection");
        }
        Utils.Log("Connected!");

        // Connected to NXJ, perform packet ID synchronization here (possible optimization)




        createPacketBuilder();

        builder.startReceiving();
        connected = true;

    }

    private void createPacketBuilder() {

        if (builder != null) {
            builder.stopReceiving();
        }


        builder = new PacketBuilder(outputStream, inputStream, new IPacketReceiver() {

            @Override
            public void onPacketReceived(int packetIdentifier, byte[] dgram, int size) {
                IPacketTransporter t = listenerMap.get(packetIdentifier);
                if (t == null) {
                    Utils.Log("Packet discarded because no transporter is registered for this type! (" + packetIdentifier + ")");
                    return;
                }
                t.onPacketReceived(packetIdentifier, dgram, 0, size);

            }

            @Override
            public void onError(Exception ex) {
                //WARNING!!! DANGEROUS MULTITHREADING, FIX USING DEDICATED CONNECT THREAD
                //initializeConnection();

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

            outputStream = null;
            inputStream = null;

            NXTConnector conn = new NXTConnector();
            NXTInfo[] infos = conn.search(null, null, NXTCommFactory.ALL_PROTOCOLS);

            NXTInfo lejosInfo = null;
            for (NXTInfo inf : infos) {
                if (inf.name.equals(ROBOT_NAME)) {
                    lejosInfo = inf;

                }
            }


            if (lejosInfo != null) {
            } else if (infos.length == 1) {
                lejosInfo = infos[0];
                Utils.Log("Default robot not found, picking only found robot:"+lejosInfo.name);
            } else if (infos.length > 1) {
                Utils.Log("Multiple possible connections found, aborting!");
                return false;
            }

            if (lejosInfo == null) {
                Utils.Log("No robot found!");
                return false;
            }

            lejos.pc.comm.NXTCommLogListener listener = new NXTCommLogListener() {

                @Override
                public void logEvent(String string) {
                    Utils.Log(string);
                }

                @Override
                public void logEvent(Throwable thrwbl) {
                    Utils.Log("CommLog errror!");
                    Utils.Log(thrwbl.toString());
                    thrwbl.printStackTrace();
                }
            };
            conn.addLogListener(listener);
            boolean connected = conn.connectTo(lejosInfo, NXTComm.PACKET);
            open = conn;

            if (connected) {
                outputStream = new DataOutputStream(open.getOutputStream());
                inputStream = new DataInputStream(open.getInputStream());
            }

            return connected;
        } catch (Exception e) {

            Utils.Log(e.toString());
            e.printStackTrace();
            return false;
        }

    }

    private void close() {
        if (open != null) {
            try {
                open.close();
            } catch (IOException ex) {
            }

        }
        open = null;
        if (builder != null) {
            builder.stopReceiving();

        }
        builder = null;
    }
}
