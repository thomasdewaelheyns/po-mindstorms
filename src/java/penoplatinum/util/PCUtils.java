package penoplatinum.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utils class only for PC!
 * @author Rupsbant
 */
public class PCUtils {
  
  private static final String PROPERTIES_PATH = "robot.properties";
  public static final Properties PROPERTIES = getProperties();
  
  

  private static Properties getProperties(){
    try {
      Properties p = new Properties();
      p.load(new FileInputStream(PROPERTIES_PATH));
      return p;
    } catch (IOException ex) {
      Logger.getLogger(PCUtils.class.getName()).log(Level.SEVERE, null, ex);
      System.exit(-1);
    }
    return null;
  }
  
}
