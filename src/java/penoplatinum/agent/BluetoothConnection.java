package penoplatinum.agent;

/**
 * BluetoothConnection
 * 
 * Temporary local stub implementation.
 *
 * Author: Team Platinum
 */

import java.io.*;

public class BluetoothConnection {
  private String name;
  private BufferedReader br = 
    new BufferedReader(new InputStreamReader(System.in));
  private String nextMsg = "";

  public BluetoothConnection(String name) {
    this.name = name;
  }

  public Boolean hasNext() {
   try {
       this.nextMsg = this.name + "','" + this.br.readLine();
       return !this.nextMsg.equals( this.name + "','quit" );
    } catch (IOException ioe) {
       System.err.println("IO error reading from connection.");
    }
    return false;
  }
  
  public String getMessage() {
    return this.nextMsg;
  }
}
