package penoplatinum;

import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import penoplatinum.grid.view.SwingGridView;
import penoplatinum.model.Model;
import penoplatinum.model.part.Barcode;
import penoplatinum.navigator.GhostNavigator;
import penoplatinum.robot.GhostRobot;
import penoplatinum.simulator.SimulatedGatewayClient;
import penoplatinum.simulator.Simulator;
import penoplatinum.simulator.SimulatorTest;
import penoplatinum.simulator.entities.PacmanEntity;
import penoplatinum.simulator.entities.SimulatedEntity;
import penoplatinum.simulator.entities.SimulationRobotAPI;
import penoplatinum.simulator.view.SwingSimulationView;
import static org.mockito.Mockito.*;
import penoplatinum.grid.Grid;
import penoplatinum.grid.Sector;
import penoplatinum.grid.agent.Agent;
import penoplatinum.grid.agent.PacmanAgent;
import penoplatinum.model.GhostModel;
import penoplatinum.model.part.GridModelPart;
import penoplatinum.model.part.MessageModelPart;
import penoplatinum.model.part.ModelPartRegistry;
import penoplatinum.protocol.ProtocolHandler;
import penoplatinum.util.Point;

/**
 *
 * @author MHGameWork
 */
public class GhostRobotTest extends TestCase{
  
  public GhostRobotTest(String name){
    super(name);
  }
  
  public void testHandleSendGridInformation(){
    GhostModel model = mockModel();
    GridModelPart gridPart = mock(GridModelPart.class);
    MessageModelPart messagePart = mock(MessageModelPart.class);
    Grid grid = mock(Grid.class);
    ArrayList<Sector> list = new ArrayList<Sector>();
    Sector sec1 = mock(Sector.class);
    Sector sec2 = mock(Sector.class);
    list.add(sec1);
    list.add(sec2);
    Iterable<Sector> sectors = list;
    PacmanAgent pacman = mock(PacmanAgent.class);
    ProtocolHandler handler = mock(ProtocolHandler.class);
    when(messagePart.getProtocolHandler()).thenReturn(handler);
    when(model.getPart(ModelPartRegistry.GRID_MODEL_PART)).thenReturn(gridPart);
    when(model.getPart(ModelPartRegistry.MESSAGE_MODEL_PART)).thenReturn(messagePart);
    when(gridPart.getMyGrid()).thenReturn(grid);
    when(grid.getSectors()).thenReturn(sectors);
    when(gridPart.getPacmanAgent()).thenReturn(pacman);
    when(grid.getPositionOf(pacman)).thenReturn(new Point(1,1));
    when(gridPart.getMyPosition()).thenReturn(new Point(2,1));
    GhostRobot robot = new GhostRobot("Robot1", model);
    robot.handleSendGridInformation();
    verify(handler).handleResendData(sectors, new Point(1,1), new Point(2,1));
  }

