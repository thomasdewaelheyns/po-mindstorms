package penoplatinum.model;

/**
 * Model
 *
 * Central information store for everything concerning the robot and its
 * environment. It is divided into logical parts, to keep the implementation
 * packaged in small classes.
 *
 * @author Team Platinum
 */

import penoplatinum.model.part.ModelPart;

import penoplatinum.model.processor.ModelProcessor;

import penoplatinum.reporter.Reporter;


public interface Model {
  // adds a part to the Model
  public Model register(ModelPart part);
  // retrieves a registered ModelPart based on its assigned ID
  public ModelPart getPart(int id);

  // a Model can be processed by ModelProcessors
  public Model setProcessor(ModelProcessor processor);

  // sets a reporter that reports on the state of the robot
  public Model setReporter(Reporter reporter);
  // provides the reporter to other parts of the robot
  public Reporter getReporter();

  // after modifications have been made to the Model, this method kicks of
  // the internal workings of the Model to bring it up-to-date and triggers
  // a reporter to report on any interesting changes
  public Model refresh();
}
