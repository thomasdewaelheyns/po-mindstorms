package penoplatinum.simulator;

public class EmptyAgent implements GatewayClient{

  @Override
  public void setRobot(Robot robot) {
    //does nothing
  }

  @Override
  public void run() {
    //does nothing
  }

  @Override
  public void receive(String cmd) {
    //does nothing
  }

  @Override
  public void send(String status) {
    //does nothing
  }

}