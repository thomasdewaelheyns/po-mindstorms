package penoplatinum.model.processor;

/**
 * ModelProcessorTest
 * 
 * Tests ModelProcessor base class
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 
import static org.mockito.Mockito.*;

import penoplatinum.model.Model;


public class ModelProcessorTest extends TestCase {

  private ModelProcessor processor,
                         mockedProcessor;
  private Model mockedModel;
  
  // flag to indicate the work() method has been called
  private boolean worked;


  public ModelProcessorTest(String name) { 
    super(name);
  }

  public void testSingleWorkingProcessor() {
    this.setupSingleModelProcessor();
    this.worked = false;
    this.processor.process();
    assertTrue(this.worked);
	}
	
	public void testPassingOnOfProcess() {
	  this.setupMultipleModelProcessors();
	  this.processor.process();
    verify(this.mockedProcessor).process();
  }

  public void testPassingOnOfModel() {
	  this.setupMultipleModelProcessors();
    this.mockModel();
	  this.processor.setModel(this.mockedModel);
    verify(this.mockedProcessor).setModel(this.mockedModel);
  }
	
	private void setupSingleModelProcessor() {
	  this.processor = new ModelProcessor() {
	    public void work() { worked = true; }
    };
  }
  
  private void setupMultipleModelProcessors() {
    this.mockedProcessor = mock(ModelProcessor.class);
    this.processor = new ModelProcessor(this.mockedProcessor) {
      public void work() {}
    };
  }
  
  private void mockModel() {
    this.mockedModel = mock(Model.class);
  }
  
}
