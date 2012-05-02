package penoplatinum.fullTests.line;


import penoplatinum.fullTests.dumb.DumbNavigator;
import org.junit.Test;
import penoplatinum.driver.ManhattanDriver;
import static org.junit.Assert.*;
import penoplatinum.map.Map;
import penoplatinum.map.MapHashed;
import penoplatinum.navigator.Navigator;
import penoplatinum.simulator.Simulator;
import penoplatinum.simulator.entities.SimulatedEntity;
import penoplatinum.simulator.entities.SimulatedEntityFactory;
import penoplatinum.simulator.tiles.Sector;

public class LineTest {

  @Test
  public void testSimulator() {
    Simulator sim = new Simulator();
    sim.useMap(makeSquareMap());
    
    LineRobot line = new LineRobot();
    ManhattanDriver manhattan = new LineDriver(Sector.SIZE/100.0);
    Navigator dumbNavigator = new DumbNavigator();
    line.useDriver(manhattan).useNavigator(dumbNavigator);
    SimulatedEntity simE = SimulatedEntityFactory.make(line);
    
    simE.putRobotAt(20, 20, -90);
    sim.addSimulatedEntity(simE);
    sim.run(1000*6);
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
