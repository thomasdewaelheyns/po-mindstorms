package penoplatinum.gateway;

/**
 * A GatewacyClient that doesn't do anything, used for initialization.
 * 
 * @autor Team Platinum
 */  


public class NullGatewayClient implements GatewayClient {
  public void run() {}
  public void receive(String cmd) {}
  public GatewayClient setRobot(Robot robot) {
    return this;
  }
  public void send(String data, int channel) {}
}
