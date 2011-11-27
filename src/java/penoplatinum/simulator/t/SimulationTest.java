import junit.framework.*; 
import java.util.List;
import penoplatinum.simulator.*;

public class SimulationTest extends TestCase implements GoalDecider {
  
  private TestSimulation simulation;
  
  public SimulationTest(String name) {
    super(name);
  }
  
  public void testSonar() {
    this.simulation = new TestSimulation()
      .useMap(this.createMap())
      .putRobotAt(10,60,0)
      .setControler(this);
    // the run call needs to be separate, else this.simulation is not set yet      
    this.simulation.run();
    List<Integer> dist = this.simulation.getRobot().getModel().getDistances();
    // handy to test with visual view (FIXME: integrate this better)
    // try { Thread.sleep(100000); } catch(Exception e) {}
    assertEquals(  61, (int)dist.get(1) );
    assertEquals(  93, (int)dist.get(5) );
    assertEquals( 150, (int)dist.get(9) );
  }

  public Boolean reachedGoal() {
    List<Integer> distances = this.simulation.getRobot().getModel().getDistances();
    return distances != null && distances.size() > 0;
  }
  
  // we use a single-Tile Map
  private Map createMap() {
    return new Map(2)
    .add( new Tile()
                        .withWall(Baring.N)
    .withWall(Baring.W)                     .withWall(Baring.NONE)
                        .withWall(Baring.S)
    )
    .add( new Tile()
                        .withWall(Baring.N)
    .withWall(Baring.NONE)                  .withWall(Baring.E)
                        .withWall(Baring.S)
    );
  }
}
