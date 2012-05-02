package penoplatinum.robot;

import penoplatinum.driver.Driver;
import penoplatinum.gateway.GatewayClient;
import penoplatinum.model.Model;
import penoplatinum.navigator.Navigator;
import penoplatinum.reporter.Reporter;

/**
 * This interface defines a more complex Robot. allowing full capabilities of the interface.
 * It offers access to all components of the robot, allowing the construction
 * of completely custom robots.
 * @author Rupsbant
 */
public interface AdvancedRobot extends Robot {
  
  // - a Driver, that knows how to move from one point to the other,
  public AdvancedRobot useDriver(Driver driver);
  public Driver getDriver();
  
  // - a Navigator, that decides what point to move next to
  public AdvancedRobot useNavigator(Navigator navigator);
  public Navigator getNavigator();

  // - a GatyewayClient, that provides access to the Gateway to send out msgs
  public AdvancedRobot useGatewayClient(GatewayClient agent);
  public GatewayClient getGatewayClient();
  
  // incoming commands are processed
  public void processCommand(String cmd);
  
  // - a Reporter, that interrogates the Robot and can send information out
  public AdvancedRobot useReporter(Reporter reporter);

  // - an internal Model
  public Model getModel();
 
}
