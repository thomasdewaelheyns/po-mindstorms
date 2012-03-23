package penoplatinum.gateway;

public interface Connection {
  public Connection send(String msg, int channel);

  public boolean hasNext();
  public String  getMessage();
  public int     getType();
}
