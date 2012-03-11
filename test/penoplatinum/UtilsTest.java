package penoplatinum;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import org.junit.*;
import penoplatinum.Utils;
import static org.junit.Assert.*;
import penoplatinum.map.MapFactorySector;
import penoplatinum.simulator.tiles.TileGeometry;

/**
 *
 * @author MHGameWork
 */
public class UtilsTest {

  @Test
  public void testClampLooped() throws FileNotFoundException {
   

    assertEquals(30, Utils.ClampLooped(130, 10, 110));
    assertEquals(80,Utils.ClampLooped(-20, 10, 110));
  }
}
