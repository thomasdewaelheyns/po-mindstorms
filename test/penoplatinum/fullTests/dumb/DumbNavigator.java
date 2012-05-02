package penoplatinum.fullTests.dumb;

import penoplatinum.driver.Driver;
import penoplatinum.driver.ManhattanDriver;
import penoplatinum.model.Model;
import penoplatinum.navigator.Navigator;

public class DumbNavigator implements Navigator {
  int state = 0;

  @Override
  public Navigator useModel(Model model) {
    return this;
  }

  @Override
  public Navigator instruct(Driver driver) {
    if(state == 12 || state == -1){
      state = -1;
      return this;
    } else if(state % 2 == 0){
      ((ManhattanDriver)driver).move();
    } else if(state % 2 == 1){
      ((ManhattanDriver)driver).turnRight();
    }
    state++;
    return this;
  }

  @Override
  public boolean reachedGoal() {
    return state == -1;
  }

  @Override
  public Navigator finish(Driver driver) {
    //do nothing
    return this;
  }
  
}
