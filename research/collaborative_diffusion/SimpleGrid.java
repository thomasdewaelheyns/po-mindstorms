import java.io.File;
import java.util.Scanner;
import java.io.IOException;

import java.util.List;
import java.util.Collection;
import java.util.ArrayList;

// we're using commons collections HashedMap because HashMap isn't implemented
// on Lejos.
import org.apache.commons.collections.map.HashedMap;

public class SimpleGrid implements Grid {
  // we keep track of the boundaries of our Grid
  int minLeft = 0, maxLeft = 0, minTop = 0, maxTop = 0;
  // mapping from coordinates to allocating Sector
  private HashedMap sectors = new HashedMap();
  private HashedMap tags    = new HashedMap();
  // all agents in a row
  private List<Agent> agents = new ArrayList<Agent>();  
  // visualization for the Grid, by default none, is used by Simulator
  private GridView view = new NullGridView();

  private GridProcessor processor;
  
  public Grid setProcessor(GridProcessor processor) {
    this.processor = processor;
    this.processor.useGrid(this);
    return this;
  }

  // adds a sector to the grid.
  // a sector already contains its own coordinates, based on the coordinates
  // of the sector is was attached to. we store the sector using a <left,top>
  // string-key
  public Grid addSector(Sector sector) {
    sector.putOn(this);
    
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

    this.view.sectorsNeedRefresh();

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

  public List<Sector> getSectors() {
    @SuppressWarnings("unchecked")
    List<Sector> sectors = new ArrayList(this.sectors.values());
    return sectors;
  }

  private void connect(Sector sector, Sector other, int location) {
    if( sector != null ) {
      sector.addNeighbour(other, location);
    }
    if( other != null ) {
      other.addNeighbour(sector, Bearing.reverse(location));
    }
  }

  // sets the view to display the Grid on
  public Grid displayOn(GridView view) {
    this.view = view;
    this.view.display(this);
    return this;
  }

  // refreshes the Grid
  // this triggers the GridProcessors and refreshes any attached view
  public Grid refresh() {
    if( this.processor != null ) { this.processor.process(); }
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
          System.out.printf( "%5d ", (int)sector.getValue() );
        } else {
          System.out.print( "../....." );
        }
      }
      System.out.println("");
    }
    return this;
  }
  
  public Grid addAgent(Agent agent) {
    if( ! this.agents.contains(agent) ) {
      this.agents.add(agent);
      this.view.agentsNeedRefresh();
    }
    return this;
  }
  
  // return a list of all agents
  public List<Agent> getAgents() {
    return this.agents;
  }
  
  public Agent getAgent(String name) {
    for(Agent agent: this.agents) {
      if(agent.getName().equals(name)) { return agent; }
    }
    return null;
  }

  // remove all agents from the agent list
  public Grid clearAgents() {
    for(Agent agent : this.agents) {
      agent.getSector().removeAgent();
    }
    this.agents.clear();
    this.view.agentsNeedRefresh();
    return this;
  }

  public Grid load(String fileName) {
    try {
      File file = new File(fileName);
      Scanner scanner = new Scanner(file);
      this.loadWalls(scanner);
      this.loadAgentsAndTags(scanner);
    } catch( Exception e ) { throw new RuntimeException(e); }
    return this;
  }
  
  public Grid sectorsNeedRefresh() {
    this.view.sectorsNeedRefresh();
    return this;
  }

  public Grid wallsNeedRefresh() {
    // TODO: also separate walls update
    this.view.sectorsNeedRefresh();
    return this;
  }

  public Grid valuesNeedRefresh() {
    this.view.valuesNeedRefresh();
    return this;
  }

  public Grid agentsNeedRefresh() {
    this.view.agentsNeedRefresh();
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

  // given a scanner-based file, load the agents/tags
  // at least one target agent should be available, else add one randomly
  // placed in the Grid
  private void loadAgentsAndTags(Scanner scanner) {
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
        if( type.equals("tag") ) {
          String tag = scanner.next();
          sector.addTag(tag);
          this.tags.put(tag, sector);
        } else {
          if( type.equals("ghost") ) {
            name = scanner.next();
            agent = new GhostAgent(name);
          } else {
            agent = new PacmanAgent();
            haveTarget = true;  
          }
          sector.put(agent, orientation);
        }
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
      sector.put(new PacmanAgent(), Bearing.N);
    }
  }
}
