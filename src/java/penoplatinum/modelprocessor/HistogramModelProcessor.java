/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.modelprocessor;

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
//        System.out.println(value);
        this.model.getLightValueBuffer().insert(value);
    }
    
}
