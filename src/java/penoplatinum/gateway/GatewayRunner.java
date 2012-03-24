package penoplatinum.gateway;

/**
 * GatewayRunner
 * 
 * Processes CLI parameters, constructs an Gateway and starts it.
 *
 * Author: Team Platinum
 */

import org.apache.commons.cli.*;

import penoplatinum.Config;

public class GatewayRunner {
  private static final String DEFAULT_ROBOT = "platinum";
  private String robotName;
  
  // de default GatewayRunner has a Bluetooth and a real MQ connection.
  private BluetoothConnection connection = new BluetoothConnection();
  private MQ queue;
  // the actual Gateway
  private Gateway gateway;
  // cli options
  private Options options;


  private GatewayRunner(String[] args) {
    this.prepareCommandLineOptions();
    this.processCommandLine(args);
    this.setupBluetooth();
    this.setupMQ();
    this.setupGateway();
  }
  
  private void prepareCommandLineOptions() {
    this.options = new Options();
    this.options.addOption( "h", "help", false, "show this information." );
    this.options.addOption( "r", "robot", true, "connect to robot <name>. " +
                                                "default=" + DEFAULT_ROBOT );
  }

  private void processCommandLine(String[] args) {
    CommandLineParser parser = new GnuParser();
    try {
      CommandLine line = parser.parse( options, args );
      if( line.hasOption("help") ) { 
        this.showHelp();
      } else {
        this.robotName = line.getOptionValue("robot", DEFAULT_ROBOT);
      }
    } catch( ParseException e ) {
      System.err.println( "ERROR:" + e.getMessage() );
      this.showHelp();
      System.exit(1);
    }
  }
  
  private void showHelp() {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp( "GatewayRunner", this.options );    
  }
  
  private void setupBluetooth() {
    this.connection.setName(this.robotName);
  }
  
  private void setupMQ() {
    try {
      this.queue = new MQ().connectToMQServer(Config.MQ_SERVER)
                        .follow(Config.GHOST_CHANNEL);
    } catch(Exception e) {
      System.err.println( "ERROR: " + e.getMessage() );
      System.exit(1);
    }
  }
  
  private void setupGateway() {
    this.gateway = new Gateway().useConnection(this.connection)
                                .useQueue(this.queue);
  }
  
  public void start() {
    this.gateway.start();
  }
  
  public static void main(String[] args) {
    new GatewayRunner(args).start();
  }
}
