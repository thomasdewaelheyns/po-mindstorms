package penoplatinum.agent;

/**
 * AgentRunner
 * 
 * Processes CLI parameters, constructs an Agent and starts it.
 *
 * Author: Team Platinum
 */

import org.apache.commons.cli.*;

public class AgentRunner {

  public static void main(String[] args) {
    String defaultRobot = "platinum";
    
    Agent agent = new Agent();

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
        AgentRunner.showHelpFor(options);
      } else {
        agent.connect( line.getOptionValue( "robot", defaultRobot ) );
        setupComplete = true;
      }
    } catch( ParseException exp ) {
      System.err.println( "ERROR:" + exp.getMessage() );
      AgentRunner.showHelpFor(options);
    }

    if( setupComplete ) { agent.start(); }
  }

  public static void showHelpFor(Options options) {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp( "AgentRunner", options );    
  }

}
