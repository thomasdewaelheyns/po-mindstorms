package penoplatinum.simulator;

// TODO: this Model Interface now contains ALL methods of ALL implementations.
//       we need a really generic model with only generic methods and
//       in case we want specialized methods, we need to cast it to a specific
//       Model class.

import java.util.List;

import penoplatinum.grid.Agent;
import penoplatinum.grid.Sector;
import penoplatinum.grid.Grid;
import penoplatinum.grid.GridView;
import penoplatinum.util.Buffer;
import penoplatinum.util.LightColor;
import penoplatinum.modelprocessor.ModelProcessor;

public interface Model {
  // slightly common
  public Model  setProcessor(ModelProcessor processor);
  public Model  updateSensorValues(int[] values);
  public void   updateSonarValues(List<Integer> distances, List<Integer> angles);
  public String explain();

  // from GhostModel
  
  // when running in a UI environment we can provide a View for the Grid
  public Model displayGridOn(GridView view);
  public Grid getGrid();
  public void addIncomingMessage(String msg);
  public void receiveIncomingMessages(List<String> buffer);
  public List<String> getOutgoingMessages();
  public void clearOutbox();
  public Agent getAgent();
  public boolean hasNewSonarValues();
  public void markSonarValuesProcessed();
  public void updateSector(Sector newSector);
  public Sector getDetectedSector();
  public Sector getCurrentSector();
  public int getLeftFreeDistance();
  public int getFrontFreeDistance();
  public int getRightFreeDistance();
  public boolean sectorHasChanged();
  public void moveForward();
  public void turnLeft();
  public void turnRight();
  public void clearLastMovement();
  public int getLastMovement();
  
  // from OriginalModel
  // shorthands mapping the sensors/numbers to their technical ports
  public static final int M1 = 0; // right motor
  public static final int M2 = 1; // left motor
  public static final int M3 = 2; // sonar motor
  public static final int S1 = 3; // irSensor WAS touch right
  public static final int S2 = 4; // empty WAS touch left
  public static final int S3 = 5; // sonarsensor
  public static final int S4 = 6; // lightsensor
  public static final int MS1 = 7; // Motor state 1
  public static final int MS2 = 8; // Motor state 1
  public static final int MS3 = 9; // Motor state 1
  public static final int IR0 = 10;
  public static final int IR1 = 11;
  public static final int IR2 = 12;
  public static final int IR3 = 13;
  public static final int IR4 = 14;
  
  public static final int SENSORVALUES_NUM = 15; // number of sensorvalues 
  public static final int MOTORSTATE_FORWARD = 1;
  public static final int MOTORSTATE_BACKWARD = 2;
  public static final int MOTORSTATE_STOPPED = 3;

  public void updateDistances(List<Integer> distances, List<Integer> angles);
  public List<Integer> getDistances();
  public List<Integer> getAngles();
  public int getSensorValue(int num);
  public Boolean isMoving();
  public Boolean isTurning();
  public int[] getSonarValues();
  public int getBarcode();
  public void setBarcode(int barcode);
  public Buffer getLightValueBuffer();
  public Line getLine();
  public void setLine(Line line);
  public float getAverageTacho();
  public String toString();
  public int getGapEndAngle();
  public void setGapEndAngle(int gapEndAngle);
  public boolean isGapFound();
  public void setGapFound(boolean gapFound);
  public int getGapStartAngle();
  public void setGapStartAngle(int gapStartAngle);
  public float getPositionX();
  public void setPositionX(float positionX);
  public float getPositionY();
  public void setPositionY(float positionY);
  public float getDirection();
  public void setDirection(float direction);
  public Boolean isSweepComplete();

  LightColor getCurrentLightColor();
  public void setReadingBarcode(boolean b);
  public boolean isReadingBarcode();

  public void setPacManInNext(boolean b, int x, int y);
  boolean isIsNextToPacman();
}
