package penoplatinum.fullTests;

import org.junit.Test;
import static org.junit.Assert.*;
import penoplatinum.map.Map;
import penoplatinum.map.MapHashed;
import penoplatinum.robot.Robot;
import penoplatinum.simulator.Simulator;
import penoplatinum.simulator.entities.SimulatedEntity;
import penoplatinum.simulator.entities.SimulatedEntityFactory;
import penoplatinum.simulator.tiles.Sector;

public class DumbTest {

  @Test
  public void testSimulator() {
    //This tests the simulator with a driver that first moves forward an then turns to the right. 
    Simulator sim = new Simulator();
    sim.useMap(makeSquareMap());
    
    Robot dumb = new DumbRobot();
    SimulatedEntity simE = SimulatedEntityFactory.make(dumb);
    simE.putRobotAt(20, 20, -90);
    sim.addSimulatedEntity(simE);
    sim.run(1000*4);
    assertEquals(60.0, simE.getPosX(), 1);
    assertEquals(60.0, simE.getPosY(), 1);
    assertEquals(-720.0+90.0, simE.getDirection(), 5);
  }

  private Map makeSquareMap() {
    MapHashed out = new MapHashed();
    Sector s1 = new Sector();
    s1.addWall(0);
    s1.addWall(3);
    out.put(s1, 1, 1);

    s1 = new Sector();
    s1.addWall(0);
    s1.addWall(1);
    out.put(s1, 2, 1);

    s1 = new Sector();
    s1.addWall(2);
    s1.addWall(3);
    out.put(s1, 1, 2);

    s1 = new Sector();
    s1.addWall(1);
    s1.addWall(2);
    out.put(s1, 2, 2);

    return out;
  }
}
