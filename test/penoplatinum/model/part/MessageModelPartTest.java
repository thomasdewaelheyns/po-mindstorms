package penoplatinum.model.part;

/**
 * MessageModelPartTest
 * 
 * Tests MessageModelPart class
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Arrays;

import penoplatinum.protocol.ProtocolHandler;


public class MessageModelPartTest extends TestCase {

  private MessageModelPart part;
  private ProtocolHandler mockedProtocolHandler;
  private List<String> messages;


  public MessageModelPartTest(String name) { 
    super(name);
  }


  public void testInitialEmptyPostboxes() {
    this.createModelPart();
    assertEquals(0, this.part.getIncomingMessages().size());
    assertEquals(0, this.part.getOutgoingMessages().size());
  }

  public void testIncomingMessages() {
    this.createModelPart();
    this.receiveMessages();
    List<String> received = this.part.getIncomingMessages();
    for(int i=0; i<this.messages.size(); i++) {
      assertEquals(this.messages.get(i), received.get(i));
    }
  }

  public void testClearingOfInbox() {
    this.createModelPart();
    this.receiveMessages();
    this.part.getIncomingMessages();
    assertEquals(0, this.part.getIncomingMessages().size());
  }

  public void testcomingMessages() {
    this.createModelPart();
    this.sendMessages();
    List<String> sent = this.part.getOutgoingMessages();
    for(int i=0; i<this.messages.size(); i++) {
      assertEquals(this.messages.get(i), sent.get(i));
    }
  }

  public void testClearingOfOutbox() {
    this.createModelPart();
    this.sendMessages();
    this.part.getOutgoingMessages();
    assertEquals(0, this.part.getOutgoingMessages().size());
  }

  public void testProtocolHandler() {
    this.createModelPart();
    this.mockProtocolHandler();
    this.part.setProtocolHandler(this.mockedProtocolHandler);
    assertEquals(this.mockedProtocolHandler, this.part.getProtocolHandler());
  }
  
  // construction helpers
  
  private void createModelPart() {
    this.part = new MessageModelPart();
  }
  
  private void receiveMessages() {
    this.createMessages();
    for(String message : this.messages) {
      this.part.addIncomingMessage(message);
    }
  }

  private void sendMessages() {
    this.createMessages();
    for(String message : this.messages) {
      this.part.addOutgoingMessage(message);
    }
  }
  
  private void createMessages() {
    this.messages = Arrays.asList( new String[] { "test message 1", 
                                                  "test message 2",
                                                  "test message 3" } );
  }
  
  private void mockProtocolHandler() {
    this.mockedProtocolHandler = mock(ProtocolHandler.class);
  }
}
