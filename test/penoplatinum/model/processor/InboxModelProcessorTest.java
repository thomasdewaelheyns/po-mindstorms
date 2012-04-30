package penoplatinum.model.processor;

/**
 * InboxModelProcessorTest
 * 
 * Tests InboxModelProcessor class
 * 
 * @author: Team Platinum
 */

import java.util.List;
import java.util.Arrays;

import junit.framework.*; 
import static org.mockito.Mockito.*;

import penoplatinum.model.Model;

import penoplatinum.model.part.ModelPartRegistry;
import penoplatinum.model.part.MessageModelPart;

import penoplatinum.protocol.ProtocolHandler;


public class InboxModelProcessorTest extends TestCase {

  private InboxModelProcessor processor;
  private Model mockedModel;

  private MessageModelPart mockedMessageModelPart;
  private ProtocolHandler  mockedProtocolHandler;


  public InboxModelProcessorTest(String name) { 
    super(name);
  }

  public void testNoMessages() {
    this.setup();
    when(this.mockedMessageModelPart.getIncomingMessages())
      .thenReturn(Arrays.asList(new String[] {}));
    this.processor.work();
    verifyZeroInteractions(this.mockedProtocolHandler);
  }

  public void testMessages() {
    this.setup();
    List<String> messages = this.generateMessages();
    when(this.mockedMessageModelPart.getIncomingMessages())
      .thenReturn(messages);
    this.processor.work();
    verify(this.mockedProtocolHandler).receive(messages.get(0));
    verify(this.mockedProtocolHandler).receive(messages.get(1));
    verify(this.mockedProtocolHandler).receive(messages.get(2));
  }
  
  // construction helpers
  
  private void setup() {
    this.createProcessor();
    this.mockModel();
    this.processor.setModel(this.mockedModel);
  }

  private void createProcessor() {
    this.processor = new InboxModelProcessor();
  }
  
  private void mockModel() {
    this.mockedModel = mock(Model.class);
    // message
    this.mockedMessageModelPart = mock(MessageModelPart.class);
    when(this.mockedModel.getPart(ModelPartRegistry.MESSAGE_MODEL_PART))
      .thenReturn(this.mockedMessageModelPart);
    // protocolHandler
    this.mockedProtocolHandler = mock(ProtocolHandler.class);
    when(this.mockedMessageModelPart.getProtocolHandler())
      .thenReturn(this.mockedProtocolHandler);
  }
  
  private List<String> generateMessages() {
    return Arrays.asList( new String[] { "test message 1", 
                                         "test message 2",
                                         "test message 3" } );
    
  }
}