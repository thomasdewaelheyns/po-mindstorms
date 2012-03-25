package penoplatinum.simulator;

/**
 * SimulationRunner
 * 
 * Constructs a robot and map, sets up the Simulator and starts the simulation
 * 
 * @author: Team Platinum
 */
import java.io.File;
import java.io.FileNotFoundException;

import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.cli.*;

import penoplatinum.simulator.tiles.Panels;
import penoplatinum.simulator.view.SwingSimulationView;

import penoplatinum.simulator.tiles.Sector;

import penoplatinum.map.MapArray;
import penoplatinum.map.MapFactory;
import penoplatinum.map.MapFactorySector;
import penoplatinum.map.mazeprotocolinterpreter.ProtocolMapFactory;

import penoplatinum.grid.SwingGridView;

import penoplatinum.driver.Driver;

import penoplatinum.util.Point;

public class SimulationRunner {

  // TODO move this to a properties-file
  private final static String DEFAULT_ROBOT = "penoplatinum.pacman.GhostRobot";
  private final static String DEFAULT_NAVIGATOR = "penoplatinum.pacman.GhostNavigator";
  private final static String DEFAULT_DRIVER = "penoplatinum.driver.GhostDriver";
  private final static String DEFAULT_GATEWAY_CLIENT = "penoplatinum.simulator.SimulatedGatewayClient";
  private final static String DEFAULT_MAP = "../../maps/wolfraam.txt";
  private Simulator simulator;
  private String robotClassName;
  private String navigatorClassName;
  private String driverClassName;
  private String gatewayClientClassName;
  private Map<String, Robot> robots = new HashMap<String, Robot>();
  private Map<String, Navigator> navigators = new HashMap<String, Navigator>();
  private Map<String, Driver> drivers = new HashMap<String, Driver>();
  private Map<String, RobotAgent> gatewayClients = new HashMap<String, RobotAgent>();
  private Map<String, RobotAPI> robotAPIs = new HashMap<String, RobotAPI>();
  private Map<String, RobotEntity> simulatedEntities = new HashMap<String, RobotEntity>();

  public SimulationRunner() {
    this.simulator = new Simulator();
  }

  public SimulationRunner useUI() {
    SwingSimulationView view = new SwingSimulationView();
    this.simulator.displayOn(view);


    return this;
  }

  // ROBOT
  public SimulationRunner useRobot(String robotClassName) {
    this.robotClassName = robotClassName;
    return this;
  }

  public Robot getRobot(String name) {
    if (!this.robots.containsKey(name)) {
      this.robots.put(name, this.createRobotInstance(name));
    }
    return this.robots.get(name);
  }

  @SuppressWarnings("unchecked")
  public Robot createRobotInstance(String name) {
    try {
      Class theClass = Class.forName(this.robotClassName);
      Constructor ctor = theClass.getConstructor(String.class);


      return (Robot) ctor.newInstance(name);
    } catch (InvocationTargetException ex) {
      System.err.println(ex + " could not create robot class.");
    } catch (NoSuchMethodException ex) {
      System.err.println(ex + " Robot class have constructor with String.");
    } catch (ClassNotFoundException ex) {
      System.err.println(ex + " Robot class must be in class-path.");
    } catch (InstantiationException ex) {
      System.err.println(ex + " Robot class must be concrete.");
    } catch (IllegalAccessException ex) {
      System.err.println(ex + " Robot class must have a no-arg constr.");
    }
    return null;
  }

  // NAVIGATOR
  public SimulationRunner useNavigator(String navigatorClassName) {
    this.navigatorClassName = navigatorClassName;
    return this;
  }

  public Navigator getNavigator(String name) {
    if (!this.navigators.containsKey(name)) {
      this.navigators.put(name, this.createNavigatorInstance(name));
    }
    return this.navigators.get(name);
  }

  public Navigator createNavigatorInstance(String name) {
    try {
      Class theClass = Class.forName(this.navigatorClassName);
      return (Navigator) theClass.newInstance();
    } catch (ClassNotFoundException ex) {
      System.err.println(ex + " Navigator class must be in class-path.");
    } catch (InstantiationException ex) {
      System.err.println(ex + " Navigator class must be concrete.");
    } catch (IllegalAccessException ex) {
      System.err.println(ex + " Navigator class must have a no-arg constr.");
    }
    return null;
  }

  // DRIVER
  public SimulationRunner useDriver(String driverClassName) {
    this.driverClassName = driverClassName;
    return this;
  }

  public Driver getDriver(String name) {
    if (!this.drivers.containsKey(name)) {
      this.drivers.put(name, this.createDriverInstance(name));
    }
    return this.drivers.get(name);
  }

  public Driver createDriverInstance(String name) {
    try {
      Class theClass = Class.forName(this.driverClassName);
      return (Driver) theClass.newInstance();
    } catch (ClassNotFoundException ex) {
      System.err.println(ex + " Driver class must be in class-path.");
    } catch (InstantiationException ex) {
      System.err.println(ex + " Driver class must be concrete.");
    } catch (IllegalAccessException ex) {
      System.err.println(ex + " Driver class must have a no-arg constr.");
    }
    return null;
  }

  // GATEWAYCLIENT
  public SimulationRunner useGatewayClient(String gatewayClientClassName) {
    this.gatewayClientClassName = gatewayClientClassName;
    return this;
  }

