public class ProxyAgent extends MovingAgent {
  public ProxyAgent() { super( "proxy"); }

  public void move(int[] values) {
    // we're not moving on our own
  }

  public int getValue() { return 0; }

  public boolean isHolding() { return false; }

  public boolean isProxy() { return true; }
}
