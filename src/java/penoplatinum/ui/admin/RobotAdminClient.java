package penoplatinum.ui.admin;

/**
 * RobotAdminClient
 *
 * Class that controls a robot through custom commands on the Queue channel.
 *
 * @author Penoplatinum
 */

import penoplatinum.gateway.Queue;

import penoplatinum.util.MD5;


public class RobotAdminClient {
  
  private Queue     queue;
  
  private String name   = "";
  private String secret = "";

  private int    counter = 0;

  // accepts a fully configured queue
  public RobotAdminClient useQueue(Queue queue) {
    this.queue = queue;
    return this;
  }

  // sets the robot's own name and shared secret
  public RobotAdminClient authenticateWith(String name, String secret) {
    this.name   = name;
    this.secret = secret;
    return this;
  }

  // generic command handling method:
  // parses and handles an input string
  public RobotAdminClient handleCommand(String input) { 
    String[] args = input.split(" ");
    if(args[0].equalsIgnoreCase("forcestart")) { this.sendForcedStart(); }
    else { throw new RuntimeException("Unknown command."); }
    return this;
  }
    
  private void sendForcedStart(){ this.sendMessage( "FORCESTART" ); }

  private void sendMessage(String message) {
    this.counter++;      
    message = this.counter + " " + message;
    String signature = MD5.getHashString(this.secret + " " + message);
    this.send(this.name + " PLATINUM_CMD "+ signature + " " + message);
  }

  private void send(String line){
    if( line == null ) { return; }
    this.queue.send( line + "\n" );
  }
}
