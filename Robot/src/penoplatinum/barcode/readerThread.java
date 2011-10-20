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
    BarcodeReader codeReader;
    boolean continueThread;
    public readerThread(){
        this.codeReader = new BarcodeReader();
        this.continueThread = true;
    }
    
    public void run(){
        while(continueThread){
            System.out.print(""+this.codeReader.read());
       }
    }
    
}
