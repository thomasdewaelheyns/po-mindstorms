/**
 * Simulator
 * 
 * Accepts a robot, map and view and simulates the how the robot would run
 * this map (in a perfect world). Its main use is to test the theory behind
 * Navigator implementations, without the extra step onto the robot.
 * 
 * Future Improvements: Add support for multiple robots
 * 
 * Author: Team Platinum
 */
 
import java.awt.Point;
 
class Simulator {

  private SimulationView view;    // a view to display the simulation
  private Map map;                // the map that the robot will run on
  private SimulationRobotAPI robotAPI;      // the API used to access hardware
  private SimulationRobotAgent robotAgent;  // the communication layer

  private Robot robot;            // the actual robot

  private double positionX;       // the position of the robot in the world
  private double positionY;       //   expressed in X,Y coordinates
  private double direction;       //   and a direction its facing
  
  private int[] sensorValues = new int[7]; // the current state of the sensors

  private int steps;              // the number of steps still to do
  private double dx, dy, dr;      // the difference for x, y and rotation
  
  private int currentMovement;    // movement that is being stepped
  private int lastChangeM1 = 0;    // the last change on Motor 1
  private int lastChangeM2 = 0;    // the last change on Motor 2
  private int lastChangeM3 = 0;    // the last change on Motor 3

  // main constructor, no arguments, Simulator is selfcontained
  public Simulator() {
    this.setupSimulationEnvironment();
  }

  /**
   * The simulation environment provides an implementation of the RobotAPI
   * and RobotAgent which are wired to the Simulator. This allows any default
   * robot to be tested without modification.
   */
  private void setupSimulationEnvironment() {
    this.robotAPI = new SimulationRobotAPI();
    this.robotAPI.setSimulator(this);         // provide callback object);
    this.robotAgent = new SimulationRobotAgent();
    this.robotAgent.setSimulator(this);       // provide callback object);
  }

  /**
   * On a SimulationView, the Simulator can visually show what happens during
   * the simulation.
   */
  public Simulator displayOn(SimulationView view) {
    this.view = view;
    if( this.map != null ) {
      this.view.showMap(map);
    }
    return this;
  }

  /**
   * A map can be provided. This will determine what information the 
   * Simulator will send to the robot's sensors, using the SimulationRobotAPI.
   */
  public Simulator useMap(Map map) {
    this.map = map;
    if( this.view != null ) {
      this.view.showMap(map);
    }
    return this;
  }
  
  /**
   * A robot is put on the map - as in the real world - on a certain place
   * and in a given direction.
   * The Simulator also instruments the robot with a RobotAPI and sets up
   * the RobotAgent to interact with the robot.
   */
  public Simulator putRobotAt(Robot robot, int x, int y, int direction) {
    this.robot = robot;
    this.positionX = x;
    this.positionY = y;
    this.direction = direction;
    
    // we provide the robot with our SimulationAPI
    this.robot.useRobotAPI( this.robotAPI );

    // we connect our SimulationRobotAgent to the robot
    this.robotAgent.setRobot( this.robot );

    return this;
  }
  
  public Simulator moveRobot( double movement ) {
    this.currentMovement = Navigator.MOVE;
    
    // our direction 0 is pointing North
    double rads = Math.toRadians(this.direction + 90);

    // convert from distance in meters to cm
    int distance = (int)(movement * 100.0);
    int direction = 1;
    if( distance < 0 ) {
      direction = -1;
      distance *= -1;
    }

    this.dx    = Math.cos(rads) * direction;
    this.dy    = Math.sin(rads) * direction;
    this.steps = distance;
    
    return this;
  }
  
  public Simulator turnRobot( double angle ) {
    this.currentMovement = Navigator.TURN;
    
    // turn in steps of 3 degrees
    this.dr   = 3.0;
    this.steps = (int)(Math.abs(angle) / this.dr);
    // if the angle is negative, move clock-wise
    if( angle < 0 ) {
      this.dr = -3.0;
    }

    return this;
  }
  
