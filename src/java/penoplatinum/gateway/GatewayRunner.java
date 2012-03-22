package penoplatinum.gateway;

/**
 * GatewayRunner
 * 
 * Processes CLI parameters, constructs an Gateway and starts it.
 *
 * Author: Team Platinum
 */

import org.apache.commons.cli.*;

public class GatewayRunner {

  public static void main(String[] args) {
    String defaultRobot = "platinum";
    
    Gateway gateway = new Gateway();

    Boolean setupComplete = false;
    
    // process command line arguments
    Options options = new Options();
    options.addOption( "h", "help", false, "show this helpful information." );
    options.addOption( "r", "robot", true, "connect to robot <name>. " +
                                           "default=" + defaultRobot );

    CommandLineParser parser = new GnuParser();
    try {
      CommandLine line = parser.parse( options, args );

      if( line.hasOption("help") ) { 
        GatewayRunner.showHelpFor(options);
      } else {
        gateway.connect( line.getOptionValue( "robot", defaultRobot ) );
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
