package penoplatinum.navigator.action;

/**
 * TurnRightNavigatorActionTest
 * 
 * Tests TurnRightNavigatorAction class
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 
import static org.mockito.Mockito.*;

import penoplatinum.driver.ManhattanDriver;
import penoplatinum.model.part.GridModelPart;
import penoplatinum.grid.Agent;


public class TurnRightNavigatorActionTest extends TestCase {

  TurnRightNavigatorAction action;
  ManhattanDriver mockedDriver;
  GridModelPart mockedGridModelPart;
  Agent mockedAgent;

  public TurnRightNavigatorActionTest(String name) { 
    super(name);
  }

  public void testInstructDriver() {
    this.setup();
    this.action.instruct(this.mockedDriver);
    verify(this.mockedDriver).turnRight();
	}

  public void testComplete() {
    this.setup();
    this.action.complete();
    verify(this.mockedAgent).turnRight();
	}
	
	private void setup() {
	  this.mockedDriver        = mock(ManhattanDriver.class);
    this.mockedGridModelPart = mock(GridModelPart.class);
    this.mockedAgent         = mock(Agent.class);
    when(this.mockedGridModelPart.getMyAgent()).thenReturn(this.mockedAgent);
    this.action = new TurnRightNavigatorAction(this.mockedGridModelPart);
  }
}
