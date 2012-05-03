package penoplatinum.ui.admin;

/**
 * RobotAdminClientTest
 * 
 * Tests RobotAdminClient class
 * 
 * @author: Team Platinum
 */

import java.security.*;

import junit.framework.*; 
import static org.mockito.Mockito.*;

import penoplatinum.gateway.Queue;


public class RobotAdminClientTest extends TestCase {

  private String name   = "Unit",
                 secret = "sikrit";

  private RobotAdminClient client;
  private Queue            mockedQueue;


  public RobotAdminClientTest(String name) { 
    super(name);
  }
  
  public void testHandleUnknownCommand() {
    this.setup();
    this.client.handleCommand( "beebabelooba" );
    verify(this.mockedQueue).send( "beebabelooba\n" );
  }

  public void testHandleEmpty() {
    this.setup();
    try {
      this.client.handleCommand( "" );
      fail( "Unknown command should throw Exception." );
    } catch( Exception e ) {}
  }

  public void testHandleNullCommand() {
    this.setup();
    try {
      this.client.handleCommand( null );
      fail( "Unknown command should throw Exception." );
    } catch( Exception e ) {}
  }
  
  public void testForceStart() {
    this.setup();
    this.client.handleCommand( "forcestart" );
    String signature = this.md5( this.secret + " 1 FORCESTART" );
    verify(this.mockedQueue)
      .send( this.name + " PLATINUM_CMD " + signature + " 1 FORCESTART\n" );
  }
  
  public void testIncrementingCounter() {
    this.setup();
    this.client.handleCommand( "forcestart" );
    String signature = this.md5( this.secret + " 1 FORCESTART" );
    verify(this.mockedQueue)
      .send( this.name + " PLATINUM_CMD " + signature + " 1 FORCESTART\n" );
    this.client.handleCommand( "forcestart" );
    signature = this.md5( this.secret + " 2 FORCESTART" );
    verify(this.mockedQueue)
      .send( this.name + " PLATINUM_CMD " + signature + " 2 FORCESTART\n" );
  }

  // construction helpers
  
  private void setup() {
    this.createClient();
    this.mockQueue();
    this.client.useQueue(this.mockedQueue);
    this.client.authenticateWith(this.name, this.secret);
  }
  
  private void createClient() {
    this.client = new RobotAdminClient();
  }
  
  private void mockQueue() {
    this.mockedQueue = mock(Queue.class);
  }
  
  private String md5(String message) {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      byte[] digest = md.digest(message.getBytes("UTF-8"));
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < digest.length; i++) {
        sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
      }
      return sb.toString();
    } catch(Exception e) {
      fail( "Could not generate MD5.");
    }
    return "";
  }
}
