package penoplatinum.fulltests;

import penoplatinum.driver.ManhattanDriver;
import penoplatinum.driver.behaviour.LineDriverBehaviour;

public class LineDriver extends ManhattanDriver {

  public LineDriver(double sectorSize) {
    super(sectorSize);
    addBehaviour(new LineDriverBehaviour());
  }

}
