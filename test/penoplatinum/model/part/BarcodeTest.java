package penoplatinum.model.part;

/**
 * BarcodeTest
 * 
 * Tests the Barcode class
 * 
 * @author Team Platinum
 */

import junit.framework.TestCase;

import penoplatinum.util.LightColor;

public class BarcodeTest extends TestCase {
  
  private static int LINE_WIDTH            = 12;
  private static int INCOMPLETE_LINE_WIDTH = 3;
  private static int BARCODE_WIDTH         = 8 * LINE_WIDTH;

  private Barcode barcode;


  public BarcodeTest(String name) {
    super(name);
  }
  
  public void testReverse(){
    assertEquals( 6, Barcode.reverse(6, 4));
    assertEquals(96, Barcode.reverse(6, 8));
    
    assertEquals(10, Barcode.reverse(5, 4));
    assertEquals( 2, Barcode.reverse(4, 4));
  }

  public void testCopyConstructor() {
    this.createBarcode("11010110");
    assertEquals(214, this.barcode.translate());
    Barcode barcodeCopy = new Barcode(this.barcode);
    assertEquals(214, barcodeCopy.translate());
  }

  public void testBadCopyConstructor() {
    Barcode barcodeCopy = new Barcode(null);
    assertEquals(-1, barcodeCopy.translate());
  }
  
  public void testWhiteBarcode() {
    this.createBarcode("11111111");
    assertEquals(255, this.barcode.translate());
  }

  public void testBlackBarcode() {
    this.createBarcode("00000000");
    assertEquals(0, this.barcode.translate());
  }

  public void testTranslateCorrectBarcode(){
    this.createBarcode("01101010");
    assertEquals(106, this.barcode.translate());
  }
  
  public void testTranslateVeryIncorrectBarcode() {
    this.createBarcode();
    this.addLine(LightColor.BLACK);
    this.addReadings(LightColor.WHITE, INCOMPLETE_LINE_WIDTH);
    this.addReadings(LightColor.BLACK, INCOMPLETE_LINE_WIDTH);
    this.addReadings(LightColor.WHITE, INCOMPLETE_LINE_WIDTH);
    this.addReadings(LightColor.BLACK, INCOMPLETE_LINE_WIDTH);
    this.addLine(LightColor.WHITE);
    this.addLine(LightColor.BLACK);
    this.addLine(LightColor.WHITE);
    this.addLine(LightColor.WHITE);
    this.addLine(LightColor.BLACK);
    this.addLine(LightColor.WHITE);

    assertEquals(-1, this.barcode.translate());
  }

  public void testTranslateLittleIncorrectBarcode() {
    this.createBarcode();
    this.addLine(LightColor.BLACK);
    this.addReadings(LightColor.BLACK, INCOMPLETE_LINE_WIDTH);
    this.addReadings(LightColor.WHITE, INCOMPLETE_LINE_WIDTH);
    this.addReadings(LightColor.WHITE, INCOMPLETE_LINE_WIDTH);
    this.addReadings(LightColor.WHITE, INCOMPLETE_LINE_WIDTH);
    this.addLine(LightColor.WHITE);
    this.addLine(LightColor.BLACK);
    this.addLine(LightColor.WHITE);
    this.addLine(LightColor.WHITE);
    this.addLine(LightColor.BLACK);
    this.addLine(LightColor.WHITE);

    assertEquals(109, this.barcode.translate());
  }

  // construction helpers

  private void createBarcode() {
    this.barcode = new Barcode();
  }

  private void createBarcode(String bitfield) {
    this.createBarcode();
    for(char c : bitfield.toCharArray()) {
      this.addLine(c == '0' ? LightColor.BLACK : LightColor.WHITE);
    }
  }
  
  private void addReadings(LightColor color, int amount) {
    for(int i = 0; i<amount; i++) { this.barcode.addColor(color); }
  }
  
  private void addLine(LightColor color) {
    this.addReadings(color, LINE_WIDTH);
  }
}
