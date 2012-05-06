package penoplatinum.navigator.action;

/**
 * ForwardNavigatorActionTest
 * 
 * Tests ForwardNavigatorAction class
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

public class ForwardNavigatorActionTest extends TestCase {

  ForwardNavigatorAction action;
  ManhattanDriver mockedDriver;
  GridModelPart mockedGridModelPart;
  Agent mockedAgent;
  private Grid mockedGrid;
  private Point mockedPosition;
  private Bearing mockedBearing;

  public ForwardNavigatorActionTest(String name) {
    super(name);
  }

  public void testInstructDriver() {
    this.setup();
    this.action.instruct(this.mockedDriver);
    verify(this.mockedDriver).move();
  }

  public void testComplete() {
    mockedPosition = new Point(2, 3);
    mockedBearing = Bearing.N;

    this.setup();
    this.action.complete();
    verify(this.mockedGrid).moveTo(mockedAgent, new Point(2, 2), mockedBearing);
    
    mockedPosition = new Point(2, 3);
    mockedBearing = Bearing.E;

    this.setup();
    this.action.complete();
    verify(this.mockedGrid).moveTo(mockedAgent, new Point(3, 3), mockedBearing);
    
    mockedPosition = new Point(2, 3);
    mockedBearing = Bearing.S;

    this.setup();
    this.action.complete();
    verify(this.mockedGrid).moveTo(mockedAgent, new Point(2, 4), mockedBearing);
    
    mockedPosition = new Point(2, 3);
    mockedBearing = Bearing.W;

    this.setup();
    this.action.complete();
    verify(this.mockedGrid).moveTo(mockedAgent, new Point(1, 3), mockedBearing);
  }

  private void setup() {
    this.mockedDriver = mock(ManhattanDriver.class);
    this.mockedGridModelPart = mock(GridModelPart.class);
    this.mockedAgent = mock(Agent.class);
    this.mockedGrid = mock(Grid.class);

    when(this.mockedGridModelPart.getMyGrid()).thenReturn(this.mockedGrid);
    when(this.mockedGridModelPart.getMyAgent()).thenReturn(this.mockedAgent);
    when(this.mockedGridModelPart.getMyPosition()).thenReturn(this.mockedPosition);
    when(this.mockedGridModelPart.getMyBearing()).thenReturn(this.mockedBearing);
    this.action = new ForwardNavigatorAction(this.mockedGridModelPart);
  }
}
