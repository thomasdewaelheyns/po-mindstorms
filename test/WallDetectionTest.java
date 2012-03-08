
import java.io.FileNotFoundException;
import org.junit.Test;
import penoplatinum.Utils;
import penoplatinum.navigators.BehaviourNavigator;
import penoplatinum.pacman.GhostModel;
import penoplatinum.simulator.NavigatorRobot;
import penoplatinum.simulator.SimulatedEntity;
import penoplatinum.simulator.SimulationRobotAPI;
import penoplatinum.simulator.SimulationRobotAgent;
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
    Simulator sim = new Simulator();
    sim.useMap(SimulatorTest.createSectorMap());

    final NavigatorRobot robot = new NavigatorRobot();
    robot.useNavigator(new BehaviourNavigator());
    SimulatedEntity ent = new SimulatedEntity(new SimulationRobotAPI(), new SimulationRobotAgent(), robot);
    ent.setPostition(20, 20, 0);
    sim.addSimulatedEntity(ent);

    sim.useStepRunnable(new Runnable() {

      @Override
      public void run() {
        if (!((GhostModel) robot.getModel()).hasUpdatedSonarValues()) {
          return;
        }
        String s = "";
        s += "Left: " + (((GhostModel) robot.getModel()).isWallLeft() ? "XXX" : "   ");
        s += " Front: " + (((GhostModel) robot.getModel()).isWallFront() ? "XXX" : "   ");
        s += " Right: " + (((GhostModel) robot.getModel()).isWallRight() ? "XXX" : "   ");

        System.out.println(s);
        Utils.Sleep(40);
      }
    });

    sim.displayOn(new SwingSimulationView());
    sim.run();


  }

}
