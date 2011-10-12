package penoplatinum.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommandConnector;
import lejos.pc.comm.NXTConnector;

public class PC {

    private OutputStream outputStream;
    private InputStream inputStream;
    private NXTComm open;

    public static void main(String[] args) {
        PC pc = new PC();

        try {
            pc.connect();
        } catch (Exception ex) {}
        System.out.println(pc.open);
        pc.sendCatch(1);
        pc.sendCatch(0);
        pc.sendCatch(0);
        pc.sendCatch(0);
        pc.sendCatch(123456789);
        pc.close();

    }

    public boolean connect() throws Exception {
        NXTConnector conn = new NXTConnector();
        boolean connected = conn.connectTo(NXTComm.PACKET);
        open = (connected ? conn.getNXTComm() : null);
        outputStream = (connected ? open.getOutputStream() : null);
        inputStream = (connected ? open.getInputStream() : null);
        return connected;
    }

    public void close() {
        try {
            open.close();
        } catch (IOException ex) {
        }
    }

    public boolean sendCatch(int n) {
        try {
            send(n);
        } catch (IOException ex) {
            System.out.println("IO Exception.");
            return false;
        }
        return true;
    }
    public void send(int n) throws IOException{
        System.out.println("send: " + n);
        outputStream.write(n);
        outputStream.flush();
    }
}
