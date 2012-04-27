package penoplatinum.model.part;

/**
 * GapModelPartTest
 * 
 * Tests GapModelPart class
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 
import static org.mockito.Mockito.*;


public class GapModelPartTest extends TestCase {

  private GapModelPart part;


  public GapModelPartTest(String name) { 
    super(name);
  }

  public void testInitialNoGap() {
    this.createModelPart();
    assertFalse( this.part.foundGap() );
    assertEquals( 0, this.part.getGapStart() );
    assertEquals( 0, this.part.getGapEnd() );
  }

  public void testFoundGap() {
    this.createModelPart();
    this.part.markGap( -120, -100 );
    assertTrue( this.part.foundGap() );
    assertEquals( -120, this.part.getGapStart() );
    assertEquals( -100, this.part.getGapEnd() );
  }

  public void testClearGap() {
    this.createModelPart();
    this.part.markGap( -120, -100 );
    this.part.clearGap();
    assertFalse( this.part.foundGap() );
    assertEquals( 0, this.part.getGapStart() );
    assertEquals( 0, this.part.getGapEnd() );
  }
  
  // construction helpers
  
  private void createModelPart() {
    this.part = new GapModelPart();
  }
  
}
