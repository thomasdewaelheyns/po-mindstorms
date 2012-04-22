package penoplatinum;


import java.io.FileNotFoundException;
import org.junit.Test;
import penoplatinum.util.Utils;
//import penoplatinum.navigators.BehaviourNavigator;
import penoplatinum.model.GhostModel;
import penoplatinum.simulator.entities.SimulatedEntity;
import penoplatinum.robot.SimulationRobotAPI;
import penoplatinum.simulator.SimulatedGatewayClient;
import penoplatinum.simulator.Simulator;
import penoplatinum.simulator.SimulatorTest;
import penoplatinum.simulator.view.SwingSimulationView;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author MHGameWork
 */
public class WallDetectionTest {

  @Test
  public void testOutputWalls() throws FileNotFoundException {
    throw new UnsupportedOperationException();
//    Simulator sim = new Simulator();
//    sim.useMap(SimulatorTest.createSectorMap());
//
//    final NavigatorRobot robot = new NavigatorRobot();
//    robot.useNavigator(new BehaviourNavigator());
//    SimulatedEntity ent = new SimulatedEntity(new SimulationRobotAPI(), new SimulatedGatewayClient(), robot);
//    ent.setPostition(20, 20, 0);
//    sim.addSimulatedEntity(ent);
//
//    sim.useStepRunnable(new Runnable() {
//
//      @Override
//      public void run() {
//        if (!((GhostModel) robot.getModel()).hasUpdatedSonarValues()) {
//          return;
//        }
//        String s = "";
//        s += "Left: " + (((GhostModel) robot.getModel()).isWallLeft() ? "XXX" : "   ");
//        s += " Front: " + (((GhostModel) robot.getModel()).isWallFront() ? "XXX" : "   ");
//        s += " Right: " + (((GhostModel) robot.getModel()).isWallRight() ? "XXX" : "   ");
//
//        System.out.println(s);
//        Utils.Sleep(40);
//      }
//    });
//
//    sim.displayOn(new SwingSimulationView());
//    sim.run();


  }

}
