/**
 * MQSpy
 * 
 * Connects to an MQ channel and "spies"
 *
 * Author: Team Platinum
 */

import org.apache.commons.cli.*;

import penoplatinum.Config;

import penoplatinum.gateway.MQ;
import penoplatinum.gateway.MessageReceiver;


public class MQSpy implements MessageReceiver {
  private static String DEFAULT_MQ_SERVER;
  private static String DEFAULT_MQ_CHANNEL;

  // cli options
  private Options options;

  private String robotName, mqServerName, mqChannelName;
  private String secret;
  
  private MQ queue;


  public MQSpy(String[] args) {
    this.loadConfig();
    this.prepareCommandLineOptions();
    this.processCommandLine(args);
    this.setupMQ();
  }

  private void loadConfig() {
    Config.load("robot.properties");
    DEFAULT_MQ_SERVER  = Config.MQ_SERVER;
    DEFAULT_MQ_CHANNEL = Config.GHOST_CHANNEL;
  }
  
  public void connect() {
    this.queue.subscribe(this);
  }
  
  public void receive(String message) {
    System.out.println(message.trim());
  }
  
  private void prepareCommandLineOptions() {
    this.options = new Options();
    this.options.addOption( "h", "help", false, "show this information." );
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
    formatter.printHelp( "MQSpy", this.options );
  }

  private void setupMQ() {
    try {
      this.queue = new MQ().connectToMQServer(this.mqServerName)
                           .follow(this.mqChannelName);
    } catch(Exception e) {
      System.err.println( "ERROR: " + e.getMessage() );
      System.exit(1);
    }
  }    

  public static void main(String[] args) throws java.lang.InterruptedException {
    MQSpy spy = new MQSpy(args);

    System.out.println("Waiting for messages. To exit press CTRL+C");
    spy.connect();
    while (true) { Thread.sleep(100); }
  }
}
