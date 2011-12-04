package penoplatinum.simulator;

/**
 * ModelProcessor abstract base class
 * 
 * Abstract base class for ModelProcessors. It implements the Decorator-
 * pattern behavior, requiring extending concrete implementation to only
 * implement a single "work" method.
 * 
 * @author: Team Platinum
 */

abstract public class ModelProcessor {

  protected ModelProcessor nextProcessor;
  protected Model model;

  public ModelProcessor() {}

  public ModelProcessor( ModelProcessor nextProcessor ) {
    this.nextProcessor = nextProcessor;
  }

  public void setModel( Model model ) {
    this.model = model;
    if( this.nextProcessor != null ) {
      this.nextProcessor.setModel( model );
    }
  }

  public void process() {
    this.work();
    if( this.nextProcessor != null ) {
      this.nextProcessor.process();
    }
  }

  protected abstract void work();

}
