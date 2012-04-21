package penoplatinum.simulator.tiles;

import junit.framework.TestCase;
import org.junit.Test;
import static org.junit.Assert.*;
import penoplatinum.util.Bearing;

public class SectorTest extends TestCase {

  /**
   * Test of getBarcode method, of class Sector.
   */
  @Test
  public void testGetBarcode() {
    System.out.println("getBarcode");
    Sector instance = new Sector();
    instance.addBarcode(3, 0);
    assertEquals(6, instance.getBarcode8Bit());
    instance.addBarcode(15, 0);
    assertEquals(30, instance.getBarcode8Bit());
  }

  /**
   * Test of hasBarcode method, of class Sector.
   */
  @Test
  public void testHasBarcode() {
    System.out.println("hasBarcode");
    Sector instance = new Sector();
    assertEquals(false, instance.hasBarcode());
    instance.addBarcode(0, 0);
    assertEquals(true, instance.hasBarcode());
    instance.removeBarcode();
    assertEquals(false, instance.hasBarcode());
  }

  /**
   * Test of getBarcodeLocation method, of class Sector.
   */
  @Test
  public void testGetBarcodeLocation() {
    System.out.println("getBarcodeLocation");
    Sector instance = new Sector();
    assertEquals(-1, instance.getBarcodeLocation());
    int dir = 0;
    instance.addBarcode(3, dir);
    assertEquals(dir, instance.getBarcodeLocation());
    dir = 1;
    instance.addBarcode(3, dir);
    assertEquals(dir, instance.getBarcodeLocation());
    dir = 2;
    instance.addBarcode(3, dir);
    assertEquals(dir, instance.getBarcodeLocation());
    dir = 3;
    instance.addBarcode(3, dir);
    assertEquals(dir, instance.getBarcodeLocation());
  }

  /**
   * Test of getBarcodeLine method, of class Sector.
   */
  @Test
  public void testGetBarcodeLine() {
    System.out.println("getBarcodeLine");
    Sector instance = new Sector();
    instance.addBarcode(0, 1);
    for (int i = 0; i < 8; i++) {
      assertEquals(Sector.BLACK, instance.getBarcodeLine(i));
    }
    instance.addBarcode(42, 0);
    assertEquals(Sector.BLACK, instance.getBarcodeLine(0));
    assertEquals(Sector.WHITE, instance.getBarcodeLine(1));
    assertEquals(Sector.BLACK, instance.getBarcodeLine(2));
    assertEquals(Sector.WHITE, instance.getBarcodeLine(3));
    assertEquals(Sector.BLACK, instance.getBarcodeLine(4));
    assertEquals(Sector.WHITE, instance.getBarcodeLine(5));
    assertEquals(Sector.BLACK, instance.getBarcodeLine(6));
    assertEquals(Sector.BLACK, instance.getBarcodeLine(7));
    instance.addBarcode(21, 0);
    assertEquals(Sector.BLACK, instance.getBarcodeLine(0));
    assertEquals(Sector.BLACK, instance.getBarcodeLine(1));
    assertEquals(Sector.WHITE, instance.getBarcodeLine(2));
    assertEquals(Sector.BLACK, instance.getBarcodeLine(3));
    assertEquals(Sector.WHITE, instance.getBarcodeLine(4));
    assertEquals(Sector.BLACK, instance.getBarcodeLine(5));
    assertEquals(Sector.WHITE, instance.getBarcodeLine(6));
    assertEquals(Sector.BLACK, instance.getBarcodeLine(7));
    instance.addBarcode(17, 0);
    assertEquals(Sector.BLACK, instance.getBarcodeLine(0));
    assertEquals(Sector.BLACK, instance.getBarcodeLine(1));
    assertEquals(Sector.WHITE, instance.getBarcodeLine(2));
    assertEquals(Sector.BLACK, instance.getBarcodeLine(3));
    assertEquals(Sector.BLACK, instance.getBarcodeLine(4));
    assertEquals(Sector.BLACK, instance.getBarcodeLine(5));
    assertEquals(Sector.WHITE, instance.getBarcodeLine(6));
    assertEquals(Sector.BLACK, instance.getBarcodeLine(7));
  }

