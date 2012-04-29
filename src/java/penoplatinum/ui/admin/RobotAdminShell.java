package penoplatinum.ui.admin;

/**
 * RobotAdminShell
 *
 * Simple shell implementation as a front-end to the RobotAdminClient
 *
 * @author Penoplatinum
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;


public class RobotAdminShell {
  
  private BufferedReader in;
  private BufferedWriter out;

  private RobotAdminClient client;

  private String prompt = "rash";

  private boolean acceptNextCommand;


  public RobotAdminShell useInput(BufferedReader in) {
    this.in = in;
    return this;
  }

  public RobotAdminShell useOutput(BufferedWriter out) {
    this.out = out;
    return this;
  }

  public RobotAdminShell useClient(RobotAdminClient client) {
    this.client = client;
    return this;
  }
  
  public RobotAdminShell setPrompt(String prompt) {
    this.prompt = prompt;
    return this;
  }

  public RobotAdminShell run() {
    this.acceptNextCommand = true;
    while(this.acceptNextCommand) {
      try {
        this.print( this.prompt + "> " );
        String command = this.in.readLine();
        this.handleCommand(command);
      } catch(Exception e) {
        this.println( "ERROR: Failed to read your input" );
        this.acceptNextCommand = false;
      }
    }
    return this;
  }
  
  private void print(String msg) {
    try {
      this.out.write(msg, 0, msg.length());
    } catch( Exception e ) {
      System.err.println( "Could not write to provided output." );
    }
  }

  private void println(String msg) {
    this.print(msg);
    try {
      this.out.newLine();
    } catch( Exception e ) {
      System.err.println( "Could not write to provided output." );
    }
  }

  // commands can be shell commands or commands that need to be handled by
  // the client
  private void handleCommand(String command) {
    String args[] = command.trim().split(" ");
    if( args[0].length() < 1 ) {
      return;
    } else if(args[0].equalsIgnoreCase("load")) { 
      this.handleLoadFile(args[1]);
    } else if(args[0].equalsIgnoreCase("exit"))  { 
      this.acceptNextCommand = false;
    } else {
      this.client.handleCommand(command);
    }
  }
  
  private void handleLoadFile(String fileName){
    BufferedReader in = null;
    try {
      in = new BufferedReader(new FileReader(fileName));
    } catch(Exception e){
      this.println("ERROR: could not load file: " + fileName);
      return;
    }
    try{
      while(in.ready()) {
        String line = in.readLine();
        this.handleCommand(line);
        this.println("sent: " + line);
      }
    } catch(Exception e){
      this.println("ERROR: could not read file content.");
    }
  }
}
