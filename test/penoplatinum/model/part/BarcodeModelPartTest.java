package penoplatinum.model.part;

/**
 * BarcodeModelPartTest
 * 
 * Tests BarcodeModelPart class
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 
import static org.mockito.Mockito.*;

import penoplatinum.util.LightColor;


public class BarcodeModelPartTest extends TestCase {

  private BarcodeModelPart part;


  public BarcodeModelPartTest(String name) { 
    super(name);
  }

  public void testStartNewReading() {
    this.setup();
    assertFalse(this.part.isReadingBarcode());    
    this.part.startNewReading();
    assertTrue(this.part.isReadingBarcode());
  }
  
  public void testStopReading() {
    this.setup();
    this.part.startNewReading();
    this.part.finishReading();
    assertFalse(this.part.isReadingBarcode());    
  }

  public void testNeedAtLeast8Readings() {
    this.setup();
    this.part.startNewReading();
    this.simulateBarcode("1111");
    this.part.finishReading();
    assertEquals(-1, this.part.getLastBarcodeValue());
  }
  
  public void testAddReadingsWhileNotReading() {
    this.setup();
    this.simulateBarcode("11111111");
    this.part.finishReading();
    assertEquals(-1, this.part.getLastBarcodeValue());
  }

  public void testGetCurrentBarcodeValue() {
    this.setup();
    this.part.startNewReading();
    this.simulateBarcode("10001011");
    this.part.finishReading();
    assertEquals(139, this.part.getLastBarcodeValue());
  }
  
  public void testGetPreviousBarcodeValue() {
    this.setup();
    assertEquals(-1, this.part.getLastBarcodeValue());
    this.part.startNewReading();
    this.simulateBarcode("10001011");
    this.part.finishReading();
    assertEquals(139, this.part.getLastBarcodeValue());
    this.part.startNewReading();
    assertEquals(139, this.part.getLastBarcodeValue());
  }
  
  public void testDiscardBarcode() {
    this.setup();
    this.part.startNewReading();
    this.simulateBarcode("11111011");
    this.part.finishReading();
    this.part.startNewReading();
    this.simulateBarcode("10001011");
    assertEquals(251, this.part.getLastBarcodeValue());
    this.part.discardReading();
    assertEquals(251, this.part.getLastBarcodeValue());
  }
  
  private void setup() {
    this.part = new BarcodeModelPart();
  }
  
  private void simulateBarcode(String bitfield) {
    for(char c : bitfield.toCharArray()) {
      this.part.addReading(c == '0' ? LightColor.BLACK : LightColor.WHITE);
    }
  }
}
