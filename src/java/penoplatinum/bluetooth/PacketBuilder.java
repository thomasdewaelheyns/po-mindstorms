package penoplatinum.bluetooth;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import penoplatinum.Utils;

/**
 *
 * @author MHGameWork
 */
public class PacketBuilder {

    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    private final IPacketReceiver receiver;
    private boolean errorOccured;

    public synchronized boolean isErrorOccured() {
        return errorOccured;
    }

    public synchronized void setErrorOccured(boolean errorOccured) {
        this.errorOccured = errorOccured;
    }

    public PacketBuilder(DataOutputStream outputStream, DataInputStream inputStream, IPacketReceiver receiver) {
        this.outputStream = outputStream;
        this.inputStream = inputStream;
        this.receiver = receiver;

    }
    /**
     * Readonly, only write in main thread
     */
    private boolean receiving;

    public void startReceiving() {
        receiving = true;

        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    byte[] buffer = new byte[1024];//new byte[256];
                    int available;
                    int packetHeaderSize = 4 + 2; // Identifier + (short) size
                    while (receiving) {
                        int identifier = inputStream.readInt();
                        int size = inputStream.readShort();
                        inputStream.read(buffer, 0, size);
                        receiver.onPacketReceived(identifier, buffer, size);


                    }
                } catch (IOException ex) {
                    Utils.Log("Receive error!");
                    if (ex.toString() != null) {
                        Utils.Log(ex.toString());
                    }
                    setErrorOccured(true);
                    receiver.onError(ex);
                }

            }
        });
        t.setDaemon(true);
        t.start();
    }

    /**
     * Not entirely implemented
     */
    public void stopReceiving() {
        receiving = false; // TODO: WARNING! MAYBE NOT THREAD SAFE
    }

    public void sendPacket(int packetIdentifier, byte[] dgram) {
        try {
            //Utils.Log("Send packet." + packetIdentifier);
            outputStream.writeInt(packetIdentifier);
            outputStream.writeShort((short) dgram.length);
            outputStream.write(dgram, 0, dgram.length);
            outputStream.flush();
            //TODO: flush??
        } catch (IOException ex) {
            setErrorOccured(true);
            Utils.Log("Send error!");
            if (ex.getMessage() != null) {
                Utils.Log(ex.getMessage());
            }
        }


    }
}
