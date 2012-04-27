package penoplatinum.model.part;

/**
 * SensorModelPartTest
 * 
 * Tests SensorModelPart class
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 
import static org.mockito.Mockito.*;

import penoplatinum.Config;


public class SensorModelPartTest extends TestCase {

  private SensorModelPart part;


  public SensorModelPartTest(String name) { 
    super(name);
  }
 
  public void testWrongAmountOfSensorValues() {
    this.createModelPart();
    try {
      this.part.updateSensorValues(new int[] { 1, 2, 3 } );
      fail( "less than SENSORVALUES_NUM (15) should throw Exception");
    } catch(Exception e) {}
  }
  
  public void testIsMoving() {
    this.createModelPart();
    this.part.updateSensorValues( new int[] {  1,  2,  3,  4,  5,  6, 7,
                                               Config.MOTORSTATE_FORWARD,
                                               Config.MOTORSTATE_FORWARD,
                                               10, 11, 12, 13, 14, 15 } );
    assertTrue(this.part.isMoving());
    this.part.updateSensorValues( new int[] {  1,  2,  3,  4,  5,  6, 7,
                                               Config.MOTORSTATE_BACKWARD,
                                               Config.MOTORSTATE_FORWARD,
                                               10, 11, 12, 13, 14, 15 } );
    assertTrue(this.part.isMoving());
    this.part.updateSensorValues( new int[] {  1,  2,  3,  4,  5,  6, 7,
                                               Config.MOTORSTATE_FORWARD,
                                               Config.MOTORSTATE_BACKWARD,
                                               10, 11, 12, 13, 14, 15 } );
    assertTrue(this.part.isMoving());
    this.part.updateSensorValues( new int[] {  1,  2,  3,  4,  5,  6, 7,
                                               Config.MOTORSTATE_STOPPED,
                                               Config.MOTORSTATE_FORWARD,
                                               10, 11, 12, 13, 14, 15 } );
    assertTrue(this.part.isMoving());
    this.part.updateSensorValues( new int[] {  1,  2,  3,  4,  5,  6, 7,
                                               Config.MOTORSTATE_FORWARD,
                                               Config.MOTORSTATE_STOPPED,
                                               10, 11, 12, 13, 14, 15 } );
    assertTrue(this.part.isMoving());
    this.part.updateSensorValues( new int[] {  1,  2,  3,  4,  5,  6, 7,
                                               Config.MOTORSTATE_STOPPED,
                                               Config.MOTORSTATE_BACKWARD,
                                               10, 11, 12, 13, 14, 15 } );
    assertTrue(this.part.isMoving());
    this.part.updateSensorValues( new int[] {  1,  2,  3,  4,  5,  6, 7,
                                               Config.MOTORSTATE_BACKWARD,
                                               Config.MOTORSTATE_STOPPED,
                                               10, 11, 12, 13, 14, 15 } );
    assertTrue(this.part.isMoving());
    this.part.updateSensorValues( new int[] {  1,  2,  3,  4,  5,  6, 7, 
                                               Config.MOTORSTATE_STOPPED,
                                               Config.MOTORSTATE_STOPPED,
                                               10, 11, 12, 13, 14, 15 } );
    assertFalse(this.part.isMoving());
  }

  public void testIsTurning() {
    this.createModelPart();
    this.part.updateSensorValues( new int[] {  1,  2,  3,  4,  5,  6, 7,
                                               Config.MOTORSTATE_FORWARD,
                                               Config.MOTORSTATE_FORWARD,
                                               10, 11, 12, 13, 14, 15 } );
    assertFalse(this.part.isTurning());
    this.part.updateSensorValues( new int[] {  1,  2,  3,  4,  5,  6, 7,
                                               Config.MOTORSTATE_BACKWARD,
                                               Config.MOTORSTATE_FORWARD,
                                               10, 11, 12, 13, 14, 15 } );
    assertTrue(this.part.isTurning());
    this.part.updateSensorValues( new int[] {  1,  2,  3,  4,  5,  6, 7,
                                               Config.MOTORSTATE_FORWARD,
                                               Config.MOTORSTATE_BACKWARD,
                                               10, 11, 12, 13, 14, 15 } );
    assertTrue(this.part.isTurning());
    this.part.updateSensorValues( new int[] {  1,  2,  3,  4,  5,  6, 7,
                                               Config.MOTORSTATE_STOPPED,
                                               Config.MOTORSTATE_FORWARD,
                                               10, 11, 12, 13, 14, 15 } );
    assertTrue(this.part.isTurning());
    this.part.updateSensorValues( new int[] {  1,  2,  3,  4,  5,  6, 7,
                                               Config.MOTORSTATE_FORWARD,
                                               Config.MOTORSTATE_STOPPED,
                                               10, 11, 12, 13, 14, 15 } );
    assertTrue(this.part.isTurning());
    this.part.updateSensorValues( new int[] {  1,  2,  3,  4,  5,  6, 7,
                                               Config.MOTORSTATE_STOPPED,
                                               Config.MOTORSTATE_BACKWARD,
                                               10, 11, 12, 13, 14, 15 } );
    assertTrue(this.part.isTurning());
    this.part.updateSensorValues( new int[] {  1,  2,  3,  4,  5,  6, 7,
                                               Config.MOTORSTATE_BACKWARD,
                                               Config.MOTORSTATE_STOPPED,
                                               10, 11, 12, 13, 14, 15 } );
    assertTrue(this.part.isTurning());
    this.part.updateSensorValues( new int[] {  1,  2,  3,  4,  5,  6, 7,
                                               Config.MOTORSTATE_STOPPED,
                                               Config.MOTORSTATE_STOPPED,
                                               10, 11, 12, 13, 14, 15 } );
    assertFalse(this.part.isTurning());
  }
  
  public void testGetSonarDistance() {
    this.createModelPart();
    this.part.updateSensorValues( new int[] {  1,  2,  3,  4,  5,  6,  7,  8,
                                               9, 10, 11, 12, 13, 14, 15 } );
    assertEquals( 6, this.part.getSonarDistance());
  }

  public void testGetSonarAngle() {
    this.createModelPart();
    this.part.updateSensorValues( new int[] {  1,  2,  3,  4,  5,  6,  7,  8,
                                               9, 10, 11, 12, 13, 14, 15 } );
    assertEquals( 6, this.part.getSonarDistance());
  }

  public void testGetIRDirection() {
    this.createModelPart();
    this.part.updateSensorValues( new int[] {  1,  2,  3,  4,  5,  6,  7,  8,
                                               9, 10, 11, 12, 13, 14, 15 } );
    assertEquals( 4, this.part.getIRDirection());
  }
  
  public void testGetIRValue() {
    this.createModelPart();
    this.part.updateSensorValues( new int[] {  1,  2,  3,  4,  5,  6,  7,  8,
                                               9, 10, 11, 12, 13, 14, 15 } );
    assertEquals( 11, this.part.getIRValue(0));
    assertEquals( 12, this.part.getIRValue(1));
    assertEquals( 13, this.part.getIRValue(2));
    assertEquals( 14, this.part.getIRValue(3));
    assertEquals( 15, this.part.getIRValue(4));
  }

  public void testGetLightSensorValue() { 
    this.createModelPart();
    this.part.updateSensorValues( new int[] {  1,  2,  3,  4,  5,  6,  7,  8,
                                               9, 10, 11, 12, 13, 14, 15 } );
    assertEquals( 6, this.part.getSonarDistance());
  }
  
  public void testTotalTurnedAngle() {
    this.createModelPart();
    this.part.setTotalTurnedAngle(15.15f);
    assertEquals(15.15f, this.part.getTotalTurnedAngle(), 0.01);
  }

  public void testGetValuesId() {
    this.createModelPart();
    this.part.updateSensorValues( new int[] {  1,  2,  3,  4,  5,  6,  7,  8,
                                               9, 10, 11, 12, 13, 14, 15 } );
    assertEquals(1, this.part.getValuesId());
    this.part.updateSensorValues( new int[] {  1,  2,  3,  4,  5,  6,  7,  8,
                                               9, 10, 11, 12, 13, 14, 15 } );
    assertEquals(2, this.part.getValuesId());
  }


  // construction helpers
  
  private void createModelPart() {
    this.part = new SensorModelPart();
  }
}
