/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.pcclients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import penoplatinum.Config;
import penoplatinum.gateway.MQ;

/**
 *
 * @author Penoplatinum
 */
public class RobotAdminClient {
  
  private MQ rabbit = new MQ();
  private String secretString;
  private String name;
  private int counter;
  
  public RobotAdminClient(String mqServer, String channel, String key, String name) throws IOException, InterruptedException{
    secretString = key;
    this.name = name;
    counter = 0;
    rabbit.connectToMQServer(mqServer).follow(channel);
  }
   
  public static void main(String[] args){
    if(args.length<2){
      System.out.println("Insufficient arguments.");
      System.exit(0);
    }
    String name =args[0];
    RobotAdminClient client = null;
    try{
      client= new RobotAdminClient(Config.MQ_SERVER, Config.GHOST_CHANNEL, args[1], name);
    }
    catch(Exception e){
      System.out.println("Could not connect to the MQ server.");
      System.exit(0);
    }
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    boolean cont = true;
    String input = null;
    while(cont){
      System.out.print(name+">");
      try {
        input = br.readLine();
      } catch (IOException ex) {
        System.exit(0);
      }
      if(input.equalsIgnoreCase("EXIT")){
        cont = false;
      }
      else{
        handleCommand(input, client);
      }
    }
  }
  
  private static void handleCommand(String input, RobotAdminClient client){
    String[] argumenten = input.split(" ");
    if(argumenten[0].equalsIgnoreCase("FORCESTART")){
      handleForceStart();
    }
    else if(argumenten[0].equalsIgnoreCase("load")){
      handleLoadFile()
    }
  }
    
    private void sendForcedStart(){
      counter++;
      
    }
  
}
