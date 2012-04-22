package penoplatinum.map;

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
    angle = penoplatinum.util.Utils.ClampLooped(angle, 0, 360);

    // determine the point on the (virtual) wall on the current tile, where
    // the robot would hit at this bearing
    double dist = 0;
    Bearing bearing = null;
    Tile tile = null;
    Point hit = null;
    do {
      tile = map.get(left, top);

      if (tile == null) {
        System.out.println(left + " " + top + " " + hit.getX() + " " + hit.getY() + " " + angle);
        left++;
      }
      hit = TileGeometry.findHitPoint(x, y, angle, tile.getSize());

      // distance from the starting point to the hit-point on this tile
      dist += TileGeometry.getDistance(x, y, hit);

      // if we don't have a wall on this tile at this bearing, move to the next
      // at the same bearing, starting at the hit point on the tile
      // FIXME: throws OutOfBoundException, because we appear to be moving
      //        through walls.
      bearing = TileGeometry.getHitWall(hit, tile.getSize(), angle);
      left = left + Position.moveLeft(bearing, left);
      top = top + Position.moveTop(bearing, top);
      //}
      //System.out.println(left + " " + top);
      x = hit.getX() == 0 ? tile.getSize() : (hit.getX() == tile.getSize() ? 0 : hit.getX());
      y = hit.getY() == 0 ? tile.getSize() : (hit.getY() == tile.getSize() ? 0 : hit.getY());

    } while (!tile.hasWall(bearing));
    return (int) Math.round(dist);
  }
}
