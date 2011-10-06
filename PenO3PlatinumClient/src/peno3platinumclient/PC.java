package peno3platinumclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

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
        } catch (Exception ex) {
        }
        Scanner sc = new Scanner(System.in);
        while (true) {
            char get = sc.next().charAt(0);
            if (get == 'z') {
                pc.send(1);
                pc.send(720);
                pc.send(2);
                pc.send(720);
            } else if (get == 'q') {
                pc.send(1);
                pc.send(0);
                pc.send(2);
                pc.send(360);
            } else if (get == 'd') {
                pc.send(1);
                pc.send(360);
                pc.send(2);
                pc.send(0);
            } else if (get == 's') {
                pc.send(1);
                pc.send(-720);
                pc.send(2);
            } else if (get == 'b') {
                break;
            }
        }
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
        }
    }
}
