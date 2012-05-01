package penoplatinum.fullTests;

import penoplatinum.grid.Agent;
import penoplatinum.grid.MovingAgent;
import penoplatinum.util.Bearing;
import penoplatinum.util.Color;
import penoplatinum.util.Position;
import static org.mockito.Mockito.*;

public class MockedAgent extends MovingAgent {

  private MockedGrid toChange;
  private int x = 1;
  private int y = 1;

  public MockedAgent(MockedGrid toChange, String name) {
    super(name);
    this.toChange = toChange;
    this.toChange.setPosition(x, y);
    when(toChange.getGridModelPart().getMyBearing()).thenReturn(getBearing());
  }

  @Override
  public Agent moveForward() {
    x = Position.moveLeft(getBearing(), x);
    y = Position.moveTop(getBearing(), y);
    toChange.setPosition(x, y);
    return this;
  }

  @Override
  public Agent turnLeft() {
    super.turnLeft();
    when(toChange.getGridModelPart().getMyBearing()).thenReturn(getBearing());
    return this;
  }

  @Override
  public Agent turnRight() {
    super.turnRight();
    when(toChange.getGridModelPart().getMyBearing()).thenReturn(getBearing());
    return this;
  }

  @Override
  public Color getColor() {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
