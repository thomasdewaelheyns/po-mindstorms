package penoplatinum.fullTests.barcode;

import penoplatinum.driver.ManhattanDriver;
import penoplatinum.driver.behaviour.BarcodeDriverBehaviour;
import penoplatinum.driver.behaviour.LineDriverBehaviour;

public class BarcodeDriver extends ManhattanDriver {

  public BarcodeDriver(double sectorSize) {
    super(sectorSize);
    addBehaviour(new LineDriverBehaviour());
    addBehaviour(new BarcodeDriverBehaviour());
  }
}
