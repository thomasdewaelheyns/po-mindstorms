package penoplatinum.simulator;

import penoplatinum.Utils;

/**
 * SimulationRunner
 * 
 * Constructs a robot and course, sets up the Simulator and starts the
 * simulation.
 * 
 * Future improvement: Use DI (Spring,...) framework to externalize the wiring.
 *
 * Author: Team Platinum
 */

class SimulationRunner {

  public static void main(String[] args) {
    // setup the robot
    //Navigator n = new BumperNavigator2Sensor();
    Navigator n = new SonarNavigator();
    Robot robot = new NavigatorRobot(n);

    // construct a course
    Map map = new Map(4)
      .add(new Tile( 9)
              .withBarcode(Barcode.Right)     .withBarcodeLocation(Baring.S)
              .withLine(Baring.N, Tile.WHITE) .withLine(Baring.W, Tile.WHITE)
          )
      .add(new Tile( 5)
              .withBarcode(Barcode.Forward)   .withBarcodeLocation(Baring.W)
              .withLine(Baring.N, Tile.WHITE) .withLine(Baring.S, Tile.BLACK)
          )
      .add(new Tile( 5)
              .withBarcode(Barcode.Forward)   .withBarcodeLocation(Baring.W)
              .withLine(Baring.N, Tile.WHITE) .withLine(Baring.S, Tile.BLACK)
          )
      .add(new Tile( 3)
              .withBarcode(Barcode.Right)     .withBarcodeLocation(Baring.W)
              .withLine(Baring.N, Tile.WHITE) .withLine(Baring.E, Tile.WHITE)
          )
       // row 2
      .add(new Tile(12)
              .withBarcode(Barcode.Right)     .withBarcodeLocation(Baring.E)
              .withLine(Baring.W, Tile.WHITE) .withLine(Baring.S, Tile.WHITE)
          )
      .add(new Tile( 5)
              .withBarcode(Barcode.Forward)   .withBarcodeLocation(Baring.E)
              .withLine(Baring.N, Tile.BLACK) .withLine(Baring.S, Tile.WHITE)
          )
      .add(new Tile( 3)
              .withBarcode(Barcode.Left)      .withBarcodeLocation(Baring.S)
              .withLine(Baring.N, Tile.BLACK) .withLine(Baring.E, Tile.BLACK)
          )
      .add(new Tile(10)
              .withBarcode(Barcode.Forward).withBarcodeLocation(Baring.N)
              .withLine(Baring.W, Tile.BLACK) .withLine(Baring.E, Tile.WHITE)
          )
       // row 3
      .add(new Tile( 9)
              .withBarcode(Barcode.Right)     .withBarcodeLocation(Baring.S)
              .withLine(Baring.W, Tile.WHITE) .withLine(Baring.N, Tile.WHITE)
          )
      .add(new Tile( 5)
              .withBarcode(Barcode.Forward)   .withBarcodeLocation(Baring.W)
              .withLine(Baring.N, Tile.WHITE) .withLine(Baring.S, Tile.BLACK)
          )
      .add(new Tile( 6)
              .withBarcode(Barcode.Left)      .withBarcodeLocation(Baring.W)
              .withLine(Baring.E, Tile.BLACK) .withLine(Baring.S, Tile.BLACK)
          )
      .add(new Tile(10)
              .withBarcode(Barcode.Forward)   .withBarcodeLocation(Baring.N)
              .withLine(Baring.W, Tile.BLACK) .withLine(Baring.E, Tile.WHITE)
          )
       // row 4
      .add(new Tile(12)
              .withBarcode(Barcode.Right)     .withBarcodeLocation(Baring.E)
              .withLine(Baring.W, Tile.WHITE) .withLine(Baring.S, Tile.WHITE)
          )
      .add(new Tile( 5)
              .withBarcode(Barcode.Forward)   .withBarcodeLocation(Baring.E)
              .withLine(Baring.N, Tile.BLACK) .withLine(Baring.S, Tile.WHITE)
          )
      .add(new Tile( 5)
              .withBarcode(Barcode.Forward)   .withBarcodeLocation(Baring.E)
              .withLine(Baring.N, Tile.BLACK) .withLine(Baring.S, Tile.WHITE)
          )
      .add(new Tile( 6)
              .withBarcode(Barcode.Right)     .withBarcodeLocation(Baring.N)
              .withLine(Baring.E, Tile.WHITE) .withLine(Baring.S, Tile.WHITE)
          );

    // Future implementation
    // Map map = Map.fromFile("map.txt");  // load a map from a file

    // setup the simulator, (optionally) providing a view and add the course
    // and the robot
    Simulator simulator = new Simulator();
    simulator.displayOn(new SwingSimulationView());
    simulator.useMap   (map);
    // put the robot at position 50 cm from top, 50 cm from left, in an
    // angle of 33 degrees, with 0 degrees pointing north
    Utils.Sleep(10000);
    simulator.putRobotAt (robot, 150, 150, 33);

    // give robot instructions through the communication layer
    // simulator.send( "22;0.05" ); // run polygon with 22 vertexes of 5cm

    // start the simulator and enjoy the ride
    simulator.run();
  }

}
