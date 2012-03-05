package penoplatinum.grid;

/**
 * ConsoleGridView
 * 
 * GridView to display a Grid on the console.
 * 
 * @author: Team Platinum
 */

public class ConsoleGridView implements GridView {
  private Grid grid;
  
  public GridView refresh(Grid grid) {
    this.grid = grid;
    return this;
  }
  
  public GridView refresh() {
    if( this.grid == null ) { return this; }
    
    // clear screen (handy for basic "animation"
    for(int clear=0; clear<150; clear++ ) { System.out.println(""); }

    int width  = this.grid.getWidth();
    int height = this.grid.getHeight();

    // draw the Grid using it's internal values
    for(int top=0; top<height; top++) {
      for(int left=0; left<width; left++ ) {
        System.out.printf( "%5d ", this.grid.getValue(left,top) );
      }
      System.out.println("");
    }
    return this;
  }

  public GridView sectorsNeedRefresh() { return this; }
  public GridView valuesNeedRefresh()  { return this; }
  public GridView agentsNeedRefresh()  { return this; }
  
  public GridView changeTitle(String title)         { return this; }
  public GridView changeLocation(int left, int top) { return this; }
}
