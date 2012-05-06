package penoplatinum.gateway;

/**
 * Custom Log4J appender to sens messages to Twitter
 * 
 * @author Team Platinum
 */

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import twitter4j.TwitterFactory;
import twitter4j.Twitter;


public class TwitterAppender extends AppenderSkeleton {

  private Twitter twitter = new TwitterFactory().getInstance();

  public boolean requiresLayout(){ return true; }

	protected void append(LoggingEvent event) {
	  try {
      this.twitter.updateStatus(this.layout.format(event));
    } catch (Exception e) {
      System.out.println(e.toString());
    }
	}

  public synchronized void close() {}
}
