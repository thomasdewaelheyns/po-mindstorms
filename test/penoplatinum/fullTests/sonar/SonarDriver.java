package penoplatinum.fullTests.sonar;

import penoplatinum.driver.behaviour.FrontProximityDriverBehaviour;
import penoplatinum.driver.behaviour.SideProximityDriverBehaviour;
import penoplatinum.fullTests.barcode.BarcodeDriver;

public class SonarDriver extends BarcodeDriver {

  public SonarDriver(double sectorSize) {
    super(sectorSize);
    addBehaviour(new FrontProximityDriverBehaviour());
    addBehaviour(new SideProximityDriverBehaviour());
  }
}
