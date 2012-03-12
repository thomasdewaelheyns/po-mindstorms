package penoplatinum.agent;

import org.apache.log4j.Logger;

public class SectorsLogger implements CustomLogger {
  private static Logger logger = Logger.getLogger("sectors");   // 124

  public void log(String msg) {
    logger.info(msg);
  }
}
