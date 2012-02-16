public class Sector {
  private Tile tile;
  private int  value;
  
  public Sector() {
    this.tile = new Tile();
  }
  
  public int getValue() {
    return value;
  }
  
  public Sector setValue(int value) {
    this.value = value;
    return this;
  }
  
  public Sector setWalls(char walls) {
    this.tile.withWalls(walls);
    return this;
  }

  public char getWalls() {
    return this.tile.getWalls();
  }
  
  public Sector setWall(int wall) {
    this.tile.withWall(wall);
    return this;
  }
  
  public boolean hasWall(int wall) {
    return this.tile.hasWall(wall);
  }
}
