public class Sector {
  // walls and the certainty about them
  private Tile walls     = new Tile();
  private Tile certainty = new Tile();
  
  // the value associated with this sector. it is used to create collaborated
  // diffusion
  private int  value;
  
  public int getValue() {
    return value;
  }
  
  public Sector setValue(int value) {
    this.value = value;
    return this;
  }
  
  public Sector setWalls(char walls) {
    this.walls.withWalls(walls);
    this.certainty.withWalls((char)15); // mark all walls as certain
    return this;
  }
  
  public Sector clearCertainty() {
    this.certainty.clear();
    return this;
  }

  public boolean isKnown(int wall) {
    return this.certainty.hasWall(wall);
  }

  public char getWalls() {
    return this.walls.getWalls();
  }
  
  public Sector setWall(int wall) {
    this.walls.withWall(wall);
    this.certainty.withWall(wall);
    return this;
  }

  public Sector unsetWall(int wall) {
    this.walls.withoutWall(wall);
    this.certainty.withWall(wall);
    return this;
  }
  
  // we use the Boolean class here to be able to return "null" when we don't
  // know anything about the wall.
  public Boolean hasWall(int wall) {
    return this.certainty.hasWall(wall) ? this.walls.hasWall(wall) : null;
  }
}
