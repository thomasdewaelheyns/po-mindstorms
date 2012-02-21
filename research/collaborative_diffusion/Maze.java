import java.io.File;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileInputStream;

import java.util.List;
import java.util.ArrayList;

public class Maze {
  private int         width   = 0,
                      height  = 0;
  private Sector[][]  maze;
  private MazeView    view    = new ConsoleMazeView().show(this);
  private boolean     showing = false;
  private List<Agent> agents  = new ArrayList<Agent>();  

  private Maze() {}

  // sets the size of the Maze and initializes the internal 2-dim array of
  // Sectors
  public Maze setSize(int width, int height) {
    this.width  = width;
    this.height = height;
    this.maze = new Sector[this.width][];
    // create new 2-dim array to store maze info
    for(int left=0; left<this.width; left++) {
      this.maze[left] = new Sector[this.height];
      for(int top=0; top<this.height; top++ ) {
        this.maze[left][top] = new Sector();
      }
    }
    return this;
  } 

  // updates wall information for a given position, if we're showing the Maze
  // already, request an update to visually reflect the updated information
  private Maze setWalls(int left, int top, char walls) {
    this.maze[left][top].setWalls(walls);
    if( this.showing ) {
      this.view.refreshWalls();
    }
    return this;
  }
  
  public Maze setWall(int left, int top, int orientation) {
    this.maze[left][top].setWall(orientation);
    if( this.showing ) {
      this.view.refreshWalls();
    }
    return this;
  }

  public Maze unsetWall(int left, int top, int orientation) {
    this.maze[left][top].unsetWall(orientation);
    if( this.showing ) {
      this.view.refreshWalls();
    }
    return this;
  }
  
  // returns all wall information for a given position
  public char getWalls(int left, int top) {
    return this.maze[left][top].getWalls();
  }
  
  // determines if a wall is present at a given position and wall-location
  // returns null if nothing is known about the wall
  public Boolean hasWall(int left, int top, int wall) {
    if( left < 0 || left >= this.width || top < 0 || top >= this.height ) {
      return null;
    }
    return this.maze[left][top].hasWall(wall);
  }
  
  // determins if given a position and baring if a wall or agent is present
  // at the faced position
  public boolean hasObstacle(int left, int top, int wall) {
    int l=0, t=0;
    switch(wall) {
      case Baring.N: t=-1; break;
      case Baring.E: l=+1; break;
      case Baring.S: t=+1; break;
      case Baring.W: l=-1; break;
    }
    return this.hasWall(left, top, wall) ||
           this.hasAgentAt(left+l, top+t);
  }

  // determine if given a position and baring if an agent is present at the
  // faced position
  public boolean facesAgentAt(int left, int top, int wall) {
    int l=0, t=0;
    switch(wall) {
      case Baring.N: t=-1; break;
      case Baring.E: l=+1; break;
      case Baring.S: t=+1; break;
      case Baring.W: l=-1; break;
    }
    return this.hasAgentAt(left+l, top+t);
  }

  // sets the value of the position in the Maze
  // TODO: clean this with respect to the other accessor
  private Maze setValue(int left, int top, int value) {
    this.maze[left][top].setValue(value);
    return this;
  }
  
  // returns the value of the position in the Maze
  // TODO: clean this with respect to other accessors
  public int getValue(int left, int top) {
    return this.maze[left][top].getValue();
  }

  // sets the view to display the maze on
  public Maze displayOn(MazeView view) {
    this.view = view;
    this.view.show(this);
    this.showing = true;
    return this;
  }
  
  // shows the maze, triggering a refresh/re-rendering of the view
  public Maze show() {
    this.view.refresh();
    return this;
  }
  
  // dumps the Maze using its values.
  public Maze dump() {
    for(int top=0; top<this.height; top++) {
      for(int left=0; left<this.width; left++ ) {
        System.out.printf( "%2d/%5d ", (int)this.maze[left][top].getWalls(),
                                      this.maze[left][top].getValue() );
      }
      System.out.println("");
    }
    return this;
  }
  
  // returns the value of the position. if an agent is present, return its
  // value.
  public int get(int left, int top) {
    Agent agent = this.getAgentAt(left, top);
    if( agent != null ) {
      return agent.getValue();
    }
    return this.getRaw(left,top);
  }

  // return the raw value of the position, this returns only the actual
  // value of the position, disregarding the availability of an agent
  public int getRaw(int left, int top) {
    if( left < 0 || left >= this.width || top < 0 || top >= this.height ) {
      return 0;
    }
    return this.maze[left][top].getValue();
  }
  
  // set the value of a position in the Maze
  // don't overwrite the value if there is a target on the position
  public Maze set(int left, int top, int value ) {
    if( left < 0 || left >= this.width || top < 0 || top >= this.height ) {
      return this;
    }
    if( this.maze[left][top].getValue() != 1000 ) {
      this.maze[left][top].setValue(value);
    }
    return this;
  }
  
  // return the width of the Maze
  public int getWidth() {
    return this.width;
  }

  // return the height of the Maze
  public int getHeight() {
    return this.height;
  }
  
