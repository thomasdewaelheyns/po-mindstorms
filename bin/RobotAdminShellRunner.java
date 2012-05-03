/**
 * RobotAdminShellRunner
 * 
 * Processes CLI parameters, constructs a RobotAdminShell and starts it.
 *
 * Author: Team Platinum
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

import org.apache.commons.cli.*;

import penoplatinum.Config;

import penoplatinum.gateway.Queue;
import penoplatinum.gateway.MQ;
import penoplatinum.gateway.MessageReceiver;

import penoplatinum.ui.admin.RobotAdminClient;
import penoplatinum.ui.admin.RobotAdminShell;

public class RobotAdminShellRunner {

  private static String DEFAULT_ROBOT;
  private static String DEFAULT_MQ_SERVER;
  private static String DEFAULT_MQ_CHANNEL;

  // cli options
  private Options options;

  private String robotName, mqServerName, mqChannelName;
  private String secret;

  private Queue            queue;
  private RobotAdminClient client;
  private RobotAdminShell  shell;

  private RobotAdminShellRunner(String[] args) {
    this.loadConfig();
    this.prepareCommandLineOptions();
    this.processCommandLine(args);
    this.setupMQ();
    this.setupClient();
    this.setupShell();
  }
  
  private void loadConfig() {
    Config.load("robot.properties");
    DEFAULT_ROBOT      = Config.ROBOT_NAME;
    DEFAULT_MQ_SERVER  = Config.MQ_SERVER;
    DEFAULT_MQ_CHANNEL = Config.GHOST_CHANNEL;
  }
  
  private void prepareCommandLineOptions() {
    this.options = new Options();
    this.options.addOption( "h", "help", false, "show this information." );
    this.options.addOption( "d", "debug", false, "run in debug-mode." );
    this.options.addOption( "r", "robot", true, "admin robot <name>. " +
                                                "default=" + DEFAULT_ROBOT );
    this.options.addOption( "s", "secret", true, "<secret> for robot. ");
    this.options.addOption( "m", "mq",   true,  "connect to MQ <server>. " +
                                                "default=" + DEFAULT_MQ_SERVER);
    this.options.addOption( "j", "join", true,  "join <channel>. " +
                                                "default=" + DEFAULT_MQ_CHANNEL);
  }

  private void processCommandLine(String[] args) {
    CommandLineParser parser = new GnuParser();
    try {
      CommandLine line = parser.parse( options, args );
      if( line.hasOption("help") ) { 
        this.showHelp();
        System.exit(0);
      } else {
        this.robotName     = line.getOptionValue("robot", DEFAULT_ROBOT);
        if( ! line.hasOption("debug") ) {
          this.mqServerName  = line.getOptionValue("mq", DEFAULT_MQ_SERVER);
          this.mqChannelName = line.getOptionValue("channel", DEFAULT_MQ_CHANNEL);
        }
        if( line.hasOption("secret") ) {
          this.secret = line.getOptionValue("secret");
        }
      }
    } catch( ParseException e ) {
      System.err.println( "ERROR:" + e.getMessage() );
      this.showHelp();
      System.exit(1);
    }
  }
  
  private void showHelp() {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp( "RobotAdminShellRunner", this.options );
  }


  private void setupMQ() {
    if( this.mqServerName != null && this.mqChannelName != null ) {
      this.setupRemoteMQ();
    } else {
      this.setupDebugMQ();
    }
  }
  
  private void setupRemoteMQ() {
    try {
      this.queue = new MQ().connectToMQServer(this.mqServerName)
                           .follow(this.mqChannelName);
    } catch(Exception e) {
      System.err.println( "ERROR: " + e.getMessage() );
      System.exit(1);
    }
  }  

  private void setupDebugMQ() {
    System.out.println( "--- Setting up debugging MQ using stdout." );
    this.queue = new Queue() {
       public Queue subscribe(MessageReceiver receiver) { return this; }
       public Queue send(String message) {
         System.out.print( "MQ: " + message );
         return this;
       }
    };
  }

  private void setupClient() {
    this.client = new RobotAdminClient()
      .useQueue(this.queue);
  }

  private void setupShell() {
    this.shell = new RobotAdminShell()
      .useInput(new BufferedReader(new InputStreamReader(System.in)))
      .useOutput(new BufferedWriter(new OutputStreamWriter(System.out)))
      .useClient(this.client);
    if( this.secret != null ) {
      this.client.authenticateWith(this.robotName, this.secret);
      this.shell.setPrompt(this.robotName);
    }
  }
  
  public void start() {
    System.out.println("--- Starting shell.");
    this.shell.run();
  }
  
  public static void main(String[] args) {
    new RobotAdminShellRunner(args).start();
  }
}
