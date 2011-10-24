package penoplatinum.barcode;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Thomas
 */
public class lightSensorSim implements iLightSensor {
    private byte[] codeList;
    int index;
    
    public lightSensorSim(byte[] list){
        codeList = list;
        index = 0;
    }
    
    @Override
    public byte readValue(){
        index++;
        return (byte) (codeList[index-1]+Math.random()*3);
    }
    
}
