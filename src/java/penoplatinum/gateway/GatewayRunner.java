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

  public static void main(String[] args) {
    Boolean setupComplete = false;
    final BluetoothConnection connection = new BluetoothConnection();
    Gateway gateway = new Gateway();
    
    MQ mq = new MQ() {
      protected void handleIncomingMessage(String message) {
        connection.send(message, GatewayConfig.MQRelayPacket);
      }
    };
    
    gateway.useQueue(mq);

    try {
      mq.connectToMQServer(Config.MQ_SERVER)
        .follow(Config.GHOST_CHANNEL);
    } catch (Exception ex) {
      System.err.println("Could not connect to MQ.");
    }
    
    // process command line arguments
    Options options = new Options();
    options.addOption( "h", "help", false, "show this helpful information." );
    options.addOption( "r", "robot", true, "connect to robot <name>. " +
                                           "default=" + DEFAULT_ROBOT );

    CommandLineParser parser = new GnuParser();
    try {
      CommandLine line = parser.parse( options, args );

      if( line.hasOption("help") ) { 
        GatewayRunner.showHelpFor(options);
      } else {
        connection.setName(line.getOptionValue("robot", DEFAULT_ROBOT));
        gateway.connect(connection);
        setupComplete = true;
      }
    } catch( ParseException exp ) {
      System.err.println( "ERROR:" + exp.getMessage() );
      GatewayRunner.showHelpFor(options);
    }

    if( setupComplete ) { gateway.start(); }
  }

  public static void showHelpFor(Options options) {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp( "GatewayRunner", options );    
  }
}