  /**
   * Test of getBarcodeColor method, of class Sector.
   */
  @Test
  public void testGetBarcodeColor() {
    System.out.println("getBarcodeColor");
    Sector instance = new Sector();
    int size = instance.getSize();
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        assertEquals(Sector.NO_COLOR, instance.getBarcodeColor(i, j));
      }
    }
  }

  @Test
  public void testGetBarcodeColorN() {
    System.out.println("getBarcodeColorN");
    Sector instance = new Sector();
    int size = instance.getSize();
    instance.addBarcode(42, 0);
    int startBarcode = Sector.SIZE / 2 - Sector.BARCODE_WIDTH / 2;
    int endBarcode = Sector.SIZE / 2 + Sector.BARCODE_WIDTH / 2;
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < startBarcode; j++) {
        assertEquals(Sector.NO_COLOR, instance.getBarcodeColor(i, j));
      }
    }
    for (int j = startBarcode; j < endBarcode; j += 2) {
      int color = instance.getBarcodeColor(0, j);
      assertNotSame(Sector.NO_COLOR, instance.getBarcodeColor(0, j));
      for (int i = 0; i < size; i += 1) {
        assertEquals(color, instance.getBarcodeColor(i, j));
        assertEquals(color, instance.getBarcodeColor(i, j + 1));
      }
    }
    for (int i = 0; i < size; i++) {
      for (int j = endBarcode; j < size; j++) {
        assertEquals(Sector.NO_COLOR, instance.getBarcodeColor(i, j));
      }
    }
    assertEquals(Sector.BLACK, instance.getBarcodeColor(0, startBarcode));
    assertEquals(Sector.WHITE, instance.getBarcodeColor(0, startBarcode + 2));
    assertEquals(Sector.BLACK, instance.getBarcodeColor(0, startBarcode + 4));
    assertEquals(Sector.WHITE, instance.getBarcodeColor(0, startBarcode + 6));
    assertEquals(Sector.BLACK, instance.getBarcodeColor(0, startBarcode + 8));
    assertEquals(Sector.WHITE, instance.getBarcodeColor(0, startBarcode + 10));
    assertEquals(Sector.BLACK, instance.getBarcodeColor(0, startBarcode + 12));
    assertEquals(Sector.BLACK, instance.getBarcodeColor(0, startBarcode + 14));
  }

  @Test
  public void testGetBarcodeColorE() {
    System.out.println("getBarcodeColorE");
    Sector instance = new Sector();
    int size = instance.getSize();
    instance.addBarcode(63, 1);
    int startBarcode = Sector.SIZE / 2 - Sector.BARCODE_WIDTH / 2;
    int endBarcode = Sector.SIZE / 2 + Sector.BARCODE_WIDTH / 2;
    for (int x = 0; x < startBarcode; x++) {
      for (int y = 0; y < size; y++) {
        assertEquals(Sector.NO_COLOR, instance.getBarcodeColor(x, y));
      }
    }
    for (int x = startBarcode; x < endBarcode; x += 2) {
      int color = instance.getBarcodeColor(x, 0);
      assertNotSame(Sector.NO_COLOR, color);
      for (int y = 0; y < size; y++) {
        assertEquals(color, instance.getBarcodeColor(x, y));
        assertEquals(color, instance.getBarcodeColor(x + 1, y));
      }
    }
    for (int x = endBarcode; x < size; x++) {
      for (int y = 0; y < size; y++) {
        assertEquals(Sector.NO_COLOR, instance.getBarcodeColor(x, y));
      }
    }
    assertEquals(Sector.BLACK, instance.getBarcodeColor(startBarcode, 0));
    assertEquals(Sector.WHITE, instance.getBarcodeColor(startBarcode + 2, 0));
    assertEquals(Sector.WHITE, instance.getBarcodeColor(startBarcode + 4, 0));
    assertEquals(Sector.WHITE, instance.getBarcodeColor(startBarcode + 6, 0));
    assertEquals(Sector.WHITE, instance.getBarcodeColor(startBarcode + 8, 0));
    assertEquals(Sector.WHITE, instance.getBarcodeColor(startBarcode + 10, 0));
    assertEquals(Sector.WHITE, instance.getBarcodeColor(startBarcode + 12, 0));
    assertEquals(Sector.BLACK, instance.getBarcodeColor(startBarcode + 14, 0));
  }

  @Test
  public void testGetColorAtLineN() {
    System.out.println("getColorAtLineN");
    Sector instance = new Sector();
    final int size = instance.getSize();
    instance.addWall(0);
    for (int i = 0; i < size; i++) {
      assertEquals(Sector.NO_COLOR, instance.getColorAt(i, 0));
      assertEquals(Sector.WHITE, instance.getColorAt(i, size - 1));
    }
    for (int i = 1; i < size - 1; i++) {
      assertEquals(Sector.WHITE, instance.getColorAt(0, i));
      assertEquals(Sector.WHITE, instance.getColorAt(size - 1, i));
    }
  }

  @Test
  public void testGetColorAtLineE() {
    System.out.println("getColorAtLineE");
    Sector instance = new Sector();
    final int size = instance.getSize();
    instance.addWall(1);
    for (int i = 1; i < size - 1; i++) {
      assertEquals(Sector.NO_COLOR, instance.getColorAt(size - 1, i));
      assertEquals(Sector.WHITE, instance.getColorAt(0, i));
    }
    for (int i = 1; i < size - 1; i++) {
      assertEquals(Sector.WHITE, instance.getColorAt(i, 0));
      assertEquals(Sector.WHITE, instance.getColorAt(i, size - 1));
    }
  }

  @Test
  public void testGetColorAtLineS() {
    System.out.println("getColorAtLineS");
    Sector instance = new Sector();
    final int size = instance.getSize();
    instance.addWall(2);
    for (int i = 0; i < size; i++) {
      assertEquals(Sector.NO_COLOR, instance.getColorAt(i, size - 1));
      assertEquals(Sector.WHITE, instance.getColorAt(i, 0));
    }
    for (int i = 1; i < size - 1; i++) {
      assertEquals(Sector.WHITE, instance.getColorAt(0, i));
      assertEquals(Sector.WHITE, instance.getColorAt(size - 1, i));
    }
  }

  @Test
  public void testGetColorAtLineW() {
    System.out.println("getColorAtLineW");
    Sector instance = new Sector();
    final int size = instance.getSize();
    instance.addWall(3);
    for (int i = 0; i < size; i++) {
      assertEquals(Sector.NO_COLOR, instance.getColorAt(0, i));
      assertEquals(Sector.WHITE, instance.getColorAt(size - 1, i));
    }
    for (int i = 1; i < size - 1; i++) {
      assertEquals(Sector.WHITE, instance.getColorAt(i, 0));
      assertEquals(Sector.WHITE, instance.getColorAt(i, size - 1));
    }
  }

  /**
   * Test of getColorAt method, of class Sector.
   */
  @Test
  public void testGetColorAt() {
    System.out.println("getColorAt");
    Sector instance = new Sector();
    final int size = instance.getSize();
    for (int i = 0; i < size; i++) {
      assertEquals(Sector.WHITE, instance.getColorAt(0, i));
      assertEquals(Sector.WHITE, instance.getColorAt(i, 0));
      assertEquals(Sector.WHITE, instance.getColorAt(size - 1, i));
      assertEquals(Sector.WHITE, instance.getColorAt(i, size - 1));
    }
    for (int i = 1; i < size - 1; i++) {
      for (int j = 1; j < size - 1; j++) {
        assertEquals(Sector.NO_COLOR, instance.getColorAt(i, j));
      }
    }
  }

  /**
   * Test of hasWall method, of class Sector.
   */
  @Test
  public void testHasWall() {
    System.out.println("hasWall");
    Sector instance = new Sector();
    for(Bearing b : Bearing.values()){
      if(b.equals(Bearing.UNKNOWN)){
        continue;
      }
      assertFalse(instance.hasWall(b));
    }
    instance.addWall(0);
    assertTrue(instance.hasWall(Bearing.N));
    assertTrue(instance.hasWall(Bearing.NE));
    assertTrue(instance.hasWall(Bearing.NW));
    instance.addWall(1).removeWall(0);
    assertTrue(instance.hasWall(Bearing.E));
    assertTrue(instance.hasWall(Bearing.NE));
    assertTrue(instance.hasWall(Bearing.SE));
    instance.addWall(2).removeWall(1);
    assertTrue(instance.hasWall(Bearing.S));
    assertTrue(instance.hasWall(Bearing.SW));
    assertTrue(instance.hasWall(Bearing.SE));
    instance.addWall(3).removeWall(2);
    assertTrue(instance.hasWall(Bearing.W));
    assertTrue(instance.hasWall(Bearing.SW));
    assertTrue(instance.hasWall(Bearing.NW));
  }

  /**
   * Test of toString method, of class Sector.
   */
  @Test
  public void testToString() {
    System.out.println("toString");
    Sector instance = new Sector();
    assertEquals("0000000000000", instance.toString());
    instance.addWall(0).addWall(1).addWall(2).addWall(3);
    assertEquals("0000000001111", instance.toString());
    instance.addBarcode(63, 3);
    assertEquals("1111111111111", instance.toString());
    instance.removeWall(0);
    instance.removeWall(1);
    instance.removeWall(2);
    instance.removeWall(3);
    assertEquals("1111111110000", instance.toString());
    instance.removeBarcode();
    assertEquals("0000000000000", instance.toString());
    instance = new Sector(43);
    assertEquals("0000000101011", instance.toString());
    instance = new Sector(2752);
    assertEquals("0101011000000", instance.toString());
  }

  /**
   * Test of addWall method, of class Sector.
   */
  @Test
  public void testAddWall() {
    System.out.println("addWall");
    Sector instance = new Sector();
    instance.addWall(0);
    assertEquals("0000000000001", instance.toString());
    instance.addWall(1);
    assertEquals("0000000000011", instance.toString());
    instance.addWall(2);
    assertEquals("0000000000111", instance.toString());
    instance.addWall(3);
    assertEquals("0000000001111", instance.toString());
  }

  /**
   * Test of addBarcode method, of class Sector.
   */
  @Test
  public void testAddBarcode() {
    System.out.println("addBarcode");
    Sector instance = new Sector();
    assertEquals("0000000000000", instance.toString());
    instance.addBarcode(3, 0);
    assertEquals("0010000110000", instance.toString());
    instance.addBarcode(4, 1);
    assertEquals("0110001000000", instance.toString());
    instance.addBarcode(63, 2);
    assertEquals("1011111110000", instance.toString());
    instance.addBarcode(64, 3);
    assertEquals("1110000000000", instance.toString());
  }

  /**
   * Test of removeWall method, of class Sector.
   */
  @Test
  public void testRemoveWall() {
    System.out.println("removeWall");
    Sector instance = new Sector();
    instance.addWall(0).addWall(1).addWall(2).addWall(3);
    assertEquals("0000000001111", instance.toString());
    instance.removeWall(0);
    assertEquals("0000000001110", instance.toString());
    instance.removeWall(1);
    assertEquals("0000000001100", instance.toString());
    instance.removeWall(2);
    assertEquals("0000000001000", instance.toString());
    instance.removeWall(3);
    assertEquals("0000000000000", instance.toString());
  }

  /**
   * Test of removeBarcode method, of class Sector.
   */
  @Test
  public void testRemoveBarcode() {
    System.out.println("removeBarcode");
    Sector instance = new Sector();
    assertEquals("0000000000000", instance.toString());
    instance.addBarcode(55, 2);
    assertEquals("1011101110000", instance.toString());
    instance.removeBarcode();
    assertEquals("0000000000000", instance.toString());
  }

  /**
   * Test of getSize method, of class Sector.
   */
  @Test
  public void testGetSize() {
    System.out.println("getSize");
    Sector instance = new Sector();
    int expResult = 40;
    int result = instance.getSize();
    assertEquals(expResult, result);
  }

  /**
   * Test of clone method, of class Sector.
   */
  @Test
  public void testClone() {
    System.out.println("clone");
    int data = 0;
    Sector instance = new Sector(data);
    assertEquals(data, instance.clone().getData());
    data = 43;
    instance = new Sector(data);
    assertEquals(data, instance.clone().getData());
    data = 2753;
    instance = new Sector(data);
    assertEquals(data, instance.clone().getData());
  }

  /**
   * Test of getDrawer method, of class Sector.
   */
  @Test
  public void testGetDrawer() {
    System.out.println("getDrawer");
    Sector instance = new Sector();
    assertEquals(SectorDraw.class, instance.getDrawer().getClass());
  }

  /**
   * Test of getData method, of class Sector.
   */
  @Test
  public void testGetData() {
    System.out.println("getData");
    int data = 0;
    Sector instance = new Sector(data);
    assertEquals(data, instance.getData());
    data = 43;
    instance = new Sector(data);
    assertEquals(data, instance.getData());
    data = 1539; //the current time ;)
    instance = new Sector(data);
    assertEquals(data, instance.getData());
  }
}
