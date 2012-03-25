package penoplatinum.simulator;

import penoplatinum.gateway.GatewayClient;

public class EmptyAgent implements GatewayClient{


  @Override
  public void run() {
    //does nothing
  }

  @Override
  public void receive(String cmd) {
    //does nothing
  }


  @Override
  public GatewayClient setRobot(Robot robot) {
    return this;
  }

  @Override
  public void send(String data, int channel) {
  }

}