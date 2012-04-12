package penoplatinum.navigator;

/**
 * Navigator Interface
 * 
 * A Navigator implements how the robot tries to reach its goal. As with a 
 * true Navigator it is asked to instruct a Driver. To
 * 
 * @author: Team Platinum
 */

import penoplatinum.driver.Driver;

import penoplatinum.model.Model;


public interface Navigator {

  // based on the information in the Model, the Navigator decides where to go
  public Navigator useModel(Model model);

  // the navigator can be asked to instruct a driver what to do
  public Navigator instruct(Driver driver);

  // when the Navigator has reached its goal, it will no longer instruct any
  // movement to the Driver, but will also provide this information as feedback
  public boolean reachedGoal();

}
