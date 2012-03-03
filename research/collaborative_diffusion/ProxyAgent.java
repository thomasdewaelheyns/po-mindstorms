public class ProxyAgent extends MovingAgent {
  public ProxyAgent(String name) { super(name); }

  public void move(int[] values) {
    // we're not moving on our own
  }

  public int getValue() { return 0; }

  public boolean isHolding() { return false; }

  public boolean isProxy() { return true; }
}
