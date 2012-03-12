package penoplatinum.agent;

import org.apache.log4j.Logger;

public class AgentsLogger implements CustomLogger {
  private static Logger logger = Logger.getLogger("agents");   // 126

  public void log(String msg) {
    logger.info(msg);
  }
}
