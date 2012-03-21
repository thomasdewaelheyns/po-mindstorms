package penoplatinum.util;

public class FPS {

  // the external tick...
  private long delta = 0;
  private int count = 0;
  private long start = 0;

  public FPS() {
  }

  public void setCheckPoint() {
    start = System.nanoTime();
  }
  
  public void endCheckPoint(){
    delta += System.nanoTime() - start;
    if (delta > 1000L * 1000 * 1000 || count > 100) {
      int fps = (int) (count / (double) delta * 1000d * 1000d * 1000d);
      Utils.Log("FPS: " + Integer.toString(fps));
      count = 0;
      delta = 0;
    }
    count++;
  }
}