  // add an agent to the maze
  public Maze addAgent(Agent agent) {
    this.agents.add(agent);
    // targets get knowledge about the entire maze and other agents
    if(agent.isTarget()) { agent.setMaze(this); }
    if( this.showing ) {
      this.view.refresh();
    }
    return this;
  }
  
  // return a list of all agents
  public List<Agent> getAgents() {
    return this.agents;
  }
  
  // get the agent at given position, might be null if nu agent is present
  public Agent getAgentAt(int left, int top) {
    for( Agent agent : this.agents ) {
      if( agent.getLeft() == left && agent.getTop() == top ) {
        return agent;
      }
    }
    return null;
  }
  
  // determine if an agent is at the given position
  public boolean hasAgentAt(int left, int top) {
    return this.getAgentAt(left, top) != null;
  }

  // determine if a hunting agent is at the given position
  public boolean hasHuntingAgentOn(int left, int top) {
    Agent agent = this.getAgentAt(left, top);
    if( agent != null && agent.isHunter() ) {
      return true;
    }
    return false;
  }

  // determine if all hunting agents are holding their position
  // this should indicate that the target is blocked
  public boolean allHuntingAgentsAreHolding() {
    for( Agent agent : this.agents ) {
      if( agent.isHunter() && ! agent.isHolding() ) { return false; }
    }
    return true;
  }
  
  // determine if (at least one) target is currently blocked
  public boolean targetIsBlocked() {
    for( Agent agent : this.agents ) {
      if( agent.isTarget() && agent.isHolding() ) { return true; }
    }
    return false;
  }

  // remove targets from the agent list
  public Maze clearTargets() {
    for( int i=this.agents.size()-1; i>=0; i-- ) {
      if( this.agents.get(i).isTarget() ) {
        this.agents.remove(i);
      }
    }
    return this;
  }

  // remove all agents from the agent list
  public Maze clearAgents() {
    this.agents.clear();
    return this;
  }

  // ask each agent to move, based on the value and agent information of the
  // four adjectant sectors
  public Maze moveAgents() {
    for( Agent agent : this.agents ) {
      int left = agent.getLeft();
      int top  = agent.getTop();

      Boolean hasWall;
      hasWall = this.hasWall(left, top, Baring.N);
      int n = hasWall != null && hasWall ? -1 : this.get(left, top-1);
      if( this.facesAgentAt(left, top, Baring.N) ) { n -= 2000;}

      hasWall = this.hasWall(left, top, Baring.E);
      int e = hasWall != null && hasWall ? -1 : this.get(left+1, top);
      if( this.facesAgentAt(left, top, Baring.E) ) { e -= 2000;}

      hasWall = this.hasWall(left, top, Baring.S);
      int s = hasWall != null && hasWall ? -1 : this.get(left, top+1);
      if( this.facesAgentAt(left, top, Baring.S) ) { s -= 2000;}

      hasWall = this.hasWall(left, top, Baring.W);
      int w = hasWall != null && hasWall ? -1 : this.get(left-1, top);
      if( this.facesAgentAt(left, top, Baring.W) ) { w -= 2000;}
  
      agent.move( n, e, s, w );
    }
    return this;
  }

  // factory method for empty Maze
  public static Maze create(int width, int height ) {
    Maze maze = new Maze();
    maze.setSize(width, height);
    return maze;
  }

  // factory method for Maze based on file
  public static Maze load(String fileName) {
    Maze maze = new Maze();
    try {
      File file = new File(fileName);
      Scanner scanner = new Scanner(file);
      maze.loadWalls(scanner);
      maze.loadAgents(scanner);
    } catch( Exception e ) { throw new RuntimeException(e); }
    return maze;
  }

  // given a scanner-based file, load the walls
  private void loadWalls(Scanner scanner) {
    // load walls
    int width  = scanner.nextInt();
    int height = scanner.nextInt();
    System.out.println( "loading maze: " + width + "x" + height );
    this.setSize(width, height);

    for(int top=0; top<height; top++) {
      for(int left=0; left<width; left++ ) {
        int v = scanner.nextInt();
        if( v < 1000 ) { this.setWalls(left, top, (char)v); }
        else {
          this.setValue(left, top, 1000);
          this.setWalls(left, top, (char)(v-1000));
        }
      }
    }
  }

  // given a scanner-based file, load the agents
  // at least one target agent should be available, else add one randomly
  // placed in the Maze
  private void loadAgents(Scanner scanner) {
    boolean haveTarget = false;

    while( scanner.hasNext() ) {
      String type, name = "";
      int left, top, orientation;
      
      type        = scanner.next();
      left        = scanner.nextInt();
      top         = scanner.nextInt();
      orientation = scanner.nextInt();
      if( type.equals("ghost") ) {
        name = scanner.next();
        this.addAgent(new GhostAgent(left, top, orientation, name));
      } else {
        this.addAgent(new PacmanAgent(left, top, orientation));
        haveTarget = true;  
      }
    }
    // add at least 1 randomly placed target
    if( ! haveTarget ) {
      int left = (int)(Math.random()*this.getWidth());
      int top  = (int)(Math.random()*this.getHeight());
      this.addAgent(new PacmanAgent(left, top, Baring.N));
    }
  }
}
