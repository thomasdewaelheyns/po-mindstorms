package penoplatinum.model.processor;

public class HistogramModelProcessor extends ModelProcessor{

    public HistogramModelProcessor(){
        super();
    }
    
    public HistogramModelProcessor( ModelProcessor nextProcessor ) {
    super(nextProcessor);
    }
    
    
    @Override
    protected void work() {
        //int value = this.model.getSensorValue(this.model.S4);
        //this.model.getLightValueBuffer().insert(value);
        this.model.getLightValueBuffer().insert(model.getCurrentLightColor().getVal());
    }
}
