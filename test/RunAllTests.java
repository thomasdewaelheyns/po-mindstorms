/**
 * RunAllTests
 *
 * Suite for all tests.
 *
 * @author Team Platonum
 */

import junit.framework.*;

import penoplatinum.ConfigTest;

import penoplatinum.util.*;

import penoplatinum.protocol.*;

import penoplatinum.driver.*;
import penoplatinum.driver.action.*;
import penoplatinum.driver.behaviour.*;

import penoplatinum.reporter.*;

import penoplatinum.navigator.*;
import penoplatinum.navigator.action.*;
import penoplatinum.navigator.mode.*;

import penoplatinum.model.*;
import penoplatinum.model.part.*;
import penoplatinum.model.processor.*;

import penoplatinum.robot.*;

import penoplatinum.gateway.*;


public class RunAllTests {
  public static Test suite() {
    Class[] testClasses = { 
      penoplatinum.ConfigTest.class,
      penoplatinum.util.RotationTest.class,
      penoplatinum.util.BearingTest.class,
      penoplatinum.util.PointTest.class,
      penoplatinum.util.TransformationTRTTest.class,
      penoplatinum.util.ScannerTest.class,
      penoplatinum.util.ColorTest.class,
      penoplatinum.util.LightColorTest.class,
      penoplatinum.util.BitwiseOperationsTest.class,
      penoplatinum.grid.LinkedSectorTest.class,
      penoplatinum.protocol.ExternalEventHandlerTest.class,
      penoplatinum.protocol.ProtocolHandlerTest.class,
      penoplatinum.protocol.GhostProtocolHandlerTest.class,
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
      penoplatinum.gateway.MessageReceiverTest.class
    };
    TestSuite suite = new TestSuite(testClasses);
    return suite;
  }
}
