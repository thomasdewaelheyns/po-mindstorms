package penoplatinum.simulator;

import java.awt.Point;
import penoplatinum.agent.MQ;
import penoplatinum.bluetooth.CircularQueue;
import penoplatinum.simulator.tiles.Tile;
import penoplatinum.simulator.view.ViewRobot;

public class RemoteEntity implements RobotEntity{

  public final double LENGTH_ROBOT = 10.0;
  
  private double positionX;       // the position of the robot in the world
  private double positionY;       //   expressed in X,Y coordinates
  private double direction;       //   and a direction it's facing
  
  SimulationRobotAgent robotAgent;  // the communication layer
  
  private Simulator simulator;

  private MQ mq;
  
  private CircularQueue<String> messageQueue;
  
  
  public RemoteEntity(String entityName) {
    this.robotAgent = robotAgent;
    
    mq = new MQ(){

      @Override
      protected void handleIncomingMessage(String sender, String message) {
        synchronized(this)
        {
          messageQueue.insert(message);
        }
      }
      
    };
    
  }
  
  public void useSimulator(Simulator simulator){
    this.simulator = simulator;
  }

  public void setPostition(double positionX, double positionY, double direction) {
    this.positionX = positionX;
    this.positionY = positionY;
    this.direction = direction;
  }

  /**
   * A robot is put on the map - as in the real world - on a certain place
   * and in a given direction.
   * The Simulator also instruments the robot with a RobotAPI and sets up
   * the RobotAgent to interact with the robot.
   */
  public RemoteEntity putRobotAt(Robot robot, int x, int y, int direction) {
    this.positionX = x;
    this.positionY = y;
    this.direction = direction;


    return this;
  }

  public double getPosX() {
    return positionX;
  }
  
  public double getPosY() {
    return positionY;
  }
  
  public double getDir(){
    return direction;
  }
  
  public ViewRobot getViewRobot(){
    return new RemoteViewRobot(this);
  }
  

  /**
   * Our internal representation of the baring uses zero pointing north.
   * Math functions use zero pointing east.
   * We also only want an angle from 0 to 359.
   */
  public int getAngle() {
    return (int) ((this.direction + 90) % 360);
  }

  // performs the next step in the movement currently executed by the robot
  public void step() {
    // Process messages in the queue
    synchronized(this){
      while (!messageQueue.isEmpty())
      {
        String msg = messageQueue.remove();
        
        //TODO: Temp message protocol
        if (msg.charAt(0) == 'p')
        {
          // Position msg
          String[] coords = msg.substring(1).split(",");
          positionX = Integer.parseInt(coords[0]);
          positionY = Integer.parseInt(coords[1]);
        }
        
        
      }
    }
  }

  public Point getCurrentTileCoordinates() {
    // determine tile coordinates we're on
    int left = (int) (this.positionX / simulator.getTileSize())+ 1;
    int top = (int) (this.positionY / simulator.getTileSize()) + 1;
    return new Point(left, top);
  }

  public Point getCurrentOnTileCoordinates() {
    // determine tile coordinates on the tile we're on
    int left = (int) (this.positionX % simulator.getTileSize());
    int top = (int) (this.positionY % simulator.getTileSize());
    return new Point(left, top);
  }
}
