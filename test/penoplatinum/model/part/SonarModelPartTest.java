package penoplatinum.model.part;

/**
 * SonarModelPartTest
 * 
 * Tests SonarModelPart class
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;


public class SonarModelPartTest extends TestCase {

  private SonarModelPart part;
  private List<Integer> distances, angles;


  public SonarModelPartTest(String name) { 
    super(name);
  }
  
  public void testInitialPart() {
    this.createModelPart();
    assertEquals(0, this.part.getCurrentSweepId());
  }

  public void testUpdate() {
    this.createModelPart();
    this.generateSweep();
    this.part.update( this.distances, this.angles );
    
    assertEquals(1,              this.part.getCurrentSweepId());
    assertEquals(this.distances, this.part.getDistances() );
    assertEquals(this.angles,    this.part.getAngles() );
  }
  
  // construction helpers
  
  private void createModelPart() {
    this.part = new SonarModelPart();
  }
  
  private void generateSweep() {
    new ArrayList<Integer>(Arrays.asList(1,2,3,5,8,13,21));
    this.distances = new ArrayList<Integer>(Arrays.asList(10, 20, 30));
    this.angles    = new ArrayList<Integer>(Arrays.asList( 1,  2,  3));
  }

}