  public Simulator stopRobot() {
    this.currentMovement = Navigator.STOP;
    return this;
  }

  private void refreshView() {
    this.view.updateRobot( (int)this.positionX, (int)this.positionY,
                           (int)this.direction );
  }
  
  /**
   * Performs the next step in the movement currently executed by the robot
   */ 
  private void step() {
    this.lastChangeM1 = 0;
    this.lastChangeM2 = 0;
    // process the next step in the movement that is currently being performed
    switch( this.currentMovement ) {
      case Navigator.MOVE:
        if( this.steps-- > 0 ) {
          this.positionX += this.dx;
          this.positionY -= this.dy;
          // TODO: fix this to be correct towards actual change
          this.lastChangeM1 = 1;
          this.lastChangeM2 = 1;
        }
        break;
      case Navigator.TURN:
        if( this.steps-- > 0 ) {
          this.direction = this.direction + this.dr;
          if( this.direction < 0 ) {
            this.direction += 360;
          }
          this.direction %= 360;
          // TODO: fix this to be correct towards dimensions of robot
          this.lastChangeM1 = 1;
          this.lastChangeM2 = -1;
        }
        break;
      case Navigator.STOP:
        this.currentMovement = Navigator.NONE;
        break;
      case Navigator.NONE:
      default:
        // do nothing
    }
    
    // based on the new location, determine the value of the different sensors
    this.updateSensorValues();

    // always refresh our SimulationView
    this.refreshView();
  }
  
  /**
   * based on the robot's position, determine the values for the different
   * sensors.
   * TODO: extract the robot's physical configuration into separate object
   *       this is shared with the Model in a way (for now)
   */
  private void updateSensorValues() {
    int lengthRobot = 20;
    
    int distance = this.getFreeFrontDistance();

    if( distance < lengthRobot / 2 ) {
      this.sensorValues[Model.S1] = 50;
    } else {
      this.sensorValues[Model.S1] = 0;
    }
        
    this.sensorValues[Model.M1] = this.lastChangeM1;
    this.sensorValues[Model.M2] = this.lastChangeM2;
  }
  
  /**
   * Determine the distance to the first obstacle in direct line of sight
   */
  private int getFreeFrontDistance() {
    int distance = 0;
    
    // determine tile coordinates we're on
    int left = (int)Math.floor(this.positionX / 80) + 1;
    int top  = (int)Math.floor(this.positionY / 80) + 1;

    // determine position within tile
    int x = (int)this.positionX % 80;
    int y = (int)this.positionY % 80;

    // find distance to first wall in line of sight
    distance = (int)Math.round(this.findHitDistance(left, top, x, y, 0));

    System.out.println( (int)(x) + ", " + (int)(y) + " @ " + ( ( this.direction + 90 ) % 360 ) + " | " + distance );
    
    return distance;
  }
  
