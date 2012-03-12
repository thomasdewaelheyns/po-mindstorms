package penoplatinum.agent;

import org.apache.log4j.Logger;

public class ModelLogger implements CustomLogger {
  private static Logger logger = Logger.getLogger("penoplatinum.agent.ModelLogger");   // 123

  public void log(String msg) {
    logger.info(msg);
  }
}
