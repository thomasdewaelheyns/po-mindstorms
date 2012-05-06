package penoplatinum.util;

/**
 * FPSTest
 * 
 * Tests FPS enumeration
 * 
 * @author: Team Platinum
 */

import java.util.List;

import junit.framework.*; 

public class FPSTest extends TestCase {

  private FPS fps;

  public FPSTest(String name) { 
    super(name);
  }
 
  public void testFPS() {
    this.createFPS();

    for(int i=0; i<100; i++) {
      this.fps.setCheckPoint();
      this.wait(10);
      this.fps.endCheckPoint();
    }
    int fps = this.fps.getFps();
    assertTrue( fps < 100 && fps > 80 );
  }
  
  // construction helpers
  
  private void createFPS() {
    this.fps = new FPS();
  }
  
  private void wait(int time) {
    try {
      Thread.sleep(time);
    } catch(Exception e) {}
  }
}
