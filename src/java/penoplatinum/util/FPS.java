package penoplatinum.util;

/**
 * FPS
 * 
 * calculated the Frame-rate Per Second
 * 
 * @author Team Platinum
 */

public class FPS {

  // the external tick...
  private long delta = 0;
  private int count = 0;
  private long start = 0;
  private int fps = 0;

  public FPS() {}

  public void setCheckPoint() {
    start = System.nanoTime();
  }
  
  public void endCheckPoint(){
    delta += System.nanoTime() - start;
    if (delta > 1000L * 1000 * 1000 || count > 100) {
      fps = (int) (count / (double) delta * 1000d * 1000d * 1000d);
      count = 0;
      delta = 0;
    }
    count++;
  }

  public int getFps(){
    return fps;
  }
}
