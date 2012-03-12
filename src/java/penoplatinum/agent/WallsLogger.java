package penoplatinum.agent;

import org.apache.log4j.Logger;

public class WallsLogger implements CustomLogger {
  private static Logger logger = Logger.getLogger("walls");   // 123

  public void log(String msg) {
    logger.info(msg);
  }
}
