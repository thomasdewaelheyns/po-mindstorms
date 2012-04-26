package penoplatinum.model.part;

/**
 * LightModelPartTest
 * 
 * Tests LightModelPart class
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 
import static org.mockito.Mockito.*;

import penoplatinum.util.LightColor;
import penoplatinum.util.Line;


public class LightModelPartTest extends TestCase {

  private LightModelPart part;


  public LightModelPartTest(String name) { 
    super(name);
  }

  public void testCurrentLightColor() {
    this.setup();
    assertEquals(LightColor.BROWN, this.part.getCurrentLightColor());
    this.part.setCurrentLightColor(LightColor.WHITE);
    assertEquals(LightColor.WHITE, this.part.getCurrentLightColor());
    this.part.setCurrentLightColor(LightColor.BLACK);
    assertEquals(LightColor.BLACK, this.part.getCurrentLightColor());
    this.part.setCurrentLightColor(LightColor.BROWN);
    assertEquals(LightColor.BROWN, this.part.getCurrentLightColor());
  }
  
  public void testCurrentLightValue() {
    this.setup();
    assertEquals(0, this.part.getCurrentLightValue());
    this.part.setCurrentLightValue(123);
    assertEquals(123, this.part.getCurrentLightValue());
  }

  public void testSetAverageLightValue() {
    this.setup();
    assertEquals(0.0f, this.part.getAverageLightValue());
    this.part.setAverageLightValue(0.001f);
    assertEquals(0.001f, this.part.getAverageLightValue());
  }

  public void testIsReadingLine() {
    this.setup();
    assertFalse(this.part.isReadingLine());
    this.part.setLine(Line.WHITE);
    assertTrue(this.part.isReadingLine());
    this.part.setLine(Line.BLACK);
    assertTrue(this.part.isReadingLine());
    this.part.setLine(Line.NONE);
    assertFalse(this.part.isReadingLine());
  }

  public void testGetLine() {
    this.setup();
    assertEquals(Line.NONE, this.part.getLine());
    this.part.setLine(Line.WHITE);
    assertEquals(Line.WHITE, this.part.getLine());
    this.part.setLine(Line.BLACK);
    assertEquals(Line.BLACK, this.part.getLine());
    this.part.setLine(Line.NONE);
    assertEquals(Line.NONE, this.part.getLine());
  }

  // construction helpers
  
  private void setup() {
    this.part = new LightModelPart();
  }
}
