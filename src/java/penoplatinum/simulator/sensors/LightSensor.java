package penoplatinum.simulator.sensors;

import java.awt.Point;
import penoplatinum.simulator.Sensor;
import penoplatinum.simulator.SimulatedEntity;
import penoplatinum.simulator.Simulator;
import penoplatinum.simulator.Tile;

public class LightSensor implements Sensor{
  private Simulator sim;
  private SimulatedEntity simEntity;

  @Override
  public int getValue() {
    // determine position of light-sensor
    Point pos = simEntity.getCurrentOnTileCoordinates();
    double rads = Math.toRadians(simEntity.getDir());
    int x = (int) (pos.getX() - SimulatedEntity.LIGHTSENSOR_DISTANCE * Math.sin(rads));
    int y = (int) (pos.getY() - SimulatedEntity.LIGHTSENSOR_DISTANCE * Math.cos(rads));

    // if we go beyond the boundaries of this tile, move to the next and
    // adapt the x,y coordinates on the new tile
    int dx = 0, dy = 0;
    if (x < 0) {
      dx = -1;
      x += Tile.SIZE;
    }
    if (x >= Tile.SIZE) {
      dx = +1;
      x -= Tile.SIZE;
    }
    if (y < 0) {
      dy = -1;
      y += Tile.SIZE;
    }
    if (y >= Tile.SIZE) {
      dy = +1;
      y -= Tile.SIZE;
    }
    // get correct tile
    Point tilePos = simEntity.getCurrentTileCoordinates();
    tilePos.translate(dx, dy);
    Tile tile = sim.getCurrentTile(tilePos);

    int color = tile.getColorAt(x, y);

    // TODO: add random abberations
    //this.sensorValues[Model.S4] =
    //        color == Tile.WHITE ? 100 : (color == Tile.BLACK ? 0 : 70);
    return color == Tile.WHITE ? 100 : (color == Tile.BLACK ? 0 : 70);
  }

  @Override
  public void useSimulator(Simulator sim) {
    this.sim = sim;
  }

  @Override
  public void useSimulatedEntity(SimulatedEntity simEntity) {
    this.simEntity = simEntity;
  }
  
}
