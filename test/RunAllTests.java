
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
      penoplatinum.bluetooth.IConnectionTest.class,
      penoplatinum.bluetooth.IPacketHandlerTest.class,
      penoplatinum.bluetooth.IPacketReceiverTest.class,
      penoplatinum.bluetooth.IPacketTransporterTest.class,
      penoplatinum.bluetooth.IRemoteLoggerCallbackTest.class,
      penoplatinum.driver.action.subaction.FindLeftWhiteLineTest.class, 
      penoplatinum.driver.action.subaction.FindRightWhiteLineTest.class, 
      penoplatinum.driver.action.subaction.ReturnSubActionTest.class, 
      penoplatinum.driver.action.subaction.SubActionTest.class,
      penoplatinum.driver.action.subaction.TurnToAlignTest.class,
      penoplatinum.driver.action.AlignDriverActionTest.class,
      penoplatinum.driver.action.CombinedDriverActionTest.class,
      penoplatinum.driver.action.DriverActionTest.class,
      penoplatinum.driver.action.IdleDriverActionTest.class,
      penoplatinum.driver.action.MoveDriverActionTest.class,
      penoplatinum.driver.action.TurnDriverActionTest.class,
      penoplatinum.driver.behaviour.BarcodeDriverBehaviourTest.class,
      penoplatinum.driver.behaviour.DriverBehaviourTest.class,
      penoplatinum.driver.behaviour.FrontProximityDriverBehaviourTest.class,
      penoplatinum.driver.behaviour.SideProximityDriverBehaviourTest.class,
      penoplatinum.driver.DriverTest.class,
      penoplatinum.driver.ManhattanDriverTest.class,
      penoplatinum.gateway.ConnectionTest.class,
      penoplatinum.gateway.GatewayClientTest.class,
      penoplatinum.gateway.MessageReceiverTest.class,
      penoplatinum.gateway.QueueTest.class,
      penoplatinum.grid.agent.AgentTest.class,
      penoplatinum.grid.AggregatedGridTest.class,
      penoplatinum.grid.AggregatedSectorTest.class,
      penoplatinum.grid.agent.BarcodeAgentTest.class,
      penoplatinum.grid.agent.GhostAgentTest.class,
      penoplatinum.grid.GridObserverTest.class,
      penoplatinum.grid.GridTest.class,
      penoplatinum.grid.GridViewTest.class,
      penoplatinum.grid.LinkedGridTest.class,
      penoplatinum.grid.LinkedSectorTest.class,
      penoplatinum.grid.agent.MovingAgentTest.class,
      penoplatinum.grid.NullGridTest.class,
      penoplatinum.grid.agent.PacmanAgentTest.class,
      penoplatinum.grid.agent.ProxyAgentTest.class,
      penoplatinum.grid.SectorTest.class,
      penoplatinum.grid.agent.StaticTargetAgentTest.class,
      penoplatinum.grid.TransformedGridTest.class,
      penoplatinum.grid.FacadeSectorTest.class,
      penoplatinum.grid.MultiGhostGridTest.class,
      penoplatinum.map.mazeprotocol.BarcodeAtTest.class,
      penoplatinum.map.mazeprotocol.CommandoTest.class,
      penoplatinum.map.mazeprotocol.DiscoverTest.class,
      penoplatinum.map.mazeprotocol.NameTest.class,
      penoplatinum.map.mazeprotocol.PacmanTest.class,
      penoplatinum.map.mazeprotocol.PositionTest.class,
      penoplatinum.map.mazeprotocol.ProtocolMapFactoryTest.class,
      penoplatinum.map.MapHashedTest.class,
      penoplatinum.map.MapTest.class,
      // penoplatinum.map.MapTestUtil.class,  <<-- NOT A TEST
      penoplatinum.map.MapUtilTest.class, // <<--- This is
      penoplatinum.model.part.BarcodeModelPartTest.class,
      penoplatinum.model.part.BarcodeTest.class,
      penoplatinum.model.part.LightModelPartTest.class,
      penoplatinum.model.part.MessageModelPartTest.class,
      penoplatinum.model.part.ModelPartRegistryTest.class,
      penoplatinum.model.part.ModelPartTest.class,
      penoplatinum.model.part.SensorModelPartTest.class,
      penoplatinum.model.part.SonarModelPartTest.class,
      penoplatinum.model.part.WallsModelPartTest.class,
      penoplatinum.model.processor.BarcodeModelProcessorTest.class,
      penoplatinum.model.processor.BarcodeWallsModelProcessorTest.class,
      penoplatinum.model.processor.ChangesModelProcessorTest.class,
      penoplatinum.model.processor.FreeDistanceModelProcessorTest.class,
      penoplatinum.model.processor.IRModelProcessorTest.class,
      penoplatinum.model.processor.ImportWallsModelProcessorTest.class,
      penoplatinum.model.processor.InboxModelProcessorTest.class,
      penoplatinum.model.processor.LightModelProcessorTest.class,
      penoplatinum.model.processor.ModelProcessorTest.class,
      penoplatinum.model.processor.UnknownSectorModelProcessorTest.class,
      penoplatinum.model.processor.WallDetectionModelProcessorTest.class,
      penoplatinum.model.GhostModelTest.class,
      penoplatinum.model.ModelTest.class,
      penoplatinum.navigator.action.ForwardNavigatorActionTest.class,
      penoplatinum.navigator.action.NavigatorActionTest.class,
      penoplatinum.navigator.action.TurnLeftNavigatorActionTest.class,
      penoplatinum.navigator.action.TurnRightNavigatorActionTest.class,
      penoplatinum.navigator.mode.ChaseHillClimbingNavigatorModeTest.class,
      penoplatinum.navigator.mode.DiscoverHillClimbingNavigatorModeTest.class,
      penoplatinum.navigator.mode.HillClimbingNavigatorModeTest.class,
      penoplatinum.navigator.mode.NavigatorModeTest.class,
      penoplatinum.navigator.GhostNavigatorTest.class,
      penoplatinum.navigator.MultiModeNavigatorTest.class,
      penoplatinum.navigator.NavigatorTest.class,
      penoplatinum.protocol.ExternalEventHandlerTest.class,
      penoplatinum.protocol.GhostProtocolHandlerTest.class,
      penoplatinum.protocol.ProtocolHandlerTest.class,
      penoplatinum.reporter.DashboardReporterTest.class,
      penoplatinum.reporter.ReporterTest.class,
      penoplatinum.robot.RobotAPITest.class,
      penoplatinum.robot.RobotTest.class,
      penoplatinum.simulator.entities.PacmanEntityTest.class,
      penoplatinum.simulator.entities.PacmanViewRobotTest.class,
      penoplatinum.simulator.entities.SimulatedEntityTest.class,
      penoplatinum.simulator.sensors.IRSensorTest.class,
      penoplatinum.simulator.sensors.IRdistanceSensorTest.class,
      penoplatinum.simulator.sensors.LightSensorTest.class,
      penoplatinum.simulator.sensors.MotorStateTest.class,
      penoplatinum.simulator.sensors.MotorTest.class,
      penoplatinum.simulator.sensors.NoneSensorTest.class,
      penoplatinum.simulator.sensors.SonarTest.class,
      penoplatinum.simulator.sensors.TickableTest.class,
      //penoplatinum.simulator.tiles.DistanceTest.class,
      //penoplatinum.simulator.tiles.SectorDrawTest.class, <<-- TODO ?
      penoplatinum.simulator.tiles.SectorTest.class,
      penoplatinum.simulator.tiles.SectorsTest.class,
      penoplatinum.simulator.tiles.TileDrawTest.class,
      //penoplatinum.simulator.tiles.TileGeometryTest.class,
      penoplatinum.simulator.tiles.TileTest.class,
      penoplatinum.simulator.RobotEntityTest.class,
      penoplatinum.simulator.SensorTest.class,
      penoplatinum.simulator.SimulatorTest.class,
      penoplatinum.ui.admin.RobotAdminClientTest.class,
      penoplatinum.ui.admin.RobotAdminShellTest.class,
      penoplatinum.util.BearingTest.class,
      penoplatinum.util.BitwiseOperationsTest.class,
      penoplatinum.util.CantorDiagonalTest.class,
      penoplatinum.util.CircularQueueTest.class,
      penoplatinum.util.ColorTest.class,
      penoplatinum.util.ColorsTest.class,
      penoplatinum.util.FPSTest.class,
      penoplatinum.util.LightColorTest.class,
      penoplatinum.util.LineTest.class,
      penoplatinum.util.PointTest.class,
      penoplatinum.util.PositionTest.class,
      penoplatinum.util.RotationTest.class,
      penoplatinum.util.ScannerTest.class,
      penoplatinum.util.SimpleHashMapTest.class,
      penoplatinum.util.TransformationTRTTest.class,
      penoplatinum.util.TransformationTest.class,
      penoplatinum.util.UtilsTest.class
    };
    TestSuite suite = new TestSuite(testClasses);
    return suite;
  }
}
