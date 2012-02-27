import java.io.File;
import java.util.Scanner;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;

// we're using commons collections HashedMap because HashMap isn't implemented
// on Lejos.
import org.apache.commons.collections.map.HashedMap;

public class Grid {
  // we keep track of the boundaries of our Grid
  int minLeft = 0, maxLeft = 0, minTop = 0, maxTop = 0;
  // mapping from coordinates to allocating Sector
  private HashedMap sectors = new HashedMap();
  // all agents in a row
  private List<Agent> agents = new ArrayList<Agent>();  
  // visualization for the Grid, by default none, is used by Simulator
  private GridView view = new NullGridView();

  // adds a sector to the grid.
  // a sector already contains its own coordinates, based on the coordinates
  // of the sector is was attached to. we store the sector using a <left,top>
  // string-key
  public Grid addSector(Sector sector) {
    sector.setGrid(this);
    
    int left = sector.getLeft(), 
        top  = sector.getTop();
    this.resize(left, top);
    
    // add the sector to the list of all sectors in this grid
    this.sectors.put(left + "," + top, sector);
    // connect neighbours
    this.connect(sector, this.getSector(left, top-1), Bearing.N);
    this.connect(sector, this.getSector(left+1, top), Bearing.E);
    this.connect(sector, this.getSector(left, top+1), Bearing.S);
    this.connect(sector, this.getSector(left-1, top), Bearing.W);

    this.view.refreshSize();

    return this;
  }
  
  private void resize(int left, int top) {
    if( left < this.minLeft ) { this.minLeft = left; }
    if( left > this.maxLeft ) { this.maxLeft = left; }
    if( top  < this.minTop  ) { this.minTop  = top;  }
    if( top  > this.maxTop  ) { this.maxTop  = top;  }
  }

  public int getMinLeft() { return this.minLeft; }
  public int getMaxLeft() { return this.maxLeft; }
  public int getMinTop()  { return this.minTop;  }
  public int getMaxTop()  { return this.maxTop;  }

  public int getWidth()  { return this.maxLeft - this.minLeft + 1; }
  public int getHeight() { return this.maxTop - this.minTop + 1;   }

  // returns the sector at given absolute/relative coordinates or null
  public Sector getSector(int left, int top) {
    return (Sector)this.sectors.get(left + "," + top);
  }

  private void connect(Sector sector, Sector other, int location) {
    if( sector != null ) {
      //System.out.println("Connecting " + sector.getLeft() + "," + sector.getTop() + " <- " + " / " + location );
      sector.addNeighbour(other, location);
    }
    if( other != null ) {
      //System.out.println("Connecting " + " -> " + other.getLeft() + "," + other.getTop() + " / " + Bearing.reverse(location) );
      other.addNeighbour(sector, Bearing.reverse(location));
    }
  }

  // sets the view to display the Grid on
  public Grid displayOn(GridView view) {
    this.view = view;
    this.view.show(this);
    return this;
  }

  // returns the View that displays this grid
  public GridView getView() {
    return this.view;
  }
  
  // shows the Grid, triggering a refresh/re-rendering of the view
  public Grid show() {
    this.view.refresh();
    return this;
  }
  
  // dumps the Grid using its values
  public Grid dump() {
    for(int top=this.getMinTop(); top<=this.getMaxTop(); top++) {
      for(int left=this.getMinLeft(); left<=this.getMaxLeft(); left++ ) {
        Sector sector = this.getSector(left, top);
        int walls, value;
        if( sector != null ) {
          walls = 
          value = sector.getValue();
          System.out.printf( "%2d/%5d ", (int)sector.getWalls(),
                                         (int)sector.getValue() );
        } else {
          System.out.print( "../....." );
        }
      }
      System.out.println("");
    }
    return this;
  }
  
  public Grid addAgent(Agent agent) {
    if( ! this.agents.contains(agent) ) { this.agents.add(agent); }
    return this;
  }
  
  // return a list of all agents
  public List<Agent> getAgents() {
    return this.agents;
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
  public Grid clearTargets() {
    for( int i=this.agents.size()-1; i>=0; i-- ) {
      if( this.agents.get(i).isTarget() ) {
        this.agents.remove(i);
      }
    }
    return this;
  }

  // remove all agents from the agent list
  public Grid clearAgents() {
    this.agents.clear();
    return this;
  }

  // ask each agent to move, based on the value and agent information of the
  // four adjectant sectors
  public Grid moveAgents() {
    for( int a=0; a<this.agents.size(); a++ ) {
      Agent  agent  = this.agents.get(a);
      Sector sector = agent.getSector();

      Boolean hasNeighbour, hasWall;
      int[] info = {-1, -1, -1, -1};
      for(int atLocation=Bearing.N; atLocation<=Bearing.W; atLocation++ ) {
        hasNeighbour = sector.hasNeighbour(atLocation);
        hasWall      = sector.hasWall(atLocation);
        hasWall      = hasWall != null && hasWall;
        info[atLocation] = !sector.hasNeighbour(atLocation) || hasWall ?
                           -1 : sector.getNeighbour(atLocation).getValue();
        if( sector.facesAgent(atLocation) ) { info[atLocation] -= 2000;}
      }
      agent.move( info[Bearing.N], info[Bearing.E],
                  info[Bearing.S], info[Bearing.W] );
    }
    return this;
  }

  public Grid load(String fileName) {
    try {
      File file = new File(fileName);
      Scanner scanner = new Scanner(file);
      this.loadWalls(scanner);
      this.loadAgents(scanner);
    } catch( Exception e ) { throw new RuntimeException(e); }
    return this;
  }

  // given a scanner-based file, load the walls
  private void loadWalls(Scanner scanner) {
    // load walls
    int width  = scanner.nextInt();
    int height = scanner.nextInt();
    System.out.println( "loading grid: " + width + "x" + height );

    Sector currentSector;
    for(int top=0; top<height; top++) {
      for(int left=0; left<width; left++ ) {
        int v = scanner.nextInt();
        Sector sector = new Sector(this)
                          .setCoordinates(left, top)
                          .addWalls((char)v);
        this.addSector(sector); // add it to the grid, this will connect it
                                // to its neighbours
      }
    }
  }

  // given a scanner-based file, load the agents
  // at least one target agent should be available, else add one randomly
  // placed in the Grid
  private void loadAgents(Scanner scanner) {
    boolean haveTarget = false;

    while( scanner.hasNext() ) {
      String type, name = "";
      int left, top, orientation;
      
      type        = scanner.next();
      left        = scanner.nextInt();
      top         = scanner.nextInt();
      orientation = scanner.nextInt();
      Sector sector = this.getSector(left, top);
      if( sector != null ) {
        Agent agent;
        if( type.equals("ghost") ) {
          name = scanner.next();
          agent = new GhostAgent(name);
        } else {
          agent = new PacmanAgent();
          haveTarget = true;  
        }
        sector.putAgent(agent, orientation);
      }
    }
    
    // add at least 1 randomly placed target
    if( ! haveTarget ) {
      Sector sector = null;
      while(null == sector) {
        int left = (int)(Math.random()*this.getWidth());
        int top  = (int)(Math.random()*this.getHeight());
        sector = this.getSector(left, top);
      }
      sector.putAgent(new PacmanAgent(), Bearing.N);
    }
  }
}
