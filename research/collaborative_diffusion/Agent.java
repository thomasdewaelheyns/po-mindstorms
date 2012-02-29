public interface Agent {
  public Agent   activate();
  public MessageHandler getMessageHandler();

  public Agent   setSector(Sector sector, int atLocation);
  public Sector  getSector();
  
  public boolean facesAgent(int atLocation);

  public boolean hasProxy();
  public Agent   getProxy();
  
  public boolean isTarget();
  public boolean isHunter();

  public String  getName();
  public int     getValue();
  public int     getLeft();
  public int     getTop();
  public int     getBearing();
  public int     getOriginalBearing();
  
  public void    move(int[] values);

  public boolean isHolding();
}
