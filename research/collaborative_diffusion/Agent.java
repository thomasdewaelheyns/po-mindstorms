public interface Agent {
  public Agent   activate();
  public MessageHandler getMessageHandler();

  public Agent   setSector(Sector sector, int bearing);
  public Sector  getSector();

  public boolean isTarget();
  public boolean isHunter();

  public String  getName();
  public int     getValue();
  public int     getLeft();
  public int     getTop();
  public int     getOrientation();
  
  public void    move(int[] values);

  public boolean isHolding();
}
