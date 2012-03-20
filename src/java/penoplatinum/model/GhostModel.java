package penoplatinum.model;

/**
 * GhostModel
 * 
 * Implementation of Model, providing all needed by a typical Ghost
 * 
 * @author: Team Platinum
 */
// we're using commons collections HashedMap because HashMap isn't implemented
// on Lejos.
import penoplatinum.model.processor.ModelProcessor;

import penoplatinum.simulator.Model;

public class GhostModel implements Model {
  // little bit of configuration

  // ModelProcessors are a chain of Decorators
  private ModelProcessor processor;
  private BarcodeModelPart barcodePart;
  private GapModelPart gapPart;
  private GridModelPart gridPart;
  private LightModelPart lightPart;
  private MessageModelPart messagePart;
  private SensorModelPart sensorPart;
  private SonarModelPart sonarPart;
  private WallsModelPart wallsPart;

  public GhostModel(String name) {
    barcodePart = new BarcodeModelPart();
    gapPart = new GapModelPart();
    gridPart = new GridModelPart(name);
    lightPart = new LightModelPart();
    messagePart = new MessageModelPart();
    sensorPart = new SensorModelPart();
    sonarPart = new SonarModelPart();
    wallsPart = new WallsModelPart();



  }

  public Model setProcessor(ModelProcessor processor) {
    this.processor = processor;
    this.processor.setModel(this);
    return this;
  }

  // triggers the processor(s) to start processing the sensordata and update
  // the grid
  public void process() {
    if (this.processor != null) {
      this.processor.process();
    }

    getSensorPart().markSensorValuesProcessed();
    getGridPart().markChangedSectorsProcessed();
    getSonarPart().setSweepComplete(false);
    getSonarPart().setSweepDataChanged(false);
  }

  public String toString() {
    return "This iz ze model!";
  }

  public BarcodeModelPart getBarcodePart() {
    return barcodePart;
  }

  public GapModelPart getGapPart() {
    return gapPart;
  }

  public GridModelPart getGridPart() {
    return gridPart;
  }

  public LightModelPart getLightPart() {
    return lightPart;
  }

  public MessageModelPart getMessagePart() {
    return messagePart;
  }

  public SensorModelPart getSensorPart() {
    return sensorPart;
  }

  public SonarModelPart getSonarPart() {
    return sonarPart;
  }

  public WallsModelPart getWallsPart() {
    return wallsPart;
  }
}
