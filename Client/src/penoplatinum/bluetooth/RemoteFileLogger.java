/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.bluetooth;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Writes text received from packets through a packettransporter to a file
 */
public class RemoteFileLogger {

    Thread fileThread;

    public RemoteFileLogger(IConnection conn, int packetIdentifier, String baseFilename, File directory) {
        directory.mkdirs();

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd.HHmmss");

        File file = new File(directory.getAbsoluteFile() + "/" + baseFilename + format.format(new Date()) + ".txt");
        PrintStream fs = null;
        try {
            fs = new PrintStream(file);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RemoteFileLogger.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (fs == null) {
            return;
        }

        final PrintStream fsFinal = fs;

        int testNum = 0;

        final PacketTransporter t = new PacketTransporter(conn);
        conn.RegisterTransporter(t, packetIdentifier);

        

        fileThread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    int id = t.ReceivePacket();
                    Scanner scanner = new Scanner(t.getReceiveStream()); //TODO: GC
                    String s;
                    s = scanner.nextLine();
                    fsFinal.println(s);
                }
            }
        });
        fileThread.setName("RemoteFileLogger");
        fileThread.setDaemon(true);


    }

    public void startLogging() {
        fileThread.start();

    }
}
