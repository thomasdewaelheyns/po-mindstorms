package penoplatinum.barcode;

/**
 *
 * This class contains data and some functions handling the repairation of barcodes
 * @author Thomas
 */
public class BarcodeData {
    public static byte[] barcodesRepair = getBarcodes();
    public static byte[] getBarcodes(){
        byte[] out = new byte[128];
        byte[] original = new byte[]{0,15,22,25,37,42,51,60,67,76,85,90,102,105,112,127};
        for(int i=0;i<original.length;i++){
            out[original[i]]=original[i];
            for(int j=1;j<128;j*=2){
                out[original[i]^j]=original[i];
            }
        }
        return out;
    }
    
    public static String[] codes = new String[]{"0000000","0001111","0010110","0011001","0100101","0101010","0110011","0111100","1000011","1001100","1010101","1011010","1100110","1101001","1110000","1111111"};
    public BarcodeData(){
        
    }
    
    public byte getBarcodesRepair(int index){
        return this.barcodesRepair[index];
    }
    
    public String getBarcodesString(int index){
        return this.codes[index];
    }
}
