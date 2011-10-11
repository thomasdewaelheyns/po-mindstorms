package penoplatinum.client;

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
        Scanner sc = new Scanner(System.in);
        while (true) {
            char get = sc.next().charAt(0);
            if (get=='v') {
                int h=sc.nextInt();
                int d=sc.nextInt();
                int sp=sc.nextInt();
                pc.send(4);
                pc.send(d);
                pc.send(h);
                pc.send(sp);
            } else if(get=='r'){
                pc.send(1);
                pc.send(0);
                pc.send(2);
                pc.send(0);
            } else if (get == 'z') {
                pc.send(1);
                pc.send(-720/4);
                pc.send(2);
                pc.send(-720/4);
            } else if (get == 'q') {
                pc.send(1);
                pc.send(0);
                pc.send(2);
                pc.send(360/4);
            } else if (get == 'd') {
                pc.send(1);
                pc.send(360/4);
                pc.send(2);
                pc.send(0);
            } else if (get == 's') {
                pc.send(1);
                pc.send(720/4);
                pc.send(2);
                pc.send(720/4);
            } else if (get == 'b') {
                pc.send(3);
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
        if (n > 255) throw new RuntimeException("QSDFSDQF");
        System.out.println("e: " + n);
        try {
            outputStream.write((byte) n);
            outputStream.flush();
        } catch (IOException ex) {
            System.out.println("IO Exception.");
        }
    }
}
