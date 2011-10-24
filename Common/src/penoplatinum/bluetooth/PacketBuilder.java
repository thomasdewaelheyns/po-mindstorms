/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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

    public PacketBuilder(DataOutputStream outputStream, DataInputStream inputStream) {
        this.outputStream = outputStream;
        this.inputStream = inputStream;
    }
    /**
     * Readonly, only write in main thread
     */
    private boolean receiving;

    private void startReceiving() {
        receiving = true;

        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
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

                        while ((available = inputStream.available()) < packetHeaderSize && available >= 0) { // wait for packet header
                            Utils.Sleep(20); //TODO: frequency
                        }
                        if (available < 0) {
                            Utils.Log("Available Smaller than one!");
                            continue;
                        }

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
