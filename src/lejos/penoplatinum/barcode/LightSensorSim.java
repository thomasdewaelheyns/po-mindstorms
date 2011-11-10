package penoplatinum.barcode;

/**
 *
 * @author Thomas
 */
public class LightSensorSim implements ILightSensor {
    private byte[] codeList;
    int index;
    
    public LightSensorSim(byte[] list){
        codeList = list;
        index = 0;
    }
    
    @Override
    public byte readValue(){
        index++;
        return (byte) (codeList[index-1]+Math.random()*3);
    }

    public boolean isBrown(int val) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
