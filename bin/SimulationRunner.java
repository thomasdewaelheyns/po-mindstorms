
/**
 * SimulationRunner
 * 
 * Constructs a robot and map, sets up the Simulator and starts the simulation
 * 
 * @author: Team Platinum
 */
import java.io.File;
import java.io.FileNotFoundException;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.*;

import penoplatinum.Config;

import penoplatinum.simulator.entities.PacmanEntity;
import penoplatinum.simulator.entities.SimulatedEntity;
import penoplatinum.simulator.entities.SimulatedEntityFactory;

import penoplatinum.simulator.tiles.Sector;
import penoplatinum.simulator.view.SwingSimulationView;

import penoplatinum.map.mazeprotocol.ProtocolMapFactory;

import penoplatinum.model.part.GridModelPart;

import penoplatinum.grid.view.SwingGridView;

import penoplatinum.driver.*;
import penoplatinum.driver.behaviour.*;

import penoplatinum.gateway.GatewayClient;

import penoplatinum.navigator.Navigator;

import penoplatinum.reporter.Reporter;

import penoplatinum.robot.Robot;
import penoplatinum.robot.AdvancedRobot;
import penoplatinum.robot.RobotAPI;

import penoplatinum.simulator.RobotEntity;
import penoplatinum.simulator.Simulator;

import penoplatinum.util.Bearing;
import penoplatinum.util.Point;

public class SimulationRunner {

  // TODO move this to a properties-file
  private final static String DEFAULT_ROBOT = "penoplatinum.robot.GhostRobot";
  private final static String DEFAULT_NAVIGATOR = "penoplatinum.navigator.GhostNavigator";
  private final static String DEFAULT_DRIVER = "penoplatinum.driver.ManhattanDriver";
  private final static String DEFAULT_GATEWAY_CLIENT = "penoplatinum.simulator.SimulatedGatewayClient";
  private final static String DEFAULT_REPORTER = "penoplatinum.reporter.DashboardReporter";
  private final static String DEFAULT_MAP = "wolfraam.txt";
  private final static String DEFAULT_START = "1";
  private Simulator simulator;
  private String robotClassName;
  private String navigatorClassName;
  private String driverClassName;
  private String gatewayClientClassName;
  private String reporterClassName;
  private Map<String, Robot> robots = new HashMap<String, Robot>();
  private Map<String, Navigator> navigators = new HashMap<String, Navigator>();
  private Map<String, Driver> drivers = new HashMap<String, Driver>();
  private Map<String, GatewayClient> gatewayClients = new HashMap<String, GatewayClient>();
  private Map<String, Reporter> reporters = new HashMap<String, Reporter>();
  private Map<String, RobotAPI> robotAPIs = new HashMap<String, RobotAPI>();
  private Map<String, RobotEntity> simulatedEntities = new HashMap<String, RobotEntity>();
  private int start = 0;

  public SimulationRunner() {
    Config.load("robot.properties");
    this.simulator = new Simulator();
  }

  public SimulationRunner useUI() {
    this.simulator.displayOn(new SwingSimulationView());
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
      System.err.println(ex + " Robot could not create robot class.");
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
    // WARNING: HARD-CODED ...
    return new ManhattanDriver(0.4).addBehaviour(new FrontProximityDriverBehaviour()).addBehaviour(new SideProximityDriverBehaviour()).addBehaviour(new BarcodeDriverBehaviour()).addBehaviour(new LineDriverBehaviour());
  }

  // GATEWAYCLIENT
  public SimulationRunner useGatewayClient(String gatewayClientClassName) {
    this.gatewayClientClassName = gatewayClientClassName;
    return this;
  }

  public GatewayClient getGatewayClient(String name) {
    if (!this.gatewayClients.containsKey(name)) {
      this.gatewayClients.put(name, this.createGatewayClientInstance(name));
    }
    return this.gatewayClients.get(name);
  }

  public GatewayClient createGatewayClientInstance(String name) {
    try {
      Class theClass = Class.forName(this.gatewayClientClassName);
      return (GatewayClient) theClass.newInstance();
    } catch (ClassNotFoundException ex) {
      System.err.println(ex + " GatewayClient class must be in class-path.");
    } catch (InstantiationException ex) {
      System.err.println(ex + " GatewayClient class must be concrete.");
    } catch (IllegalAccessException ex) {
      System.err.println(ex + " GatewayClient class must have a no-arg constr.");
    }
    return null;
  }

  // REPORTER
  public SimulationRunner useReporter(String reporterClassName) {
    this.reporterClassName = reporterClassName;
    return this;
  }

  public Reporter getReporter(String name) {
    if (!this.reporters.containsKey(name)) {
      this.reporters.put(name, this.createReporterInstance(name));
    }
    return this.reporters.get(name);
  }

