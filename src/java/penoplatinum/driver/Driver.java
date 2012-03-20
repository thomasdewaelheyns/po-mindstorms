package penoplatinum.driver;

/**
 * Driver
 * 
 * Counterpart of the Navigator, focussing on just driving optimally.
 * 
 * @author: Team Platinum
 */
 
import penoplatinum.simulator.Model;
import penoplatinum.simulator.RobotAPI;

public interface Driver {
  public Driver useModel(Model model);
  public Driver useRobotAPI(RobotAPI api);

  public boolean isBusy();

  public void step();

  public void perform(int action);

}
