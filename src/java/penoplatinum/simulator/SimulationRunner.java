package penoplatinum.simulator;

import penoplatinum.simulator.tiles.Panels;
import penoplatinum.simulator.view.SwingSimulationView;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import org.apache.commons.cli.*;
import penoplatinum.map.MapFactorySector;

/**
 * SimulationRunner
 * 
 * Constructs a robot and course, sets up the Simulator and starts the
 * simulation.
 * 
 * @author: Team Platinum
 */
 
public class SimulationRunner {
  
  private Simulator simulator;
  private String navigator;
  
  public SimulationRunner() {
    this.simulator = new Simulator();
    useMap(createDefaultMap() );
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
    Map m = createDefaultMap();
    if(!file.equals("defaultMap")){
      File f = new File(file);
      try {
        Scanner sc = new Scanner(f);
        MapFactorySector fact = new MapFactorySector();
        m = fact.getMap(sc);
      } catch (FileNotFoundException ex) {
        System.err.println("File not found, using default map!");
      }
    }
    return useMap(m);
  }
  
  // TODO: create MapFactory with support to load files
  //       add option to provide map file
  public static Map createDefaultMap() {
    Map map = new Map(4)
      .add(Panels.S_E) .add(Panels.W_E) .add(Panels.W_E) .add(Panels.W_S)
      .add(Panels.E_N) .add(Panels.E_W) .add(Panels.S_W) .add(Panels.N_S)
      .add(Panels.S_E) .add(Panels.W_E) .add(Panels.W_N) .add(Panels.N_S)
      .add(Panels.E_N) .add(Panels.E_W) .add(Panels.E_W) .add(Panels.N_W);
    return map;
  }
  
  private static Map michielMap(){
    Map map = new Map(4)
      .add(Panels.S_E) .add(Panels.W_E) .add(Panels.W_E.withoutBarcodeLocation()) .add(Panels.W_S)
      .add(Panels.E_N.withoutBarcodeLocation()) .add(Panels.E_W) .add(Panels.S_W) .add(Panels.N_S.withoutBarcodeLocation())
      .add(Panels.S_E.withoutLine(Baring.N).withoutLine(Baring.W)) .add(Panels.W_E) .add(Panels.W_N.setNarrowingOrientation(Baring.W)) .add(Panels.N_S)
      .add(Panels.E_N) .add(Panels.E_W) .add(Panels.E_W) .add(Panels.N_W);
    return map;
  }
  
  public SimulationRunner useUI() {
    this.simulator.displayOn(new SwingSimulationView());
    return this;
  }
  
  public SimulationRunner useNavigator(String navigator){
    this.navigator = navigator;
    return this;
  }

  public Navigator getNavigator() {
    try {
      Class theClass  = Class.forName(navigator);
      return (Navigator)theClass.newInstance();
    } catch ( ClassNotFoundException ex ){
      System.err.println( ex + " Navigator class must be in class-path.");
    } catch( InstantiationException ex ){
      System.err.println( ex + " Navigator class must be concrete.");
    } catch( IllegalAccessException ex ){
      System.err.println( ex + " Navigator class must have a no-arg constr.");
    }
    return null;
  }

  public SimulationRunner putRobotAt(String position) {
    // parse position string
    String[] parts = position.split(",");
    int x         = Integer.parseInt(parts[0]),
        y         = Integer.parseInt(parts[1]),
        direction = Integer.parseInt(parts[2]);   
    // put the robot
    if( this.navigator != null ) {
      Robot robot = new NavigatorRobot(getNavigator());
      SimulatedEntity entity = new SimulatedEntity(new SimulationRobotAPI(), new SimulationRobotAgent("PlatinumSimulated"), robot);
      robot.useRobotAPI(new SimulationRobotAPI().setSimulatedEntity(entity));
      entity.setPostition(x, y, direction);
      simulator.addSimulatedEntity(entity);
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
        if(line.getOptionValues("put") == null){
          runner.putRobotAt(defaultPosition);
        } else {
          for(String put : line.getOptionValues("put")){
            runner.putRobotAt(put);
          }
        }
        
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
