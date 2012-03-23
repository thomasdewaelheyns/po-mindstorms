/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.simulator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import org.junit.*;
import static org.junit.Assert.*;
import penoplatinum.map.Map;
import penoplatinum.map.MapFactory;
import penoplatinum.map.MapFactorySector;
import penoplatinum.map.mazeprotocolinterpreter.ProtocolMapFactory;

/**
 *
 * @author MHGameWork
 */
public class SimulatorTest {

  @Test
  public void testFindHitDistance() throws FileNotFoundException {
    Simulator sim = new Simulator();
    Map m = createSectorMap();

    sim.useMap(m);

    assertEquals(20, sim.findHitDistance(180, 1, 1, 20, 20));
    assertEquals(100, sim.findHitDistance(270, 1, 1, 20, 20));
  }

  public static Map createSectorMap() throws FileNotFoundException {
    Map m;
    File f = new File("..\\..\\maps\\map2.track");
    Scanner sc = new Scanner(f);
    MapFactorySector fact = new MapFactorySector();
    m = fact.getMap(sc);
    return m;
  }

  public static Map createSectorMap2() throws FileNotFoundException {
    Map m;
    File f = new File("..\\..\\maps\\map3.track");
    Scanner sc = new Scanner(f);
    MapFactorySector fact = new MapFactorySector();
    m = fact.getMap(sc);
    return m;
  }

  public static Map createSectorMazeProtocol() throws FileNotFoundException {
    Map m;
    File f = new File("..\\..\\maps\\wolfraam.txt");
    Scanner sc = new Scanner(f);
    MapFactory fact = new ProtocolMapFactory();
    m = fact.getMap(sc);
    return m;
  }

  public static Map createSectorMazeProtocol2() throws FileNotFoundException {
    Map m;
    File f = new File("..\\..\\maps\\wolfraam3.txt");
    Scanner sc = new Scanner(f);
    MapFactory fact = new ProtocolMapFactory();
    m = fact.getMap(sc);
    return m;
  }
}
