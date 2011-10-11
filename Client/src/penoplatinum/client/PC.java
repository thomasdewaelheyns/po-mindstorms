package penoplatinum.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommandConnector;

public class PC {

    private OutputStream outputStream;
    private InputStream inputStream;
    private NXTComm open;

    public static void main(String[] args) {

        PC pc = new PC();

        try {
            pc.connect();
            pc.send(255);
            pc.send(255);
            pc.send(255);
            pc.send(255);
            pc.send(255);
            pc.send(255);
            return;
        } catch (Exception ex) {
        }
        System.out.println(pc.open);
        pc.close();

    }

    public void connect() throws Exception {
        open = NXTCommandConnector.open();
        
        outputStream = open.getOutputStream();
        inputStream = open.getInputStream();
    }

    public void close() {
        try {
            open.close();
        } catch (IOException ex) {
        }
    }

    public void send(int n) {
        System.out.println("e: " + n);
        try {
            outputStream.write(n);
            outputStream.flush();
        } catch (IOException ex) {
            System.out.println("IO Exception.");
        }
    }
}
