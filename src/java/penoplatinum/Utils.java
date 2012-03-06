package penoplatinum;

import java.io.PrintStream;
import penoplatinum.bluetooth.IConnection;
import penoplatinum.bluetooth.PacketTransporter;

/**
 * @author: Team Platinum
 */
public class Utils {

  public final static int PACKETID_LOG = 672631252;
  public final static int PACKETID_STARTLOG = 356356545;
  private final static Object logLock = new Object();
  private static PacketTransporter logTransporter;
  private static PrintStream logPrintStream;

  public static void Sleep(long milliseconds) {
    try {
      Thread.sleep(milliseconds);
    } catch (InterruptedException ex) {
      System.out.println("InterruptException");
    }
  }
  
  public static void print(String message){
    if (message == null) {
      Utils.Log("NULL!!");
      return;
    }
    System.out.print(message);

    synchronized (logLock) {
      if (logTransporter != null) {
        logPrintStream.print(message);
        logTransporter.SendPacket(PACKETID_LOG);
      }
    }
  }

  public static void Log(String message) {
    if (message == null) {
      Utils.Log("NULL!!");
      return;
    }
    System.out.println(message);

    synchronized (logLock) {
      if (logTransporter != null) {
        logPrintStream.println(message);
        logTransporter.SendPacket(PACKETID_LOG);
      }
    }
  }

  public static void Error(String message) {
    Utils.Log(message);
    Utils.Sleep(20000);
    throw new RuntimeException(message);
  }

  /**
   * @param conn 
   */
  public static void EnableRemoteLogging(IConnection conn) {
    EnableRemoteLogging(conn, "RobotLog");
  }

  /**
   * TODO: add a filename that can be logged to!
   * @param conn 
   */
  public static void EnableRemoteLogging(IConnection conn, String logname) {

    PacketTransporter t = new PacketTransporter(conn);
    conn.RegisterTransporter(t, PACKETID_LOG);
    conn.RegisterTransporter(t, PACKETID_STARTLOG);

    synchronized (logLock) {
      logTransporter = t;
      logPrintStream = new PrintStream(logTransporter.getSendStream());
      logPrintStream.println(logname);
      t.SendPacket(PACKETID_STARTLOG);
    }


  }

  public static int ClampLooped(int val, int start, int end) {
    val -= start;
    end -= start;

    if (val < 0) {
      val += (-val / end + 1) * end;
    }
    val = val % end;
    val += start;

    return val;
  }
}