  public RobotAgent getGatewayClient(String name) {
    if (!this.gatewayClients.containsKey(name)) {
      this.gatewayClients.put(name, this.createGatewayClientInstance(name));
    }
    return this.gatewayClients.get(name);
  }

  public RobotAgent createGatewayClientInstance(String name) {
    try {
      Class theClass = Class.forName(this.gatewayClientClassName);
      return (RobotAgent) theClass.newInstance();
    } catch (ClassNotFoundException ex) {
      System.err.println(ex + " GatewayClient class must be in class-path.");
    } catch (InstantiationException ex) {
      System.err.println(ex + " GatewayClient class must be concrete.");
    } catch (IllegalAccessException ex) {
      System.err.println(ex + " GatewayClient class must have a no-arg constr.");
    }
    return null;
  }

  public SimulationRunner loadMap(String fileName) {
    try {
      File file = new File(fileName);
      penoplatinum.map.Map map = new ProtocolMapFactory().getMap(new Scanner(file));

      this.simulator.useMap(map);

      // add the ghosts
      int robotNr = 0;
      for (Point position : map.getGhostPositions()) {
        robotNr++;
        //this.putGhostAt("Ghost" + robotNr, position.getX(), position.getY(), Bearing.N);
        this.putGhostAt("" + robotNr, position.getX(), position.getY(), Bearing.N);
      }

      // add a pacman
      Point position = map.getPacmanPosition();
      if (position != null) {
        this.putPacmanAt(position.getX(), position.getY(), Bearing.N);
      }

    } catch (FileNotFoundException ex) {
      throw new RuntimeException("Map File not found: " + fileName);
    }
    return this;
  }

  public SimulationRunner putGhostAt(String name, int x, int y, int direction) {
    Robot robot = this.getRobot(name);
    Navigator navigator = this.getNavigator(name);
    Driver driver = this.getDriver(name);
    RobotAgent gatewayClient = this.getGatewayClient(name);

    // construct a simulatedEntity
    SimulatedEntity simulatedEntity = new SimulatedEntity(robot);
    simulatedEntity.setPostition(x * Sector.SIZE + Sector.SIZE / 2, y * Sector.SIZE + Sector.SIZE / 2, direction);
    this.simulatedEntities.put(name, simulatedEntity);

    driver.useRobotAPI(simulatedEntity.getRobotAPI());

    robot.useNavigator(navigator).useGatewayClient(gatewayClient).useRobotAPI(simulatedEntity.getRobotAPI()).useDriver(driver).getModel().getGridPart().displayGridOn(new SwingGridView());

    this.simulator.addSimulatedEntity(simulatedEntity);

    return this;
  }

  public SimulationRunner putPacmanAt(int x, int y, int direction) {
    PacmanEntity pacman = new PacmanEntity(x * Sector.SIZE + Sector.SIZE / 2, y * Sector.SIZE + Sector.SIZE / 2, direction);
    this.simulator.setPacmanEntity(pacman);
    return this;
  }

  public void start() {
    for (Robot robot : robots.values()) {
      SwingGridView gridview = (SwingGridView) robot.getModel().getGridPart().getGrid().getView();
      gridview.disableWindow();

      ((SwingSimulationView)simulator.getView()).addGrid(gridview);

    }
    this.simulator.run();
  }

  public static void showHelpFor(Options options) {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("SimulationRunner", options);
  }

  public static void main(String[] args) {

    SimulationRunner runner = new SimulationRunner();
    Boolean setupComplete = false;

    // define command line arguments
    Options options = new Options();
    options.addOption("h", "help", false, "show this helpful information.");
    options.addOption("q", "quiet", false, "don't show a user interface.");
    options.addOption("r", "robot", true,
            "use robot <classname>. default=" + DEFAULT_ROBOT);
    options.addOption("n", "navigator", true,
            "use navigator <classname>. default=" + DEFAULT_NAVIGATOR);
    options.addOption("d", "driver", true,
            "use driver <classname>. default=" + DEFAULT_DRIVER);
    options.addOption("g", "gatewayClient", true,
            "use gatewayClient <classname>. default=" + DEFAULT_GATEWAY_CLIENT);
    options.addOption("m", "map", true,
            "use mapfile. default=" + DEFAULT_MAP);

    CommandLineParser parser = new GnuParser();
    try {
      CommandLine line = parser.parse(options, args);

      if (line.hasOption("help")) {
        SimulationRunner.showHelpFor(options);
      } else {
        if (!line.hasOption("quiet")) {
          runner.useUI();
        }

        runner.useRobot(line.getOptionValue("robot", DEFAULT_ROBOT));
        runner.useNavigator(line.getOptionValue("navigator", DEFAULT_NAVIGATOR));
        runner.useDriver(line.getOptionValue("driver", DEFAULT_DRIVER));
        runner.useGatewayClient(line.getOptionValue("gatewayClient", DEFAULT_GATEWAY_CLIENT));
        runner.loadMap(line.getOptionValue("mapFile", DEFAULT_MAP));

        setupComplete = true;
      }
    } catch (ParseException exp) {
      System.err.println("ERROR:" + exp.getMessage());
      SimulationRunner.showHelpFor(options);
    }

    if (setupComplete) {
      runner.start();
    }
  }
}
