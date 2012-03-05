package penoplatinum.grid;

/**
 * GridProcessor abstract base class
 * 
 * Abstract base class for GridProcessors. It implements the Decorator-
 * pattern behavior, requiring extending concrete implementation to only
 * implement a single "work" method.
 * 
 * @author: Team Platinum
 */

abstract public class GridProcessor {

  protected GridProcessor nextProcessor;
  protected Grid grid;

  public GridProcessor() {}

  public GridProcessor( GridProcessor nextProcessor ) {
    this.nextProcessor = nextProcessor;
  }

  public void useGrid( Grid grid ) {
    this.grid = grid;
    if( this.nextProcessor != null ) {
      this.nextProcessor.useGrid( grid );
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
