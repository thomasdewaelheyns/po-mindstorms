package penoplatinum.fullTests;

/**
 * GhostModel
 * 
 * Implementation of Model, providing all needed by a typical Ghost
 * 
 * @author: Team Platinum
 */

import penoplatinum.model.Model;
import penoplatinum.model.processor.ModelProcessor;
import penoplatinum.reporter.Reporter;

import penoplatinum.model.part.ModelPart;
import penoplatinum.model.part.ModelPartRegistry;
import penoplatinum.model.part.SensorModelPart;
import penoplatinum.model.part.LightModelPart;


public class LineModel implements Model {

  // the modelparts we need
  private SensorModelPart  sensorPart;
  private LightModelPart   lightPart;

  // processors are a chain of decorators.
  private ModelProcessor   processor;


  // setup the parts of the LineModel
  public LineModel() {
    this.sensorPart  = new SensorModelPart();
    this.lightPart   = new LightModelPart();
  }

  // adds a part to the Model
  public Model register(ModelPart part) {
    throw new RuntimeException("GhostModel doesn't allow adding more parts.");
  }
  
  // retrieves a registered ModelPart based on its assigned ID
  public ModelPart getPart(int id) {
    switch(id) {
      case ModelPartRegistry.SENSOR_MODEL_PART  : return this.sensorPart;
      case ModelPartRegistry.LIGHT_MODEL_PART   : return this.lightPart;
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
    throw new RuntimeException("Not implemented");
  }

  // provides the reporter to other parts of the robot and ModelProcessors
  public Reporter getReporter() {
    throw new RuntimeException("Not implemented");
  }

  // after modifications have been made to the Model, this method kicks of
  // the internal workings of the Model to bring it up-to-date and triggers
  // a reporter to report on any interesting changes
  public Model refresh() {
    this.processor.process();
    return this;
  }

}
