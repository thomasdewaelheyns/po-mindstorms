/**
 * BaseRunner
 * 
 * Abstract baseclass for applications, offering unified CLI support and
 * connections to a MQ.
 *
 * Author: Team Platinum
 */

import org.apache.commons.cli.*;

import penoplatinum.Config;

import penoplatinum.gateway.Queue;
import penoplatinum.gateway.MQ;
import penoplatinum.gateway.MessageReceiver;

public class BaseRunner implements MessageReceiver {

  private static String DEFAULT_ROBOT;
  private static String DEFAULT_MQ_SERVER;
  private static String DEFAULT_MQ_CHANNEL;

  // cli options
  private Options options;

  protected String robotName, mqServerName, mqChannelName;
  protected String secret;

  protected Queue            queue;

  protected BaseRunner(String[] args, String supportedOptions) {
    this.loadConfig();
    this.prepareCommandLineOptions(supportedOptions);
    this.processCommandLine(args);
    this.setupMQ();
  }
  
  private void loadConfig() {
    Config.load("robot.properties");
    DEFAULT_ROBOT      = Config.ROBOT_NAME;
    DEFAULT_MQ_SERVER  = Config.MQ_SERVER;
    DEFAULT_MQ_CHANNEL = Config.GHOST_CHANNEL;
  }
  
  private void prepareCommandLineOptions(String supportedOptions) {
    this.options = new Options();
    // all applications support help
    this.options.addOption( "h", "help", false, "show this information." );
    if(supportedOptions.contains("d")) {
      this.options.addOption( "d", "debug", false, "run in debug-mode." );
    }
    if(supportedOptions.contains("r")) {
      this.options.addOption( "r", "robot", true, "admin robot <name>. " +
                                                "default=" + DEFAULT_ROBOT );
    }
    if(supportedOptions.contains("s")) {
      this.options.addOption( "s", "secret", true, "<secret> for robot. ");
    }
    if(supportedOptions.contains("m")) {
      this.options.addOption( "m", "mq",   true,  "connect to MQ <server>. " +
                                                  "default=" + DEFAULT_MQ_SERVER);
    }
    if(supportedOptions.contains("m")) {
      this.options.addOption( "j", "join", true,  "join <channel>. " +
                                                  "default=" + DEFAULT_MQ_CHANNEL);
    }
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
    formatter.printHelp( this.getClass().getName(), this.options );
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
                           .follow(this.mqChannelName)
                           .subscribe(this);
    } catch(Exception e) {
      System.err.println( "ERROR: " + e.getMessage() );
      System.exit(1);
    }
  }  
  
  public void receive(String message) {
    // this space is intentionally left blank
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
}
