package penoplatinum.ui.admin;

/**
 * RobotAdminClient
 *
 * Class that controls a robot through custom commands on the Queue channel.
 *
 * @author Penoplatinum
 */

import penoplatinum.gateway.Queue;

import java.security.*;


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
    else if( ! input.trim().equals("") ) {
      this.send(input);
    } else { throw new RuntimeException("Unknown command: " + args[0]); }
    return this;
  }
 
  private void sendForcedStart(){ this.sendMessage( "FORCESTART" ); }

  private void sendMessage(String message) {
    this.counter++;      
    message = this.counter + " " + message;
    String signature = md5(this.secret + " " + message);
    this.send(this.name + " PLATINUM_CMD "+ signature + " " + message);
  }
  
  private String md5(String message) {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      byte[] digest = md.digest(message.getBytes("UTF-8"));
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < digest.length; i++) {
        sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
      }
      return sb.toString();
    } catch(Exception e) {}
    return "";
  }

  private void send(String line){
    if( line == null ) { return; }
    this.queue.send( line + "\n" );
  }
}
