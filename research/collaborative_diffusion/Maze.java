import java.io.File;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileInputStream;

import java.util.List;
import java.util.ArrayList;

public class Maze {
  private int width = 0, height = 0;
  private Sector[][] maze;
  
  private MazeView view = new ConsoleMazeView().show(this);

  private Maze(int width, int height) {
    this.width  = width;
    this.height = height;
    this.resize();
  }

  private Maze resize() {
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

  private Maze setWalls(int left, int top, char walls) {
    this.maze[left][top].setWalls(walls);
    return this;
  }
  
  public char getWalls(int left, int top) {
    return this.maze[left][top].getWalls();
  }
  
  public boolean hasWall(int left, int top, int wall) {
    return this.maze[left][top].hasWall(wall);
  }
  
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

  private Maze setValue(int left, int top, int value) {
    this.maze[left][top].setValue(value);
    return this;
  }
  
  public int getValue(int left, int top) {
    return this.maze[left][top].getValue();
  }

  public Maze displayOn(MazeView view) {
    this.view = view;
    this.view.show(this);
    return this;
  }
  
  public Maze show() {
    this.view.refresh();
    return this;
  }
  
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
  
  public int get(int left, int top) {
    Agent agent = this.getAgentAt(left, top);
    if( agent != null ) {
      return agent.getValue();
    }
    return this.getRaw(left,top);
  }

  public int getRaw(int left, int top) {
    if( left < 0 || left >= this.width || top < 0 || top >= this.height ) {
      return 0;
    }
    return this.maze[left][top].getValue();
  }
  
  public Maze set(int left, int top, int value ) {
    if( left < 0 || left >= this.width || top < 0 || top >= this.height ) {
      return this;
    }
    if( this.maze[left][top].getValue() != 1000 ) {
      this.maze[left][top].setValue(value);
    }
    return this;
  }
  
  public int getWidth() {
    return this.width;
  }

  public int getHeight() {
    return this.height;
  }
  
  private List<Agent> agents = new ArrayList<Agent>();
  
  public Maze addAgent(Agent agent) {
    this.agents.add(agent);
    // targets get knowledge about the entire maze and other agents
    if(agent.isTarget()) { agent.setMaze(this); }
    return this;
  }
  
  public List<Agent> getAgents() {
    return this.agents;
  }
  
  public Agent getAgentAt(int left, int top) {
    for( Agent agent : this.agents ) {
      if( agent.getLeft() == left && agent.getTop() == top ) {
        return agent;
      }
    }
    return null;
  }
  
  public boolean hasAgentAt(int left, int top) {
    return this.getAgentAt(left, top) != null;
  }

  public boolean hasHuntingAgentOn(int left, int top) {
    Agent agent = this.getAgentAt(left, top);
    if( agent != null && agent.isHunter() ) {
      return true;
    }
    return false;
  }

  public boolean allHuntingAgentsAreHolding() {
    for( Agent agent : this.agents ) {
      if( agent.isHunter() && ! agent.isHolding() ) { return false; }
    }
    return true;
  }
  
  public boolean targetIsBlocked() {
    for( Agent agent : this.agents ) {
      if( agent.isTarget() && agent.isHolding() ) { return true; }
    }
    return false;
  }

  public Maze moveAgents() {
    for( Agent agent : this.agents ) {
      int left = agent.getLeft();
      int top  = agent.getTop();
      
      int n = this.hasWall(left, top, Baring.N) ? -1 : this.get(left, top-1);
      if( this.facesAgentAt(left, top, Baring.N) ) { n -= 2000;}
      int e = this.hasWall(left, top, Baring.E) ? -1 : this.get(left+1, top);
      if( this.facesAgentAt(left, top, Baring.E) ) { e -= 2000;}
      int s = this.hasWall(left, top, Baring.S) ? -1 : this.get(left, top+1);
      if( this.facesAgentAt(left, top, Baring.S) ) { s -= 2000;}
      int w = this.hasWall(left, top, Baring.W) ? -1 : this.get(left-1, top);
      if( this.facesAgentAt(left, top, Baring.W) ) { w -= 2000;}
  
      agent.move( n, e, s, w );
    }
    return this;
  }

  public static Maze load(String fileName) {
    Maze maze = new Maze(0,0);
    try {
      File file = new File(fileName);
      Scanner scanner = new Scanner(file);
      int width  = scanner.nextInt();
      int height = scanner.nextInt();
      System.out.println( "loading maze: " + width + "x" + height );
      maze = new Maze(width,height);

      for(int top=0; top<height; top++) {
        for(int left=0; left<width; left++ ) {
          int v = scanner.nextInt();
          if( v < 1000 ) { maze.setWalls(left, top, (char)v); }
          else {
            maze.setValue(left, top, 1000);
            maze.setWalls(left, top, (char)(v-1000));
          }
        }
      }
    } catch( Exception e ) { throw new RuntimeException(e); }
    return maze;
  }
}
