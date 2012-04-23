
/**
 * RunAllTests
 *
 * Suite for all tests.
 *
 * @author Team Platonum
 */
import junit.framework.*;

public class RunAllTests {

  public static Test suite() {
    Class[] testClasses = {
      penoplatinum.ConfigTest.class,
      penoplatinum.util.BearingTest.class,
      penoplatinum.util.BitwiseOperationsTest.class,
      penoplatinum.util.CantorDiagonalTest.class,
      penoplatinum.util.CircularQueueTest.class,
      penoplatinum.util.ColorTest.class,
      penoplatinum.util.LightColorTest.class,
      penoplatinum.util.PointTest.class,
      penoplatinum.util.RotationTest.class,
      penoplatinum.util.ScannerTest.class,
      penoplatinum.util.SimpleHashMapTest.class,
      penoplatinum.util.SimpleHashMapTest2.class,
      penoplatinum.util.TransformationTRTTest.class,
      penoplatinum.util.UtilsTest.class,
      //      penoplatinum.barcode.BarcodeTest.class,
      penoplatinum.grid.LinkedSectorTest.class,
      //penoplatinum.grid.GridTest.class,
      penoplatinum.protocol.ExternalEventHandlerTest.class,
      penoplatinum.protocol.ProtocolHandlerTest.class,
      //penoplatinum.protocol.GhostProtocolHandlerTest.class,
      penoplatinum.driver.DriverTest.class,
      penoplatinum.driver.ManhattanDriverTest.class,
      penoplatinum.driver.action.DriverActionTest.class,
      penoplatinum.driver.action.IdleDriverActionTest.class,
      penoplatinum.driver.action.MoveDriverActionTest.class,
      penoplatinum.driver.action.TurnDriverActionTest.class,
      penoplatinum.driver.action.CombinedDriverActionTest.class,
      penoplatinum.driver.behaviour.DriverBehaviourTest.class,
      penoplatinum.driver.behaviour.SideProximityDriverBehaviourTest.class,
      penoplatinum.driver.behaviour.FrontProximityDriverBehaviourTest.class,
      penoplatinum.driver.behaviour.BarcodeDriverBehaviourTest.class,
      penoplatinum.reporter.ReporterTest.class,
      penoplatinum.navigator.NavigatorTest.class,
      penoplatinum.navigator.GhostNavigatorTest.class,
      penoplatinum.navigator.MultiModeNavigatorTest.class,
      penoplatinum.navigator.action.NavigatorActionTest.class,
      penoplatinum.navigator.action.ForwardNavigatorActionTest.class,
      penoplatinum.navigator.action.TurnLeftNavigatorActionTest.class,
      penoplatinum.navigator.action.TurnRightNavigatorActionTest.class,
      penoplatinum.navigator.mode.NavigatorModeTest.class,
      penoplatinum.navigator.mode.HillClimbingNavigatorModeTest.class,
      penoplatinum.navigator.mode.DiscoverHillClimbingNavigatorModeTest.class,
      penoplatinum.navigator.mode.ChaseHillClimbingNavigatorModeTest.class,
      penoplatinum.model.ModelTest.class,
      penoplatinum.model.part.ModelPartTest.class,
      penoplatinum.model.part.ModelPartRegistryTest.class,
      penoplatinum.model.processor.ModelProcessorTest.class,
      penoplatinum.robot.RobotTest.class,
      penoplatinum.robot.RobotAPITest.class,
      penoplatinum.gateway.ConnectionTest.class,
      penoplatinum.gateway.QueueTest.class,
      penoplatinum.gateway.GatewayClientTest.class,
      penoplatinum.gateway.MessageReceiverTest.class, //      penoplatinum.simulator.tiles.SectorTest.class, 
    //      penoplatinum.simulator.sensors.NoneSensorTest.class,
    //      penoplatinum.simulator.tiles.TileGeometryTest.class,
    //      penoplatinum.map.MapHashedTest.class,
    //      penoplatinum.map.mazeprotocol.BarcodeAtTest.class,
    //      penoplatinum.map.mazeprotocol.DiscoverTest.class,
    //      penoplatinum.map.mazeprotocol.PacmanTest.class,
    //      penoplatinum.map.mazeprotocol.PositionTest.class,
    //      penoplatinum.map.mazeprotocol.ProtocolMapFactoryTest.class
    };
    TestSuite suite = new TestSuite(testClasses);
    return suite;
  }
}
