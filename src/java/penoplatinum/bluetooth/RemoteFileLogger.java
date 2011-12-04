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
 * 
 * @author: Team Platinum
 */
public class RemoteFileLogger {

    Thread fileThread;
    IRemoteLoggerCallback outputStream;
    private final File directory;
    private final PacketTransporter pt;

    public void setOutputStream(IRemoteLoggerCallback outputStream) {
        this.outputStream = outputStream;
    }

    public RemoteFileLogger(IConnection conn, String baseFilename, final File directory) {


        







        int testNum = 0;

        pt = new PacketTransporter(conn);
        conn.RegisterTransporter(pt, penoplatinum.Utils.PACKETID_LOG);
        conn.RegisterTransporter(pt, penoplatinum.Utils.PACKETID_STARTLOG);
        this.directory = directory;
        directory.mkdirs();






    }

    public void startLogging() {
        
        final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd.HHmmss");
        final IRemoteLoggerCallback extraOutputStream = outputStream;
        fileThread = new Thread(new Runnable() {

            @Override
            public void run() {
                PrintStream fs = null;
                while (true) {
                    

                    int id = pt.ReceivePacket();
                    Scanner scanner = new Scanner(pt.getReceiveStream()); //TODO: GC

                    if (id == penoplatinum.Utils.PACKETID_LOG) {
                        String s;
                        s = scanner.nextLine();
                        fs.println(s);

                        if (extraOutputStream != null) {
                            extraOutputStream.onLog(s);
                        }

                    } else if (id == penoplatinum.Utils.PACKETID_STARTLOG) {
                        String baseFilename = scanner.nextLine();
                        if (baseFilename.length() == 0) {
                            baseFilename = "DEFAULT";
                        }
                        if (baseFilename.length() > 100) {
                            baseFilename = baseFilename.substring(0, 100);
                        }


                        File file = new File(directory.getAbsoluteFile() + "/" + baseFilename + format.format(new Date()) + ".txt");
                        if (extraOutputStream != null) {
                            extraOutputStream.onLog("Start writing Robot log: " + file.getPath());
                        }
                        try {
                            fs = new PrintStream(file);
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(RemoteFileLogger.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                }
            }
        });
        fileThread.setName("RemoteFileLogger");
        fileThread.setDaemon(true);

        fileThread.start();

    }
}
