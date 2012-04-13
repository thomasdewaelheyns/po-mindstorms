package penoplatinum.model.processor;

import penoplatinum.model.Model;

/**
 * ModelProcessor abstract base class
 * 
 * Abstract base class for ModelProcessors. It implements the Decorator-
 * pattern behavior, requiring extending concrete implementation to only
 * implement a single "work" method.
 * 
 * Author: Team Platinum
 */

abstract public class ModelProcessor {

  protected ModelProcessor nextProcessor;
  protected Model model;

  // default constructor, no next ModelProcessor
  public ModelProcessor() {}

  // decorating constructor
  public ModelProcessor( ModelProcessor nextProcessor ) {
    this.nextProcessor = nextProcessor;
  }

  // sets the Model for this Processor and those following
  public void setModel( Model model ) {
    this.model = model;
    if( this.nextProcessor != null ) {
      this.nextProcessor.setModel( model );
    }
  }

  // triggers the processing chain downwards
  public void process() {
    this.work();
    if( this.nextProcessor != null ) {
      this.nextProcessor.process();
    }
  }

  // the actual functionality is proper to the concrete implementation
  protected abstract void work();
}
