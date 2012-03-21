package penoplatinum.simulator.sensors;

import java.awt.Point;
import penoplatinum.util.CircularQueue;
import penoplatinum.simulator.Sensor;
import penoplatinum.simulator.SimulatedEntity;
import penoplatinum.simulator.Simulator;
import penoplatinum.simulator.tiles.Sector;
import penoplatinum.simulator.tiles.Tile;

public class LightSensor implements Sensor {

  private static final int BLACK = 360;
  private static final int WHITE = 500;
  private static final int BROWN = 450;
  private Simulator sim;
  private SimulatedEntity simEntity;
  public static final int LIGHTBUFFER_SIZE = 5;
  private CircularQueue<Integer> lightValues = new CircularQueue<Integer>(LIGHTBUFFER_SIZE);

  public LightSensor() {
    // TODO: WARNING: magic numbers
    for (int i = 0; i < LIGHTBUFFER_SIZE - 1; i++) {
      lightValues.insert(BROWN);
    }
  }

  @Override
  public int getValue() {
    // Insert a delay on the sensor
    int ret = lightValues.remove();
    lightValues.insert(getActualValue());
    return ret;
//    return getActualValue();
  }

  public int getActualValue() {
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
      x += Sector.SIZE;
    }
    if (x >= Sector.SIZE) {
      dx = +1;
      x -= Sector.SIZE;
    }
    if (y < 0) {
      dy = -1;
      y += Sector.SIZE;
    }
    if (y >= Sector.SIZE) {
      dy = +1;
      y -= Sector.SIZE;
    }
    // get correct tile
    Point tilePos = simEntity.getCurrentTileCoordinates();
    tilePos.translate(dx, dy);
    Tile tile = sim.getCurrentTile(tilePos);
    if (tile == null) {
      return Sector.BLACK;
    }

    int color = tile.getColorAt(x, y);

    // TODO: add random abberations
    //this.sensorValues[Model.S4] =
    //        color == Sector.WHITE ? 100 : (color == Sector.BLACK ? 0 : 70);
    return color == Sector.WHITE ? WHITE : (color == Sector.BLACK ? BLACK : BROWN);
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