  public Reporter createReporterInstance(String name) {
    try {
      Class theClass = Class.forName(this.reporterClassName);
      return (Reporter) theClass.newInstance();
    } catch (ClassNotFoundException ex) {
      System.err.println(ex + " Reporter class must be in class-path.");
    } catch (InstantiationException ex) {
      System.err.println(ex + " Reporter class must be concrete.");
    } catch (IllegalAccessException ex) {
      System.err.println(ex + " Reporter class must have a no-arg constr.");
    }
    return null;
  }

  public SimulationRunner loadMap(String fileName) {
    try {
      File file = new File(fileName);
      penoplatinum.map.Map map = new ProtocolMapFactory().getMap(new Scanner(file));

      this.simulator.useMap(map);

      // add the ghosts
      int robotNr = -1;
      for (Point position : map.getGhostPositions()) {
        robotNr++;
        if (robotNr != start)
          continue;
        this.putGhostAt("" + robotNr, position.getX() - 1, position.getY() - 1, Bearing.N);
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

  public SimulationRunner putGhostAt(String name, int x, int y, Bearing direction) {
    AdvancedRobot robot = (AdvancedRobot) this.getRobot(name);
    Navigator navigator = this.getNavigator(name);
    Driver driver = this.getDriver(name);
    GatewayClient gatewayClient = this.getGatewayClient(name);
    Reporter reporter = this.getReporter(name).useGatewayClient(gatewayClient);

    // construct a simulatedEntity
    SimulatedEntity simulatedEntity = SimulatedEntityFactory.make(robot);
    int directionInt = 1;
    switch (direction) {
      case N:
        directionInt = 1;
      case E:
        directionInt = 2;
      case S:
        directionInt = 3;
      case W:
        directionInt = 4;
    }
    simulatedEntity.putRobotAt(x * Sector.SIZE + Sector.SIZE / 2, y * Sector.SIZE + Sector.SIZE / 2, directionInt);
    this.simulatedEntities.put(name, simulatedEntity);

    robot.useDriver(driver).useNavigator(navigator).useGatewayClient(gatewayClient).useReporter(reporter);

    System.out.println("DRIVER = " + driver);

    SwingGridView gridView = new SwingGridView();
    gridView.displayWithoutWindow(GridModelPart.from(robot.getModel()).getFullGrid());
    ((SwingSimulationView) simulator.getView()).addGrid(gridView);

    this.simulator.addSimulatedEntity(simulatedEntity);

    return this;
  }

  public SimulationRunner putPacmanAt(int x, int y, Bearing direction) {
    int directionInt = 1;
    switch (direction) {
      case N:
        directionInt = 1;
      case E:
        directionInt = 2;
      case S:
        directionInt = 3;
      case W:
        directionInt = 4;
    }

    PacmanEntity pacman;
    try {
      pacman = new PacmanEntity(x, y);
      this.simulator.setPacmanEntity(pacman);

    } catch (IOException ex) {
    } catch (InterruptedException ex) {
    }
    return this;
  }

  public void start() {
    // for (Robot robot : robots.values()) {
    //   SwingGridView gridview = (SwingGridView) robot.getModel().getGridPart().getGrid().getView();
    //   gridview.disableWindow();
    // 
    //   ((SwingSimulationView) simulator.getView()).addGrid(gridview);
    // 
    // }
    this.simulator.run();
  }

  public static void showHelpFor(Options options) {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp("SimulationRunner", options);
  }

  public SimulationRunner setStart(int start) {
    this.start = start;
    return this;
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
    options.addOption("s", "start", true,
            "use start number <number>. default=" + DEFAULT_START);
    options.addOption("n", "navigator", true,
            "use navigator <classname>. default=" + DEFAULT_NAVIGATOR);
    options.addOption("d", "driver", true,
            "use driver <classname>. default=" + DEFAULT_DRIVER);
    options.addOption("g", "gatewayClient", true,
            "use gatewayClient <classname>. default=" + DEFAULT_GATEWAY_CLIENT);
    options.addOption("p", "reporter", true,
            "use reporter <classname>. default=" + DEFAULT_REPORTER);
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

        runner.setStart(Integer.parseInt(line.getOptionValue("start", DEFAULT_START)));

        runner.useRobot(line.getOptionValue("robot", DEFAULT_ROBOT));
        runner.useNavigator(line.getOptionValue("navigator", DEFAULT_NAVIGATOR));
        runner.useDriver(line.getOptionValue("driver", DEFAULT_DRIVER));
        runner.useGatewayClient(line.getOptionValue("gatewayClient", DEFAULT_GATEWAY_CLIENT));
        runner.useReporter(line.getOptionValue("reporter", DEFAULT_REPORTER));
        runner.loadMap(line.getOptionValue("map", DEFAULT_MAP));

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
