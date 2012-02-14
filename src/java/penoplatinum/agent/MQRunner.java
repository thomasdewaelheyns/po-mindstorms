package penoplatinum.agent;

/**
 * MQRunner
 * 
 * Demo for our MQ class
 *
 * Author: Team Platinum
 */

import java.io.IOException;

public class MQRunner {

  public static void main(String[] argv) throws java.io.IOException,
                                                java.lang.InterruptedException
  {
    MQ mq = new MQ() {
       void handleIncomingMessage(String sender, String message) {
         // handling the incoming messages ...
         System.out.println( "[" + sender + "] " + message );
       }
    }
    .setMyName(argv[0])
    .connectToMQServer("localhost")
    .follow("ghost-channel");

    // be nice ...
    mq.sendMessage( "Hello everybody." );

    // our actual event loop
    System.out.println( "Waiting for messages. To exit press CTRL+C" );
    while (true) {
      Thread.sleep(100);
      // mq.sendMessage( "Some message" );
    }
  }
}
