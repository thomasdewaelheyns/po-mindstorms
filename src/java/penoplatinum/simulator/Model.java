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
import penoplatinum.model.BarcodeModelPart;
import penoplatinum.model.GapModelPart;
import penoplatinum.model.GridModelPart;
import penoplatinum.model.LightModelPart;
import penoplatinum.model.MessageModelPart;
import penoplatinum.model.SensorModelPart;
import penoplatinum.model.SonarModelPart;
import penoplatinum.model.WallsModelPart;
import penoplatinum.util.Buffer;
import penoplatinum.util.LightColor;
import penoplatinum.model.processor.ModelProcessor;

public interface Model {
  // slightly common
  public Model  setProcessor(ModelProcessor processor);
  
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

  BarcodeModelPart getBarcodePart();

  GapModelPart getGapPart();

  GridModelPart getGridPart();

  LightModelPart getLightPart();

  MessageModelPart getMessagePart();

  SensorModelPart getSensorPart();

  SonarModelPart getSonarPart();

  WallsModelPart getWallsPart();
}
