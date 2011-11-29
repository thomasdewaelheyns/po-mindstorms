/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.simulator;

/**
 *
 * @author Thomas
 */
public class HistogramModelProcessor extends ModelProcessor{

    public HistogramModelProcessor(){
        super();
    }
    
    public HistogramModelProcessor( ModelProcessor nextProcessor ) {
    super(nextProcessor);
    }
    
    
    @Override
    protected void work() {
        int value = this.model.getSensorValue(this.model.S4);
        this.model.getLightValueBuffer().insert(value);
    }
    
}
