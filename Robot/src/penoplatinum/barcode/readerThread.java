/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.barcode;

/**
 *
 * @author Thomas
 */
public class readerThread extends Thread {
    private BarcodeReader codeReader;
    boolean continueThread;
    public void readerThread(){
        this.codeReader = new BarcodeReader();
        this.continueThread = true;
    }
    
    public void run(){
       while(continueThread){
        int code = this.codeReader.read();
        System.out.print(""+code);
       }
    }
    
}
