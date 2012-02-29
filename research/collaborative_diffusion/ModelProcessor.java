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
