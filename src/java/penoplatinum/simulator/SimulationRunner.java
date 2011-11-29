package penoplatinum.simulator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.*;
import penoplatinum.map.MapFactory;

/**
 * SimulationRunner
 * 
 * Constructs a robot and course, sets up the Simulator and starts the
 * simulation.
 * 
 * Author: Team Platinum
 */
 
public class SimulationRunner {
  
  private Simulator simulator;
  private Robot     robot;
  private Navigator navigator;
  
  public SimulationRunner() {
    this.simulator = new Simulator();
    useMap(defaultMap());
  }
  
  public SimulationRunner(Map m) {
    this.simulator = new Simulator();
    this.simulator.useMap(m);
  }
  
  public final SimulationRunner useMap(Map m){
    this.simulator.useMap(m);
    return this;
  }
  public SimulationRunner useMap(String file){
    Map m = defaultMap();
    if(!file.equals("defaultMap")){
      File f = new File(file);
      try {
        Scanner sc = new Scanner(f);
        MapFactory fact = new MapFactory();
        m = fact.getMap(sc);
      } catch (FileNotFoundException ex) {
        System.err.println("File not found, using default map!");
      }
    }
    return useMap(m);
  }
  
  // TODO: create MapFactory with support to load files
  //       add option to provide map file
  private Map defaultMap() {
    Map map = new Map(4)
      .add(Tiles.S_E) .add(Tiles.W_E) .add(Tiles.W_E) .add(Tiles.W_S)
      .add(Tiles.E_N) .add(Tiles.E_W) .add(Tiles.S_W) .add(Tiles.N_S)
      .add(Tiles.S_E) .add(Tiles.W_E) .add(Tiles.W_N) .add(Tiles.N_S)
      .add(Tiles.E_N) .add(Tiles.E_W) .add(Tiles.E_W) .add(Tiles.N_W);
    return map;
  }
  
  public SimulationRunner useUI() {
    this.simulator.displayOn(new SwingSimulationView());
    return this;
  }

  public SimulationRunner useNavigator(String navigator) {
    try {
      Class theClass  = Class.forName(navigator);
      this.navigator = (Navigator)theClass.newInstance();
    } catch ( ClassNotFoundException ex ){
      System.err.println( ex + " Navigator class must be in class-path.");
    } catch( InstantiationException ex ){
      System.err.println( ex + " Navigator class must be concrete.");
    } catch( IllegalAccessException ex ){
      System.err.println( ex + " Navigator class must have a no-arg constr.");
    }
    return this;
  }

  public SimulationRunner putRobotAt(String position) {
    // parse position string
    String[] parts = position.split(",");
    int x         = Integer.parseInt(parts[0]),
        y         = Integer.parseInt(parts[1]),
        direction = Integer.parseInt(parts[2]);   
    // put the robot
    if( this.navigator != null ) {
      Robot robot = new NavigatorRobot(this.navigator);
      this.simulator.putRobotAt(robot, x, y, direction);
    } else {
      System.err.println( "Please provide a navigator, then add a robot." );
    }
    return this;
  }
  
  public void start() {
    this.simulator.run();
  }
  
  public static void showHelpFor(Options options) {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp( "SimulationRunner", options );    
  }

  public static void main(String[] args) {
    String defaultNavigator = "penoplatinum.navigators.SonarNavigator";
    String defaultPosition  = "150,150,270";
    
    SimulationRunner runner = new SimulationRunner();
    Boolean setupComplete = false;
    
    // process command line arguments
    Options options = new Options();
    //options.addOption("m", "mapFactory", false, "testing");
    options.addOption( "h", "help",  false, "show this helpful information." );
    options.addOption( "q", "quiet", false, "don't show a user interface." );

    options.addOption( "n", "navigator",true, "use navigator <classname>. " +
                                              "default=" + defaultNavigator );
    options.addOption( "p", "put",      true, "put robot at x,y,direction. " +
                                              "default=" + defaultPosition  );
    options.addOption( "m", "mapFile",true, "use mapfile. " +
                                              "default=defaultMap");

    CommandLineParser parser = new GnuParser();
    try {
      CommandLine line = parser.parse( options, args );

      if( line.hasOption("help") ) { 
        SimulationRunner.showHelpFor(options);
      } else {
        if( ! line.hasOption("q") ) { runner.useUI(); }

        runner.useNavigator( line.getOptionValue( "navigator", 
                                                  defaultNavigator ) );
        runner.putRobotAt( line.getOptionValue( "put", defaultPosition ) );
        
        runner.useMap(line.getOptionValue("mapFile", "defaultMap"));

        setupComplete = true;
      }
    } catch( ParseException exp ) {
      System.err.println( "ERROR:" + exp.getMessage() );
      SimulationRunner.showHelpFor(options);
    }

    if( setupComplete ) { runner.start(); }
  }
}
