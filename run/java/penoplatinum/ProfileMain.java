package penoplatinum;




import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import penoplatinum.map.Map;
import penoplatinum.map.MapFactorySector;

/**
 * SimulationRunner
 * 
 * Constructs a robot and course, sets up the Simulator and starts the
 * simulation.
 * 
 * @author: Team Platinum
 */
public class ProfileMain {

  public static Map createSectorMap() throws FileNotFoundException {
    Map m;
    File f = new File("..\\..\\src\\java\\penoplatinum\\simulator\\map2.track");
    Scanner sc = new Scanner(f);
    MapFactorySector fact = new MapFactorySector();
    m = fact.getMap(sc);
    return m;
  }

  public static void main(String[] args) throws FileNotFoundException {
   GhostRobotTest test = new GhostRobotTest();
   test.testGhostRobotMazeProtocol3();
   

  }
}
