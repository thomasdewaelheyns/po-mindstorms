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
import penoplatinum.grid.PacmanAgent;
import penoplatinum.grid.BarcodeAgent;

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

  // public interface
  
  public void testVersion() {
    this.setup();
    assertEquals("2.0", this.protocolHandler.getVersion());
  }

  public void testHandleStart() {
    this.setup();
    this.protocolHandler.handleStart();
    verify(this.mockedGatewayClient).send("JOIN\n",
                                          Config.BT_GHOST_PROTOCOL);
    verifyNoMoreInteractions(this.mockedGatewayClient);
  }
  
  public void testHandleEnterSector() {
    this.setup();
    Sector sector = this.mockSector(12, 34, false, true, null, true);
    this.protocolHandler.handleEnterSector(sector);
    verify(this.mockedGatewayClient).send(NAME + " POSITION 12,-34\n", 
                                          Config.BT_GHOST_PROTOCOL);
    verifyNoMoreInteractions(this.mockedGatewayClient);
  }

  public void testHandleFoundSector() {
    this.setup();
    Sector sector = this.mockSector(12, 34, false, true, null, true);    
    this.protocolHandler.handleFoundSector(sector);
    verify(this.mockedGatewayClient).send(NAME + " DISCOVER 12,-34 0 1 2 1\n", 
                                          Config.BT_GHOST_PROTOCOL);
    verifyNoMoreInteractions(this.mockedGatewayClient);
  }
  
  public void testHandleFoundAgent() {
    this.setup();
    Sector       sector = this.mockSector(12, 34, false, true, null, true);    
    BarcodeAgent agent  = this.mockBarcodeAgent(24, Bearing.E);
    this.protocolHandler.handleFoundAgent(sector, agent);
    verify(this.mockedGatewayClient).send(NAME + " BARCODEAT 12,-34 24 2\n",
                                          Config.BT_GHOST_PROTOCOL);
    verifyNoMoreInteractions(this.mockedGatewayClient);
  }

  public void handleFoundAgent() {
    this.setup();
    Sector      sector = this.mockSector(12, 34, false, true, null, true);    
    PacmanAgent agent  = this.mockPacmanAgent();
    this.protocolHandler.handleFoundAgent(sector, agent);
    verify(this.mockedGatewayClient).send(NAME + " PACMAN 12,-34\n",
                                          Config.BT_GHOST_PROTOCOL);
    verifyNoMoreInteractions(this.mockedGatewayClient);
  }

  
  // indirect interface:
  // all input comes through the receive() method
  // this causes actions that we can validate.
  // we now test all possible incoming input... also BAD ONE !!!
  
  // TODO: complete
  
  public void testReceiveJoin() {
    this.setup();
    this.protocolHandler.receive("JOIN\n");
    // not a single event should be triggered
    verifyZeroInteractions(this.mockedEventHandler);
    verifyNoMoreInteractions(this.mockedGatewayClient);
  }

  public void testBeginAfterFourJoins() {
    this.setup();
    this.protocolHandler.receive("JOIN\n");
    this.protocolHandler.receive("JOIN\n");
    this.protocolHandler.receive("JOIN\n");
    // not a single event should be triggered so far ...
    verifyZeroInteractions(this.mockedEventHandler);

    // 4th JOIN
    this.protocolHandler.receive("JOIN\n");
    // only one Activation should be triggered
    verify(this.mockedEventHandler).handleActivation();
    // and nothing else
    verifyNoMoreInteractions(this.mockedEventHandler);
    
    // we should have send our own name now
    verify(this.mockedGatewayClient).send(NAME + " NAME " + 
                                    this.protocolHandler.getVersion() + "\n",
                                    Config.BT_GHOST_PROTOCOL);
    // and nothing else
    verifyNoMoreInteractions(this.mockedGatewayClient);
  }

  public void testBeginAfterLessThanFourJoinsButWithName() {
    this.setup();
    this.protocolHandler.receive("JOIN\n");
    this.protocolHandler.receive("JOIN\n");
    // not a single event should be triggered so far ...
    verifyZeroInteractions(this.mockedEventHandler);

    // receive (early) NAME  -> we weren't first on the channel
    this.protocolHandler.receive("otherGhost NAME someversion\n");
    // only one Activation should be triggered
    verify(this.mockedEventHandler).handleActivation();
    // and we should handle a new Agent
    verify(this.mockedEventHandler).handleNewAgent("otherGhost");
    // and nothing else
    verifyNoMoreInteractions(this.mockedEventHandler);
    
    // we should have send our own name now
    verify(this.mockedGatewayClient).send(NAME + " NAME " + 
                                    this.protocolHandler.getVersion() + "\n",
                                    Config.BT_GHOST_PROTOCOL);
    // and nothing else
    verifyNoMoreInteractions(this.mockedGatewayClient);
  }
  
  // TODO: receive other than NAME command before 4 joins or name
  // TODO: receive 4 names before any activity
  // TODO: receive position
  // TODO: receive discover
  // TODO: receive barcodeat
  // TODO: receive pacman
  // TODO: complete entire protocol

  // constructors

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
    //TODO:
    //when(mockedSector.getPosition()).thenReturn(new Point(left, top));
    when(mockedSector.hasWall(Bearing.N)).thenReturn(n);
    when(mockedSector.hasWall(Bearing.E)).thenReturn(e);
    when(mockedSector.hasWall(Bearing.S)).thenReturn(s);
    when(mockedSector.hasWall(Bearing.W)).thenReturn(w);
    return mockedSector;
  }

  private BarcodeAgent mockBarcodeAgent(int code, Bearing direction) {
    BarcodeAgent mockedAgent = mock(BarcodeAgent.class);
    when(mockedAgent.getValue()).thenReturn(code);
    when(mockedAgent.getBearing()).thenReturn(direction);
    return mockedAgent;
  }

  private PacmanAgent mockPacmanAgent() {
    return mock(PacmanAgent.class);
  }
}
