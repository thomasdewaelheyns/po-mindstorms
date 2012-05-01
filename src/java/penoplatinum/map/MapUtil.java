package penoplatinum.map;

import penoplatinum.simulator.entities.SimulatedEntity;
import penoplatinum.simulator.tiles.Tile;
import penoplatinum.simulator.tiles.TileGeometry;
import penoplatinum.util.Bearing;
import penoplatinum.util.Point;
import penoplatinum.util.Position;

public class MapUtil {

  public static Point getCurrentTileCoordinates(double x, double y, int size) {
    // determine tile coordinates we're on
    int left = (int) (x / size) + 1;
    int top = (int) (y / size) + 1;
    return new Point(left, top);
  }

  public static Point getCurrentOnTileCoordinates(double x, double y, int size) {
    // determine tile coordinates on the tile we're on
    int left = (int) (x % size);
    int top = (int) (y % size);
    return new Point(left, top);
  }

  public static int findHitDistance(Map map, int angle, double x, double y) {
    angle %= 360;
    angle += 360;
    angle %= 360;
    int EW = findHitPointEW(map, angle, x, y);
    int NS = findHitPointNS(map, angle, x, y);
    return (int) Math.min(EW, NS);
  }

  public static int findHitPointNS(Map map, int angle, double x, double y) {
    if (angle == 0 || angle == 180) {
      return Integer.MAX_VALUE;
    }
    Bearing b = Bearing.S;
    int diff = 1;
    int position = 0;
    if (angle < 180) {
      diff = -1;
      position -= diff;
      b = b.reverse();
    }
    double startY = y % map.getFirst().getSize();
    while (true) {
      position += diff;
      double distY = position * map.getFirst().getSize() - startY;
      int hitX = (int) Math.round(x + TileGeometry.height(distY, angle - 270));
      int hitY = (int) (y + position * map.getFirst().getSize() - startY);
      int tileX = (int) (hitX / map.getFirst().getSize()) + 1;
      int tileY = (int) (hitY / map.getFirst().getSize()) + (diff == -1 ? 1 : 0);

      if (hitX % map.getFirst().getSize() == 0) {
        Tile temp = map.get(tileX - 1, tileY);
        if (temp != null && temp.hasWall(b)) {
          return (int) TileGeometry.getDistance(x, y, new Point(hitX, hitY));
        }
      }

      Tile temp = map.get(tileX, tileY);
      if (temp == null) {
        return Integer.MAX_VALUE;
      }
      if (temp.hasWall(b)) {
        return (int) TileGeometry.getDistance(x, y, new Point(hitX, hitY));
      }
    }
  }

  public static int findHitPointEW(Map map, int angle, double x, double y) {
    if (angle == 90 || angle == 270) {
      return Integer.MAX_VALUE;
    }
    Bearing b = Bearing.E;
    int position = 0;
    int diff = 1;
    if (angle > 90 && angle < 270) {
      diff = -1;
      position -= diff;
      b = b.reverse();
    }
    double startX = x % map.getFirst().getSize();
    while (true) {
      position += diff;
      double distX = position * map.getFirst().getSize() - startX;
      int hitY = (int) Math.round(y - TileGeometry.height(distX, angle));
      int hitX = (int) (x + position * map.getFirst().getSize() - startX);
      int tileX = (hitX / map.getFirst().getSize()) + (diff == -1 ? 1 : 0);
      int tileY = (hitY / map.getFirst().getSize()) + 1;


      if (hitY % map.getFirst().getSize() == 0) {
        Tile temp = map.get(tileX, tileY - 1);
        if (temp != null && temp.hasWall(b)) {
          return (int) TileGeometry.getDistance(x, y, new Point(hitX, hitY));
        }
      }

      Tile temp = map.get(tileX, tileY);
      if (temp == null) {
        return Integer.MAX_VALUE;
      }
      if (temp.hasWall(b)) {
        return (int) TileGeometry.getDistance(x, y, new Point(hitX, hitY));
      }
    }
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