  /**
   * TODO : REFACTOR THIS !!!! URGENTLY !!!!!
   */
  private double findHitDistance(int left, int top, int x, int y, double d) {
    Point hit   = this.findHitPoint(x,y);
    double dist = Math.sqrt( Math.pow(hit.x - x, 2 ) + 
                             Math.pow(hit.y - y, 2 ) );

    Tile onTile = this.map.get( left, top );

    System.out.println( x + ", " + y + " --> " + hit );

    if( hit.x == 0 && hit.y == 0 ) {
      int angle = (int)(( this.direction + 90 ) % 360);
      if( angle == 90 ) {
        // N
        System.out.println( "facing north" );      
        if( ! onTile.hasWall(Tile.N) ) {
          System.out.println( "tile " + left + " , " + top + " has no north wall, adding to dist = " + dist );
          dist = this.findHitDistance(left, top - 1, 0, 80, dist );
        }
      } else if( angle > 90 && angle < 180 ) {
        // NW
        System.out.println( "facing north-west" );      
        if( ! onTile.hasWall(Tile.N) && ! onTile.hasWall(Tile.W) ) {
          System.out.println( "tile " + left + " , " + top + " has no north or west wall, adding to dist = " + dist );
          dist = this.findHitDistance(left - 1, top - 1, 80, 80, dist );
        }
      } else {
        // W
        System.out.println( "facing west" );
        if( ! onTile.hasWall(Tile.W) ) {
          System.out.println( "tile " + left + " , " + top + " has no west wall, adding to dist = " + dist );
          dist = this.findHitDistance(left - 1, top, 80, 0, dist );
        }
      }
    } else if( hit.x == 0 && hit.y == 80 ) {
      int angle = (int)(( this.direction + 90 ) % 360);
      if( angle == 180 ) {
        // W
        System.out.println( "facing west" );
        if( ! onTile.hasWall(Tile.W) ) {
          System.out.println( "tile " + left + " , " + top + " has no west wall, adding to dist = " + dist );
          dist = this.findHitDistance(left - 1, top, 80, 80, dist );
        }
      } else if( angle > 180 && angle < 270 ) {
        // SW
        System.out.println( "facing south-west" );      
        if( ! onTile.hasWall(Tile.S) && ! onTile.hasWall(Tile.W) ) {
          System.out.println( "tile " + left + " , " + top + " has no south or west wall, adding to dist = " + dist );
          dist = this.findHitDistance(left - 1, top + 1, 0, 80, dist );
        }
      } else {
        // S
        System.out.println( "facing south" );      
        if( ! onTile.hasWall(Tile.S) ) {
          System.out.println( "tile " + left + " , " + top + " has no south wall, adding to dist = " + dist );
          dist = this.findHitDistance(left, top + 1, 0, 0, dist );
        }
      }
    } else if( hit.x == 80 && hit.y == 0 ) {
      int angle = (int)(( this.direction + 90 ) % 360);
      if( angle == 0 ) {
        // E
        System.out.println( "facing east" );
        if( ! onTile.hasWall(Tile.E) ) {
          System.out.println( "tile " + left + " , " + top + " has no east wall, adding to dist = " + dist );
          dist = this.findHitDistance(left + 1, top, 0, 0, dist );
        }
      } else if( angle > 0 && angle < 90 ) {
        // NE
        System.out.println( "facing north-east" );      
        if( ! onTile.hasWall(Tile.N) && ! onTile.hasWall(Tile.E) ) {
          System.out.println( "tile " + left + " , " + top + " has no north or east wall, adding to dist = " + dist );
          dist = this.findHitDistance(left + 1, top - 1, 0, 80, dist );
        }
      } else {
        // N
        System.out.println( "facing north" );      
        if( ! onTile.hasWall(Tile.N) ) {
          System.out.println( "tile " + left + " , " + top + " has no north wall, adding to dist = " + dist );
          dist = this.findHitDistance(left, top - 1, 80, 80, dist );
        }
      }
    } else if( hit.x == 80 && hit.y == 80 ) {
      int angle = (int)(( this.direction + 90 ) % 360);
      if( angle == 270 ) {
        // S
        System.out.println( "facing south" );
        if( ! onTile.hasWall(Tile.S) ) {
          System.out.println( "tile " + left + " , " + top + " has no south wall, adding to dist = " + dist );
          dist = this.findHitDistance(left, top + 1, 80, 0, dist );
        }
      } else if( angle > 270 && angle < 360 && angle != 0 ) {
        // SE
        System.out.println( "facing south-east" );      
        if( ! onTile.hasWall(Tile.S) && ! onTile.hasWall(Tile.E) ) {
          System.out.println( "tile " + left + " , " + top + " has no south or east wall, adding to dist = " + dist );
          dist = this.findHitDistance(left + 1, top + 1, 0, 0, dist );
        }
      } else {
        // N
        System.out.println( "facing east" );      
        if( ! onTile.hasWall(Tile.E) ) {
          System.out.println( "tile " + left + " , " + top + " has no east wall, adding to dist = " + dist );
          dist = this.findHitDistance(left + 1, top, 0, 80, dist );
        }
      }
    } else if( hit.x == 0 ) {
      System.out.println( "facing west" );
      if( ! onTile.hasWall(Tile.W) ) {
        System.out.println( "tile " + left + " , " + top + " has no west wall, adding to dist = " + dist );
        dist = this.findHitDistance(left - 1, top, 80, hit.y, dist );
      }
    } else if( hit.x == 80 ) {
      System.out.println( "facing east" );      
      if( ! onTile.hasWall(Tile.E) ) {
        System.out.println( "tile " + left + " , " + top + " has no east wall, adding to dist = " + dist );
        dist = this.findHitDistance(left + 1, top, 0, hit.y, dist );
      }
    } else if( hit.y == 0 ) {
      System.out.println( "facing north" );      
      if( ! onTile.hasWall(Tile.N) ) {
        System.out.println( "tile " + left + " , " + top + " has no north wall, adding to dist = " + dist );
        dist = this.findHitDistance(left, top - 1, hit.x, 80, dist );
      }
    } else if( hit.y == 80 ) {
      System.out.println( "facing south" );      
      if( ! onTile.hasWall(Tile.S) ) {
        System.out.println( "tile " + left + " , " + top + " has no south wall, adding to dist = " + dist );
        dist = this.findHitDistance(left, top + 1, hit.x, 0, dist );
      }
    } else {
      System.out.println( "ERROR: " + hit );
    }
    
    return d + dist;
  }

