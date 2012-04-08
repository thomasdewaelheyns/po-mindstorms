/**
 * RunAllTests
 *
 * Suite for all tests.
 *
 * @author Team Platonum
 */

import junit.framework.*;

import penoplatinum.ConfigTest;

import penoplatinum.util.RotationTest;
import penoplatinum.util.BearingTest;
import penoplatinum.util.PointTest;
import penoplatinum.util.TransformationTRTTest;
import penoplatinum.util.ScannerTest;
import penoplatinum.util.ColorTest;
import penoplatinum.util.BitwiseOperationsTest;

import penoplatinum.grid.LinkedSectorTest;

import penoplatinum.protocol.ProtocolHandlerTest;
import penoplatinum.protocol.GhostProtocolHandlerTest;

import penoplatinum.driver.DriverTest;
import penoplatinum.driver.ManhattanDriverTest;

import penoplatinum.driver.action.DriverActionTest;
import penoplatinum.driver.action.IdleDriverActionTest;
import penoplatinum.driver.action.MoveDriverActionTest;
import penoplatinum.driver.action.TurnDriverActionTest;
import penoplatinum.driver.action.CombinedDriverActionTest;

import penoplatinum.driver.behaviour.DriverBehaviourTest;
import penoplatinum.driver.behaviour.SideProximityDriverBehaviourTest;
import penoplatinum.driver.behaviour.FrontProximityDriverBehaviourTest;
import penoplatinum.driver.behaviour.BarcodeDriverBehaviourTest;

import penoplatinum.reporter.ReporterTest;

import penoplatinum.navigator.NavigatorTest;

import penoplatinum.model.ModelTest;


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
      penoplatinum.util.BitwiseOperationsTest.class,
      penoplatinum.grid.LinkedSectorTest.class,
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
      penoplatinum.model.ModelTest.class
    };
    TestSuite suite = new TestSuite(testClasses);
    return suite;
  }
}
