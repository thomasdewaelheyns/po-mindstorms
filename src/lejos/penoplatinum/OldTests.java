package penoplatinum;

import penoplatinum.grid.Sector;
import penoplatinum.util.Utils;

public class OldTests {
  

  public static void testCountMemory() {
    int step = 1024 * 3;
    int size = step;
    byte[] buffer;
    for (int i = 0; i < 1000; i++) {
      buffer = new byte[size];
      System.out.println(buffer.length + " " + Runtime.getRuntime().freeMemory());
      Utils.Sleep(1000);

      size += step;
      buffer = null;
      System.gc();
    }
  }
  

  public static void testGridMemory() {
    SimpleGrid[] grids = new SimpleGrid[500];
    int num = 0;
    int k = 0; k < grids.length; k++) {
      grids[k] = new SimpleGrid();

      for (int i = 0; i < 12; i++) {
        for (int j = 0; j < 12; j++) {
          Sector s = new Sector(grids[k]).setCoordinates(i, j);
          int ff = s.getLeft() + 3;
          grids[k].addSector(s);

          num++;
          System.out.println("Wee! " + num + " " + k + " " + i + " " + j);


        }
      }
    }
  }
    for (int k = 0; k < grids.length; k++) {
      grids[k] = new SimpleGrid();

      for (int i = 0; i < 12; i++) {
        for (int j = 0; j < 12; j++) {
          Sector s = new Sector(grids[k]).setCoordinates(i, j);
          int ff = s.getLeft() + 3;
          grids[k].addSector(s);

          num++;
          System.out.println("Wee! " + num + " " + k + " " + i + " " + j);


        }
      }
    }
  }



  private static boolean startMeasurement(IRSeekerV2 seeker, int[] angles, Motor m, int startAngle) {
    int count = 0;
    while (!Button.ESCAPE.isPressed()) {
      for (int i = 0; i < angles.length; i++) {
        m.rotateTo(startAngle + angles[i], false);
        int dir = seeker.getDirection();
        String str = count + "," + dir;
        for (int j = 1; j < 6; j++) {
          str += "," + seeker.getSensorValue(j);
        }
        Utils.Log(str);
        count++;
        Utils.Sleep(1000);
      }
    }
    return false;
  }

  
  
}
