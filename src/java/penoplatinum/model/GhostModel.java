package penoplatinum.model;

/**
 * GhostModel
 * 
 * Implementation of Model, providing all needed by a typical Ghost
 * 
 * @author: Team Platinum
 */

import java.util.ArrayList;
import java.util.List;


import penoplatinum.model.part.BarcodeModelPart;
import penoplatinum.reporter.Reporter;

import penoplatinum.model.part.GridModelPart;
import penoplatinum.model.part.LightModelPart;
import penoplatinum.model.part.ModelPart;
import penoplatinum.model.part.SensorModelPart;
import penoplatinum.model.part.SonarModelPart;
import penoplatinum.model.part.WallsModelPart;
import penoplatinum.model.processor.ModelProcessor;
import penoplatinum.reporter.Reporter;

import penoplatinum.model.part.ModelPart;
import penoplatinum.model.part.ModelPartRegistry;
import penoplatinum.model.part.SensorModelPart;
import penoplatinum.model.part.WallsModelPart;
import penoplatinum.model.part.BarcodeModelPart;
import penoplatinum.model.part.LightModelPart;
import penoplatinum.model.part.GridModelPart;


public class GhostModel implements Model {

  // the modelparts we need
  private SensorModelPart  sensorPart;
  private WallsModelPart   wallsPart;
  private BarcodeModelPart barcodePart;
  private LightModelPart   lightPart;
  private GridModelPart    gridPart;

  private List<ModelPart> parts = new ArrayList<ModelPart>();

  // processors are a chain of decorators.
  private ModelProcessor   processor;

  // a reporter reports on a Model
  private Reporter         reporter;


  // setup the parts of the GhostModel
  public GhostModel() {
    this.sensorPart  = new SensorModelPart();
    this.wallsPart   = new WallsModelPart();
    this.barcodePart = new BarcodeModelPart();
    this.lightPart   = new LightModelPart();
    this.gridPart    = new GridModelPart();
    // this.gapPart     = new GapModelPart();
    // this.messagePart = new MessageModelPart();
    // this.sonarPart   = new SonarModelPart();
  }

  // adds a part to the Model
  public Model register(ModelPart part) {
    throw new RuntimeException("GhostModel doesn't allow adding more parts.");
  }
  
  // retrieves a registered ModelPart based on its assigned ID
  public ModelPart getPart(int id) {
    switch(id) {
      case ModelPartRegistry.SENSOR_MODEL_PART  : return this.sensorPart;
      case ModelPartRegistry.WALLS_MODEL_PART   : return this.wallsPart;
      case ModelPartRegistry.BARCODE_MODEL_PART : return this.barcodePart;
      case ModelPartRegistry.LIGHT_MODEL_PART   : return this.lightPart;
      case ModelPartRegistry.GRID_MODEL_PART    : return this.gridPart;
      // case GAP_MODEL_PART     : return this.gapPart;
      // case MESSAGE_MODEL_PART : return this.gapPart;
      // case SONAR_MODEL_PART   : return this.sonarPart;
    }
    throw new RuntimeException( "Unknown model part: " + id );
  }

  // a Model can be processed by ModelProcessors
  public Model setProcessor(ModelProcessor processor) {
    this.processor = processor;
    this.processor.setModel(this);
    return this;
  }

  // sets a reporter that reports on the state of the robot
  public Model setReporter(Reporter reporter) {
    this.reporter = reporter;
    return this;
  }

  // provides the reporter to other parts of the robot and ModelProcessors
  public Reporter getReporter() {
    return this.reporter;
  }

  // after modifications have been made to the Model, this method kicks of
  // the internal workings of the Model to bring it up-to-date and triggers
  // a reporter to report on any interesting changes
  public Model refresh() {
    this.processor.process();
    this.reporter.reportModelUpdate();
    return this;
  }

  @Override
  public Model register(ModelPart part) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public ModelPart getPart(int id) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Model refresh() {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
