package penoplatinum.map;

import penoplatinum.simulator.entities.SimulatedEntity;
import penoplatinum.simulator.tiles.Tile;
import penoplatinum.simulator.tiles.TileGeometry;
import penoplatinum.util.Bearing;
import penoplatinum.util.Point;
import penoplatinum.util.Position;

public class MapUtil {

  /**
   * determines the distance to the first hit wall at the current bearing.
   * if the hit is not on a wall on the current tile, we follow the bearing
   * to the next tile and recursively try to find the hit-distance
   */
  public static int findHitDistance(Map map, int angle, int left, int top, double x, double y) {
    // Force angles between 0 and 360 !!!
    angle = (int) penoplatinum.util.Utils.ClampLooped(angle, 0, 360);
    // determine the point on the (virtual) wall on the current tile, where
    // the robot would hit at this bearing
    double dist = 0;
    Bearing bearing = null;
    Tile tile = null;
    Point hit = null;
    do {
      tile = map.get(left, top);
      if (tile == null) {
        return Integer.MAX_VALUE;
      }
      hit = TileGeometry.findHitPoint(x, y, angle, tile.getSize());

      // distance from the starting point to the hit-point on this tile
      dist += TileGeometry.getDistance(x, y, hit);

      // if we don't have a wall on this tile at this bearing, move to the next
      // at the same bearing, starting at the hit point on the tile
      // FIXME: throws OutOfBoundException, because we appear to be moving
      //        through walls.
      bearing = TileGeometry.getHitWall(hit, tile.getSize(), angle);
      left = Position.moveLeft(bearing, left);
      top = Position.moveTop(bearing, top);
      if (x == y) {
        if (angle > 45 && angle <= 135) {
          y = 40;
          x = hit.getX();
        } else if (angle > 135 && angle <= 225) {
          x = 40;
          y = hit.getY();
        } else if (angle > 225 && angle < 315) {
          y = 0;
          x = hit.getX();
        } else {
          x = 0;
          y = hit.getY();
        }
      } else {
        x = hit.getX() == 0 ? tile.getSize() : (hit.getX() == tile.getSize() ? 0 : hit.getX());
        y = hit.getY() == 0 ? tile.getSize() : (hit.getY() == tile.getSize() ? 0 : hit.getY());
      }


    } while (!tile.hasWall(bearing));
    return (int) Math.round(dist);
  }

  public static boolean hasTile(Map map, double positionX, double positionY) {
    int x = (int) positionX / map.getFirst().getSize() + 1;
    int y = (int) positionY / map.getFirst().getSize() + 1;
    return map.exists(x, y);
  }

  public static boolean goesThroughWallX(Map map, SimulatedEntity entity, double dx) {
    double positionX = entity.getPosX();
    double positionY = entity.getPosY();
    double LENGTH_ROBOT = entity.LENGTH_ROBOT;
    final int size = map.getFirst().getSize();

    double posXOnTile = positionX % size;
    int tileX = (int) positionX / size + 1;
    int tileY = (int) positionY / size + 1;
    return (map.get(tileX, tileY).hasWall(Bearing.W)
            && dx < 0 && (posXOnTile + dx < LENGTH_ROBOT))
            || (map.get(tileX, tileY).hasWall(Bearing.E)
            && dx > 0 && (posXOnTile + dx > size - LENGTH_ROBOT));
  }

  public static boolean goesThroughWallY(Map map, SimulatedEntity entity, double dy) {
    double positionX = entity.getPosX();
    double positionY = entity.getPosY();
    double LENGTH_ROBOT = entity.LENGTH_ROBOT;
    final int size = map.getFirst().getSize();

    double posYOnTile = positionY % size;
    int tileX = (int) positionX / size + 1;
    int tileY = (int) positionY / size + 1;

    return (map.get(tileX, tileY).hasWall(Bearing.N)
            && dy < 0 && (posYOnTile + dy < LENGTH_ROBOT))
            || (map.get(tileX, tileY).hasWall(Bearing.S)
            && dy > 0 && (posYOnTile + dy > size - LENGTH_ROBOT));
  }
}
