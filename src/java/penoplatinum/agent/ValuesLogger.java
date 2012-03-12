package penoplatinum.agent;

import org.apache.log4j.Logger;

public class ValuesLogger implements CustomLogger {
  private static Logger logger = Logger.getLogger("values");   // 123

  public void log(String msg) {
    logger.info(msg);
  }
}
