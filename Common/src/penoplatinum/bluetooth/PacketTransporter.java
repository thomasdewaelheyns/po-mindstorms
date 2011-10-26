/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.bluetooth;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 *
 * @author MHGameWork
 */
public class PacketTransporter implements IPacketTransporter {

    private final Queue<Packet> receivedQueue = new ArrayBlockingQueue<Packet>(100);
    private ByteArrayOutputStream byteArrayOutputStream;
    private DataInputStream receiveStream;
    private DataOutputStream sendStream;
    private final IConnection connection;

    public PacketTransporter(IConnection connection) {
        receiveStream = new DataInputStream(new ByteArrayInputStream(new byte[1024]));
        byteArrayOutputStream = new ByteArrayOutputStream();
        sendStream = new DataOutputStream(byteArrayOutputStream);
        this.connection = connection;
    }

    @Override
    public DataInputStream getReceiveStream() {
        synchronized (this) {
            return receiveStream;

        }
    }

    @Override
    public DataOutputStream getSendStream() {
        synchronized (this) {
            return sendStream;
        }
    }

    @Override
    public int ReceivePacket() {
        synchronized (receivedQueue) {
            while (receivedQueue.isEmpty()) {
                try {
                    receivedQueue.wait();
                } catch (InterruptedException ex) {
                    System.out.println("ReceivePacketException");
                }
            }

            Packet p = receivedQueue.remove();
            updateReceiveStream(p);

            return p.PacketIdentifier;


        }
    }

    private void updateReceiveStream(Packet p) {
        ByteArrayInputStream strm = new ByteArrayInputStream(p.Dgram);
        receiveStream = new DataInputStream(strm);
    }

    @Override
    public int ReceiveAvailablePacket() {
        synchronized (receivedQueue) {
            if (receivedQueue.isEmpty()) {
                return -1;
            }

            Packet p = receivedQueue.remove();

            updateReceiveStream(p);

            return p.PacketIdentifier;
        }

    }

    @Override
    public void SendPacket(int packetIdentifier) {
        connection.SendPacket(this, packetIdentifier, byteArrayOutputStream.toByteArray());
        sendStream = new DataOutputStream(byteArrayOutputStream); // TODO: GC

    }

    @Override
    public void onPacketReceived(int packetIdentifier, byte[] dgram,int offset, int size) {
        Packet p = new Packet();
        p.PacketIdentifier = packetIdentifier;
        p.Dgram = Arrays.copyOfRange(dgram, offset,size);

        synchronized (receivedQueue) {
            receivedQueue.add(p);
        }

    }
}
