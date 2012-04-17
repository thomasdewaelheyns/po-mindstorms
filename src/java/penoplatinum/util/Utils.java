package penoplatinum.util;

/**
 * Utils, a bag of various goodies.
 *
 * @author: Team Platinum
 */
import java.io.PrintStream;
import java.util.List;
import java.util.ListIterator;
import penoplatinum.Config;
import penoplatinum.bluetooth.IConnection;
import penoplatinum.bluetooth.QueuedPacketTransporter;

public class Utils {

  private static final int REVERSE_THRESHOLD = 18;
  private final static Object logLock = new Object();
  private static QueuedPacketTransporter logTransporter;
  private static PrintStream logPrintStream;

  public static void Sleep(long milliseconds) {
    try {
      Thread.sleep(milliseconds);
    } catch (InterruptedException ex) {
      System.out.println("InterruptException");
    }
  }

  public static void print(String message) {
    if (message == null) {
      Utils.Log("NULL!!");
      return;
    }
    System.out.print(message);

    synchronized (logLock) {
      if (logTransporter != null) {
        logPrintStream.print(message);
        logTransporter.SendPacket(Config.BT_LOG);
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
        logTransporter.SendPacket(Config.BT_LOG);
      }
    }
  }

  public static void Error(String message) {
    Utils.Log(message);
    Utils.Sleep(20000);
    throw new RuntimeException(message);
  }

  // TODO: should be handled all by a Gateway
  public static void EnableRemoteLogging(IConnection conn) {
    EnableRemoteLogging(conn, "RobotLog");
  }

  /**
   * TODO: add a filename that can be logged to!
   *
   * @param conn
   */
  public static void EnableRemoteLogging(IConnection conn, String logname) {

    QueuedPacketTransporter t = new QueuedPacketTransporter(conn);
    conn.RegisterTransporter(t, Config.BT_LOG);
    conn.RegisterTransporter(t, Config.BT_START_LOG);

    synchronized (logLock) {
      logTransporter = t;
      logPrintStream = new PrintStream(logTransporter.getSendStream());
      logPrintStream.println(logname);
      t.SendPacket(Config.BT_START_LOG);
    }
  }

  public static int ClampLooped(int val, int start, int end) {
    val -= start;
    end -= start;

    if (val < 0) {
      val = (val % end) + end;
    }
    val = val % end;
    val += start;

    return val;
  }

  public static double ClampLooped(double val, int start, int end) {
    val -= start;
    end -= start;

    if (val < 0) {
      val = (val % end) + end;
    }
    val = val % end;
    val += start;

    return val;
  }

  @SuppressWarnings("unchecked")
  public static void reverse(List<?> list) {
    int size = list.size();
    if (size < REVERSE_THRESHOLD) {
      for (int i = 0, mid = size >> 1, j = size - 1; i < mid; i++, j--) {
        swap(list, i, j);
      }
    } else {
      ListIterator fwd = list.listIterator();
      ListIterator rev = list.listIterator(size);
      for (int i = 0, mid = list.size() >> 1; i < mid; i++) {
        Object tmp = fwd.next();
        fwd.set(rev.previous());
        rev.set(tmp);
      }
    }
  }

  @SuppressWarnings("unchecked")
  public static void swap(List<?> list, int i, int j) {
    final List l = list;
    l.set(i, l.set(j, l.get(i)));
  }
}