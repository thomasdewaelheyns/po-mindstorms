package penoplatinum.simulator;

import junit.framework.TestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import penoplatinum.map.Map;
import penoplatinum.map.MapTestUtil;
import penoplatinum.robot.Robot;
import penoplatinum.simulator.entities.PacmanEntity;
import penoplatinum.simulator.entities.RemoteEntity;
import penoplatinum.simulator.entities.SimulatedEntity;
import penoplatinum.simulator.tiles.Tile;
import penoplatinum.simulator.view.SilentSimulationView;
import penoplatinum.simulator.view.SimulationView;
import penoplatinum.simulator.view.ViewRobot;
import penoplatinum.util.Point;

public class SimulatorTest extends TestCase {

  /**
   * Test of addSimulatedEntity method, of class Simulator.
   */
  @Test
  public void testAddSimulatedEntity() {
    System.out.println("addSimulatedEntity");
    SimulatedEntity r = getEntity();
    Simulator instance = new Simulator();
    instance.addSimulatedEntity(r);
    verify(r).useSimulator(instance);
    verify(r, times(1)).getViewRobot();
    SimulationView s = mock(SimulationView.class);
    instance.displayOn(s);
    verify(r, times(2)).getViewRobot();
  }

  /**
   * Test of addRemoteEntity method, of class Simulator.
   */
  @Test
  public void testAddRemoteEntity() {
    System.out.println("addRemoteEntity");
    String entityName = "qsdf";
    int originX = 125;
    int originY = 654;
    int originBearing = 124;
    Simulator instance = new Simulator();
    RemoteEntity result = instance.addRemoteEntity(entityName, originX, originY, originBearing);
    assertEquals(entityName, result.getEntityName());
    assertEquals(originX, result.getPosX(), 0);
    assertEquals(originY, result.getPosY(), 0);
    assertEquals(originBearing, result.getAngle(), 0);
  }

  /**
   * Test of displayOn method, of class Simulator.
   */
  @Test
  public void testDisplayOn() {
    System.out.println("displayOn");
    SimulationView view = mock(SimulationView.class);
    Simulator instance = new Simulator();
    SimulatedEntity entity = getEntity();
    instance.addSimulatedEntity(entity);
    PacmanEntity pacman = mock(PacmanEntity.class);
    instance.setPacmanEntity(pacman);

    verify(entity, times(1)).getViewRobot();
    verify(pacman, times(1)).getViewRobot();
    Simulator result = instance.displayOn(view);
    assertEquals(instance, result);
    verify(entity, times(2)).getViewRobot();
    verify(pacman, times(2)).getViewRobot();
  }

  /**
   * Test of useMap method, of class Simulator.
   */
  @Test
  public void testUseMap() {
    System.out.println("useMap");
    Map map = MapTestUtil.getMap();
    Simulator instance = new Simulator();
    Simulator result = instance.useMap(map);
    assertEquals(instance, result);
    assertEquals(map, instance.getMap());
  }

  /**
   * Test of getFreeDistance method, of class Simulator.
   */
  @Test
  public void testGetFreeDistance() {
    System.out.println("getFreeDistance");
    Point tile = new Point(1, 1);
    Point pos = new Point(20, 20);
    int angle = 0;
    Simulator instance = new Simulator().useMap(MapTestUtil.getMap());
    assertEquals(60, instance.getFreeDistance(tile, pos, angle));
    angle = 90;
    assertEquals(20, instance.getFreeDistance(tile, pos, angle));
    angle = -45;
    assertEquals(28+56+1, instance.getFreeDistance(tile, pos, angle));
  }

  /**
   * Test of send method, of class Simulator.
   */
  @Test
  public void testSendReceive() {
    System.out.println("send");
    String cmd = "";
    Simulator instance = new Simulator();
    assertEquals(instance, instance.send(cmd));
    assertEquals(instance, instance.receive(cmd));
  }

  /**
   * Test of run method, of class Simulator.
   */
  @Test
  public void testRun() {
    System.out.println("run");
    Simulator instance = new Simulator();
    Simulator expResult = null;
    //Simulator result = instance.run();
    //assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setPacmanEntity method, of class Simulator.
   */
  @Test
  public void testSetPacmanEntity() {
    System.out.println("setPacmanEntity");
    SimulationView view = mock(SimulationView.class);
    Simulator instance = new Simulator().displayOn(view);

    assertEquals(null, instance.getPacMan());
    PacmanEntity p = mock(PacmanEntity.class);
    instance.setPacmanEntity(p);
    verify(p, times(1)).getViewRobot();
    verify(view, times(1)).addRobot(p.getViewRobot());
    assertEquals(p, instance.getPacMan());
  }

  /**
   * Test of getTileSize method, of class Simulator.
   */
  @Test
  public void testGetTileSize() {
    System.out.println("getTileSize");
    Simulator instance = new Simulator().useMap(MapTestUtil.getMap());
    assertEquals(40, instance.getTileSize());
    Map m = mock(Map.class);
    Tile t = mock(Tile.class);
    when(m.getFirst()).thenReturn(t);
    when(t.getSize()).thenReturn(1524);
    assertEquals(1524, instance.useMap(m).getTileSize());
  }

  /**
   * Test of useStepRunnable method, of class Simulator.
   */
  @Test
  public void testUseStepRunnable() {
    System.out.println("useStepRunnable");
    Runnable runnable = null;
    Simulator instance = new Simulator();
    instance.useStepRunnable(runnable);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getView method, of class Simulator.
   */
  @Test
  public void testGetView() {
    System.out.println("getView");
    Simulator instance = new Simulator();
    assertEquals(SilentSimulationView.class, instance.getView().getClass());
    SimulationView s = mock(SimulationView.class);
    assertEquals(s, instance.displayOn(s).getView());
  }

  /**
   * Test of getSimulatedEntityByName method, of class Simulator.
   */
  @Test
  public void testGetSimulatedEntityByName() {
    System.out.println("getSimulatedEntityByName");
    Simulator instance = new Simulator();
    assertEquals(null, instance.getSimulatedEntityByName("ahdfsjkl"));
    SimulatedEntity entity = getEntity();
    Robot r = mock(Robot.class);
    when(entity.getRobot()).thenReturn(r);
    when(r.getName()).thenReturn("def");
    instance.addSimulatedEntity(entity);
    assertEquals(entity, instance.getSimulatedEntityByName("def"));
  }

  private SimulatedEntity getEntity() {
    SimulatedEntity out = mock(SimulatedEntity.class);
    ViewRobot vr = mock(ViewRobot.class);
    when(out.getViewRobot()).thenReturn(vr);
    return out;
  }
}
