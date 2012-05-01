package penoplatinum.simulator.sensors;

import penoplatinum.map.MapUtil;
import penoplatinum.util.CircularQueue;
import penoplatinum.simulator.Sensor;
import penoplatinum.simulator.entities.SimulatedEntity;
import penoplatinum.simulator.Simulator;
import penoplatinum.simulator.tiles.Sector;
import penoplatinum.simulator.tiles.Tile;
import penoplatinum.util.Point;

public class LightSensor implements Sensor {

  public static final int BLACK = 440;

  public static final int WHITE = 570;

  public static final int BROWN = 500;

  public static final int LIGHTBUFFER_SIZE = 5;

  private Simulator sim;

  private SimulatedEntity simEntity;

  private CircularQueue<Integer> lightValues = new CircularQueue<Integer>(LIGHTBUFFER_SIZE);

  public LightSensor() {
    for (int i = 0; i < LIGHTBUFFER_SIZE; i++) {
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
    Point pos = MapUtil.getCurrentOnTileCoordinates(simEntity.getPosX(), simEntity.getPosY(), sim.getTileSize());
    double rads = Math.toRadians(simEntity.getDirection());
    int x = (int) Math.round((pos.getX() - SimulatedEntity.LIGHTSENSOR_DISTANCE * Math.sin(rads)));
    int y = (int) Math.round((pos.getY() - SimulatedEntity.LIGHTSENSOR_DISTANCE * Math.cos(rads)));

    // if we go beyond the boundaries of this tile, move to the next and
    // adapt the x,y coordinates on the new tile
    int dx = x / sim.getTileSize() + (x < 0 ? -1 : 0);
    int dy = y / sim.getTileSize() + (y < 0 ? -1 : 0);
    x -= dx * sim.getTileSize();
    y -= dy * sim.getTileSize();

    // get correct tile
    Point tilePos = MapUtil.getCurrentTileCoordinates(simEntity.getPosX(), simEntity.getPosY(), sim.getTileSize());
    tilePos.translate(dx, dy);
    Tile tile = sim.getMap().get(tilePos.getX(), tilePos.getY());
    if (tile == null) {
      return LightSensor.BLACK;
    }

    int color = tile.getColorAt(x, y);

    // TODO: add random abberations
    //this.sensorValues[Model.S4] =
    //        color == Sector.WHITE ? 100 : (color == Sector.BLACK ? 0 : 70);
    return color == Sector.WHITE ? LightSensor.WHITE : (color == Sector.BLACK ? LightSensor.BLACK : LightSensor.BROWN);
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