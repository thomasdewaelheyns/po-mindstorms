package penoplatinum.gateway;

/**
 * UpdatesJSAppender
 * 
 * Special Appender to dump all logged data to a file, in JS format, over-
 * writing the same file over and over again.
 *
 * Author: Team Platinum
 */

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ErrorCode;
import org.apache.log4j.Layout;
import org.apache.log4j.helpers.LogLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.DataOutputStream;

public class UpdatesJSAppender extends AppenderSkeleton {

  private String  fileName1 = "updates-1.js",
                  fileName2 = "updates-2.js";
  private int     count     = 0;
  private String  robotName = "none";

  private static String fileName = "";
  private static int eventId = 0;

  public boolean requiresLayout(){ return true; }

  public void setFile1(String fileName) {
    this.fileName1 = fileName;
    fileName = this.fileName1; // FIXME: this doesn't seem to work ?!
                               //        -> patched below in next()  
  }

  public void setFile2(String fileName) {
    this.fileName2 = fileName;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public void setRobot(String robotName) {
    this.robotName = robotName;
  }

  public synchronized void append( LoggingEvent event ) {
    String message = this.layout.format(event);

    if( ! message.startsWith("\"" + this.robotName) ) { 
      return;
    }

    this.next();
    this.append(this.wrapJS(event.getLoggerName(), message));
  }
  
  private String wrapJS(String scope, String message) {
    // add event sequence number and wrap in js-call
    return "Dashboard.file_update_" + scope + 
            "(" + eventId + "," + message + ");\n"; 
  }
  
  private void append(String js) {
    try {
      File file = new File(fileName);
      DataOutputStream outs = 
        new DataOutputStream(new FileOutputStream(file, true));
      outs.write(js.getBytes());
      outs.flush();
      outs.close();
    } catch(Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  private void next() {
    eventId++;

    // FIXME: why this extra init ?
    if( fileName.equals("") ) { fileName = this.fileName1; }
    
    if( eventId % this.count == 0 ) {
      // determine next fileName
      String nextFileName = fileName.equals(this.fileName1) ? 
        this.fileName2 : this.fileName1;

      // inject swap() instruction
      this.append( "swap(\"" + nextFileName + "\");\n" );

      // do swap
      fileName = nextFileName;

      // delete it to trigger restart
      new File(fileName).delete();
    }
  }
  
  public synchronized void close() {}
}
