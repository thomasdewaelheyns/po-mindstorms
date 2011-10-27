/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.bluetooth;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MHGameWork
 */
public class SimulatedConnection implements IConnection {

    private SimulatedConnection endPoint;
    private PacketBuilder builder;
    private HashMap<Integer, IPacketTransporter> map = new HashMap<Integer, IPacketTransporter>();
    private PipedInputStream internalStream;
    private PipedOutputStream externalStream;

    public SimulatedConnection() {
        internalStream = new PipedInputStream();
        try {
            externalStream = new PipedOutputStream(internalStream);
        } catch (IOException ex) {
            Logger.getLogger(SimulatedConnection.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    public void setEndPoint(SimulatedConnection endPoint) {
        this.endPoint = endPoint;

        createPacketBuilder();
        builder.startReceiving();
    }

    private void createPacketBuilder() {
        builder = new PacketBuilder(new DataOutputStream(endPoint.externalStream), new DataInputStream(internalStream), new IPacketReceiver() {

            @Override
            public void onPacketReceived(int packetIdentifier, byte[] dgram, int size) {

                map.get(packetIdentifier).onPacketReceived(packetIdentifier, dgram, 0, size);

            }
        });
    }

    @Override
    public void RegisterTransporter(IPacketTransporter transporter, int packetIdentifier) {
        map.put(packetIdentifier, transporter);
    }

    @Override
    public void SendPacket(IPacketTransporter transporter, int packetIdentifier, byte[] dgram) {

        builder.sendPacket(packetIdentifier, dgram);

    }
}
