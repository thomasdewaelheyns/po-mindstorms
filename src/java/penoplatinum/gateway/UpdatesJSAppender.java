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

import penoplatinum.ui.server.JSWrapper;

public class UpdatesJSAppender extends AppenderSkeleton {

  private String method   = "model";
  private String fileName = "updates.js";

  public boolean requiresLayout(){ return true; }

  public void setMethod(String method) {
    this.method = method;
  }

  public void setFile(String fileName) {
    this.fileName = fileName;
  }

  public synchronized void append( LoggingEvent event ) {
    
    try {
      // TODO: take into account method-name from property
      String message = this.layout.format(event);

      File file=new File(this.fileName);

      DataOutputStream outs = new DataOutputStream(
        new FileOutputStream(file, false));
      outs.write(message.getBytes());
      outs.close();
    } catch(Exception e) {
      System.err.println( "Failed to append to updates.js..." );
      throw new RuntimeException(e);
    }
  }
  
  public synchronized void close() {}
}
