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
import penoplatinum.grid.agent.Agent;
import penoplatinum.grid.Grid;
import penoplatinum.util.Bearing;
import penoplatinum.util.Point;

public class TurnRightNavigatorActionTest extends TestCase {

  TurnRightNavigatorAction action;
  ManhattanDriver mockedDriver;
  GridModelPart mockedGridModelPart;
  Agent mockedAgent;
  private Grid mockedGrid;
  private Point mockedPosition;
  private Bearing mockedBearing;

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
    verify(this.mockedGrid).moveTo(mockedAgent, mockedPosition, mockedBearing.rightFrom());
  }

  private void setup() {
    this.mockedDriver = mock(ManhattanDriver.class);
    this.mockedGridModelPart = mock(GridModelPart.class);
    this.mockedAgent = mock(Agent.class);
    this.mockedPosition = new Point(2,3);
    this.mockedBearing = Bearing.E;
    this.mockedGrid = mock(Grid.class);
    
    when(this.mockedGridModelPart.getMyGrid()).thenReturn(this.mockedGrid);
    when(this.mockedGridModelPart.getMyAgent()).thenReturn(this.mockedAgent);
    when(this.mockedGridModelPart.getMyPosition()).thenReturn(this.mockedPosition);
    when(this.mockedGridModelPart.getMyBearing()).thenReturn(this.mockedBearing);
    this.action = new TurnRightNavigatorAction(this.mockedGridModelPart);
  }
}
