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

    public PacketBuilder(DataOutputStream outputStream, DataInputStream inputStream,IPacketReceiver receiver) {
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
                    byte[] buffer = new byte[256];
                    int available;
                    int packetHeaderSize = 4 + 2; // Identifier + (short) size
                    while (receiving) {
                        while ((available = inputStream.available()) < packetHeaderSize && available >= 0) { // wait for packet header
                            Utils.Sleep(20); //TODO: frequency
                        }
                        if (available < 0) {
                            Utils.Log("Available Smaller than one!");
                            continue;
                        }

                        int identifier = inputStream.readInt();
                        int size = inputStream.readShort();

                        while ((available = inputStream.available()) < size && available >= 0) { // wait for packet header
                            Utils.Sleep(20); //TODO: frequency
                        }
                        if (available < 0) {
                            Utils.Log("Available Smaller than one!");
                            continue;
                        }
                        
                        inputStream.read(buffer, 0, size);
                        
                        receiver.onPacketReceived(identifier, buffer, size);
                        

                    }
                } catch (IOException ex) {
                    Utils.Log("Receive error!");
                    Utils.Log(ex.toString());
                }

            }
        });
        t.start();
    }

    /**
     * Not entirely implemented
     */
    public void stopReceiving()
    {
        receiving = false;
    }
    
    public void sendPacket(int packetIdentifier, byte[] dgram) {
        try {
            outputStream.writeInt(packetIdentifier);
            outputStream.writeShort((short) dgram.length);
            outputStream.write(dgram, 0, dgram.length);
            outputStream.flush();
            //TODO: flush??
        } catch (IOException ex) {
            Utils.Log("Send error!");
            if (ex.getMessage() != null) {
                Utils.Log(ex.getMessage());
            }
        }


    }
}
