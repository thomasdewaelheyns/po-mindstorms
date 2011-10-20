package penoplatinum.barcode;
import java.io.*;
import lejos.nxt.*;

public class FileWriteTest {
  public static void main(String[] args) { 
    FileOutputStream out = null; // declare outside the try block
    File data = new File("log.dat");

    try {
      out = new FileOutputStream(data, true);
    } catch(Exception e) {
        System.out.print("filestream");
    }
    DataOutputStream dataOut = new DataOutputStream(out);
    
    try {
        dataOut.writeChars("test");
      }
   catch(Exception e){
       System.out.print(e.getClass());
   }
      
    try{
      out.close(); // flush the buffer and write the file
    } catch (Exception e) {
        System.out.print("close");
    }
    Sound.beep();
    Button.waitForPress();
  }
}