  private GhostModel mockModel(){
    GhostModel model = mock(GhostModel.class);
    return model;
  }
  
//  Simulator sim;
//  Random r = new Random(456);
//
//  @Before
//  public void setup() {
//    sim = new Simulator();
//  }
//
//  @After
//  public void after() {
//
//    sim.displayOn(new SwingSimulationView());
//    sim.run();
//  }
//
//  @Test
//  public void testInvertBarcode()
//  {
//    int a = Barcode.reverse(12,6);
//    int magic = 4;
//  }
//  
//  @Test
//  public void testGhostRobotLeftFollowing() throws FileNotFoundException {
//
//    sim.useMap(SimulatorTest.createSectorMap());
//
//    putGhostRobot(20, 20, 0, new LeftFollowingGhostNavigator(), "Michiel");
//
//  }
//
//  @Test
//  public void testGhostRobotGhostNavigator() throws FileNotFoundException {
//     sim.useMap(SimulatorTest.createSectorMap());
//
//    putGhostRobot(20, 20, 0);
//
//  }
//
//
//  @Test
//  public void testGhostRobotMazeProtocol() throws FileNotFoundException {
//    sim.useMap(SimulatorTest.createSectorMazeProtocol());
//    SwingGridView view = new SwingGridView();
//
//    putGhostRobot(20 + 1 * 40, 20 + 1 * 40, 0);
//
//  }
//
//  @Test
//  public void testGhostRobotBarcodeMergeSimple() throws FileNotFoundException {
//    sim.useMap(SimulatorTest.createSectorMap());
//
//
//
//    String name = r.nextInt() + "";
//
//    SimulatedEntity ent1 = putGhostRobot(20 + 0 * 40, 20 + 2 * 40, 0);
//    SimulatedEntity ent2 = putGhostRobot(20 + 3 * 40, 20 + 1 * 40, 90);
////    putGhostRobot(20 + 5 * 40, 20 + 4 * 40, 0);
//
////    sim.addRemoteEntity(ent1.getRobot().getName(), 0, 2, Bearing.N);
////    sim.addRemoteEntity(ent2.getRobot().getName(), 3, 1, Bearing.W);
//
//  }
//
//  @Test
//  public void testGhostRobotMazeProtocol3() throws FileNotFoundException {
//    sim.useMap(SimulatorTest.createSectorMazeProtocol());
//
//
//
//    String name = r.nextInt() + "";
//
//    SimulatedEntity ent1 = putGhostRobot(20 + 0 * 40, 20 + 2 * 40, 0);
//    SimulatedEntity ent2 = putGhostRobot(20 + 3 * 40, 20 + 1 * 40, -90);
//    putGhostRobot(20 + 5 * 40, 20 + 4 * 40, 0);
//
////    sim.addRemoteEntity(ent1.getRobot().getName(), 0, 2, Bearing.N);
////    sim.addRemoteEntity(ent2.getRobot().getName(), 3, 1, Bearing.W);
//
//  }
//
//  @Test
//  public void testBarcodeReverse() throws FileNotFoundException {
//    sim.useMap(SimulatorTest.createSectorMap());
//
//
//
//    String name = r.nextInt() + "";
//
//    SimulatedEntity ent2 = putGhostRobot(20 + 3 * 40, 20 + 1 * 40, -90);
//
////    sim.addRemoteEntity(ent1.getRobot().getName(), 0, 2, Bearing.N);
////    sim.addRemoteEntity(ent2.getRobot().getName(), 3, 1, Bearing.W);
//
//  }
//
//  @Test
//  public void testBackupGapcorrection() throws FileNotFoundException {
//    sim.useMap(SimulatorTest.createSectorMap());
//
//    putGhostRobot(9, 20, 90, new LeftFollowingGhostNavigator(), "Michiel");
//
//  }
//
//  @Test
//  public void testPacman() throws FileNotFoundException {
//    sim.useMap(SimulatorTest.createSectorMap());
//
//
//
//    String name = r.nextInt() + "";
//
//    SimulatedEntity ent2 = putGhostRobot(20 + 3 * 40, 20 + 1 * 40, -90);
//
//    sim.setPacmanEntity(new PacmanEntity(20, 20, 0));
////    sim.addRemoteEntity(ent1.getRobot().getName(), 0, 2, Bearing.N);
////    sim.addRemoteEntity(ent2.getRobot().getName(), 3, 1, Bearing.W);
//
//  }
//
//  @Test
//  public void testGhostRobotMazeProtocol3Pacman() throws FileNotFoundException {
//    sim.useMap(SimulatorTest.createSectorMazeProtocol2());
//
//
//
//    String name = r.nextInt() + "";
//
//    SimulatedEntity ent1 = putGhostRobot(20 + 5 * 40, 20 + 2 * 40, 0);
//    SimulatedEntity ent2 = putGhostRobot(20 + 3 * 40, 20 + 1 * 40, -90);
//    putGhostRobot(20 + 5 * 40, 20 + 4 * 40, 0);
//    
//    
//
////    sim.addRemoteEntity(ent1.getRobot().getName(), 0, 2, Bearing.N);
////    sim.setPacmanEntity(new PacmanEntity(260, 180, 0));
////    sim.addRemoteEntity(ent2.getRobot().getName(), 3, 1, Bearing.W);
//
//  }
//  
//  private SimulatedEntity putGhostRobot(int x, int y, int angle) {
//    return putGhostRobot(x, y, angle, new GhostNavigator(), r.nextInt() + "");
//  }
//
//  private SimulatedEntity putGhostRobot(int x, int y, int angle, String name) {
//    return putGhostRobot(x, y, angle, new GhostNavigator(), name);
//
//  }
//
//  private SimulatedEntity putGhostRobot(int x, int y, int angle, Navigator nav, String name) {
//    GhostRobot robot = new GhostRobot(name, new SwingGridView());
//    
//   
//    SimulatedEntity ent = new SimulatedEntity(robot);
//    
//    robot.useNavigator(nav);
//    robot.useRobotAPI(new SimulationRobotAPI().setSimulatedEntity(ent));
//    robot.useGatewayClient(new SimulatedGatewayClient());
//    
//    ent.setPostition(x, y, angle);
//    sim.addSimulatedEntity(ent);
//
//    return ent;
//  }
}