  private double T( double x, double d ) {
    /**
     * Geonometry used:
     *
     *            +
     *          / |
     *        /   |  Y
     *      / a   |
     *    +-------+
     *        X
     *
     *    tan(a) = Y/X    =>   Y = tan(a) * X     ||   X = Y / tan(a)
     */
    return x * Math.tan(Math.toRadians(d));
  }
  
  private Point findHitPoint( int X, int Y ) {
    double angle   = ( ( this.direction + 90 ) % 360 );
    double x, y;
    double dx, dy;

    if( angle <= 90 ) {
      dx = 80 - X;
      dy = this.T( dx, angle );
      if( dy > Y ) {
        dy = Y;
        dx = this.T( dy, 90 - angle );
      }
      x = X + dx;
      y = Y - dy;
    } else if( angle > 90 && angle <= 180 ) {
      dx = X;
      dy = this.T( dx, 180-angle );
      if( dy > Y ) {
        dy = Y;
        dx = this.T( dy, angle - 90 );
      }
      x = X - dx;
      y = Y - dy;
    } else if( angle > 180 && angle <= 270 ) {
      dx = X;
      dy = this.T( dx, angle - 180 );
      if( dy > ( 80 - Y ) ) {
        dy = ( 80 - Y );
        dx = this.T( dy, 270 - angle );
      }
      x = X - dx;
      y = Y + dy;
    } else { 
      // angle > 270 && angle < 360
      dx = 80 - X;
      dy = this.T( dx, 360 - angle );
      if( dy > ( 80 - Y ) ) {
        dy = ( 80 - Y );
        dx = this.T( dy, angle - 270 );
      }
      x = X + dx;
      y = Y + dy;
    }
    
    return new Point((int)x,(int)y);
  }
  
  /**
   * Allows the end-user to send commands throught the communication layer
   * to the Robot. In the real world this is done through the RobotAgent,
   * which here is being provided and controled by the Simulator.
   */
  public Simulator send( String cmd ) {
    this.robotAgent.receive( cmd );
    return this;
  }

  /**
   * This processes status-feedback from the RobotAgent, extracted from the
   * Model and Navigator.
   */ 
  public Simulator receive( String status ) {
    // this is normally used by the PC client to implement a View of the 
    // Robot's mind.
    return this;
  }
  
  /**
   * This starts the actual simulation, which will start the robot and the 
   * agent.
   */
  public Simulator run() {
    this.robotAgent.run();
    while( ! this.robot.reachedGoal() ) {
      this.robot.step();
      this.step();
    }
    return this;
  }
  
  public int[] getSensorValues() {
    return this.sensorValues;
  }

}
