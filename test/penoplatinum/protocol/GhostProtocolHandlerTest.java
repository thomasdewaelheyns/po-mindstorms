package penoplatinum.protocol;

/**
 * GhostProtocolHandlerTest
 * 
 * Tests GhostProtocolHandler class.
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 
import static org.mockito.Mockito.*;

import penoplatinum.Config;

import penoplatinum.gateway.GatewayClient;

import penoplatinum.grid.Sector;

import penoplatinum.util.Point;
import penoplatinum.util.Bearing;
import penoplatinum.util.BitwiseOperations;


public class GhostProtocolHandlerTest extends TestCase {

  private final static String NAME = "GhostProtocolHandlerTest";
  
  private GhostProtocolHandler protocolHandler;
  private GatewayClient        mockedGatewayClient;
  private ExternalEventHandler mockedEventHandler;


  public GhostProtocolHandlerTest(String name) { 
    super(name);
  }

  public void testHandleStart() {
    this.setup();
    this.protocolHandler.handleStart();
    verify(this.mockedGatewayClient).send("JOIN\n",
                                          Config.BT_GHOST_PROTOCOL);
  }
  
  public void testHandleEnterSector() {
    this.setup();
    Sector sector = this.mockSector(12, 34, false, true, null, true);
    this.protocolHandler.handleEnterSector(sector);
    verify(this.mockedGatewayClient).send(NAME + " POSITION 12,-34\n", 
                                          Config.BT_GHOST_PROTOCOL);
  }

  public void testHandleFoundSector() {
    this.setup();
    Sector sector = this.mockSector(12, 34, false, true, null, true);    
    this.protocolHandler.handleFoundSector(sector);
    verify(this.mockedGatewayClient).send(NAME + " DISCOVER 12,-34 0 1 2 1\n", 
                                          Config.BT_GHOST_PROTOCOL);
  }
  
  // TODO: complete
  
  public void testReceiveJoin() {
    this.setup();
    this.protocolHandler.receive("JOIN\n");
    verify(this.mockedEventHandler, never()).handleActivation();
  }
  
  private void setup() {
    this.protocolHandler     = this.createGhostProtocolHandler();
    this.mockedGatewayClient = this.mockGatewayClient();
    this.mockedEventHandler  = this.mockExternalEventHandler();
    protocolHandler.useGatewayClient(mockedGatewayClient);
    protocolHandler.useExternalEventHandler(mockedEventHandler);
  }
  
  private GhostProtocolHandler createGhostProtocolHandler() {
    return new GhostProtocolHandler() {
      public String getName() { return NAME; }
    };
  }
  
  private ExternalEventHandler mockExternalEventHandler() {
    return mock(ExternalEventHandler.class);
  }

  private GatewayClient mockGatewayClient() {
    return mock(GatewayClient.class);
  }
  
  private Sector mockSector(int left, int top,
                            Boolean n, Boolean e, Boolean s, Boolean w)
  {
    Sector mockedSector = mock(Sector.class);
    when(mockedSector.getPosition()).thenReturn(new Point(left, top));
    when(mockedSector.hasWall(Bearing.N)).thenReturn(n);
    when(mockedSector.hasWall(Bearing.E)).thenReturn(e);
    when(mockedSector.hasWall(Bearing.S)).thenReturn(s);
    when(mockedSector.hasWall(Bearing.W)).thenReturn(w);
    return mockedSector;
  }
}
