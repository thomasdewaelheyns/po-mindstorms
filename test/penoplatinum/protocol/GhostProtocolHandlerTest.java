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
import penoplatinum.grid.agent.BarcodeAgent;
import penoplatinum.grid.Grid;

import penoplatinum.grid.agent.PacmanAgent;
import penoplatinum.util.Bearing;
import penoplatinum.util.Point;


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
    assertEquals("2.1", this.protocolHandler.getVersion());
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
    Sector sector = this.mockSector(12, 34, true, false, true, true, false, false, true, true);
    this.protocolHandler.handleEnterSector(sector);
    verify(this.mockedGatewayClient).send(NAME + " POSITION 12,-34\n", 
                                          Config.BT_GHOST_PROTOCOL);
    verifyNoMoreInteractions(this.mockedGatewayClient);
  }

  public void testHandleFoundSector() {
    this.setup();
    Sector sector = this.mockSector(12, 34, true, false, true, true, false, true, true, true);    
    this.protocolHandler.handleFoundSector(sector);
    verify(this.mockedGatewayClient).send(NAME + " DISCOVER 12,-34 0 1 2 1\n", 
                                          Config.BT_GHOST_PROTOCOL);
    verifyNoMoreInteractions(this.mockedGatewayClient);
  }
  
  public void testHandleFoundAgent() {
    this.setup();
    Grid grid = mock(Grid.class);
    Sector       sector = this.mockSector(12, 34, true, false, true, true, false, false, true, true);    
    BarcodeAgent agent  = this.mockBarcodeAgent(24);
    when(grid.getPositionOf(agent)).thenReturn(new Point(12,34));
    when(grid.getPositionOf(sector)).thenReturn(new Point(12,34));
    when(grid.getBearingOf(agent)).thenReturn(Bearing.E);
    this.protocolHandler.handleFoundAgent(grid, agent);
    verify(this.mockedGatewayClient).send(NAME + " BARCODEAT 12,-34 24 2\n",
                                          Config.BT_GHOST_PROTOCOL);
    verifyNoMoreInteractions(this.mockedGatewayClient);
  }

  public void handleFoundAgent() {
    this.setup();
    Grid grid = mock(Grid.class);
    Sector      sector = this.mockSector(12, 34, true, false, true, true, false, false, true, true);    
    PacmanAgent agent  = this.mockPacmanAgent();
    
    Point mockPosition = mock(Point.class);
    when(grid.getSectorAt(mockPosition)).thenReturn(sector);
    when(grid.getPositionOf(agent)).thenReturn(mockPosition);
    this.protocolHandler.handleFoundAgent(grid, agent);
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
  
  // receive other than NAME command before 4 joins
  public void testCommandBeforeActive(){
    this.setup();
    this.protocolHandler.receive("JOIN\n");
    this.protocolHandler.receive("JOIN\n");
    this.protocolHandler.receive("testRobot1 DISCOVER 1,1 1 1 0 0\n");
    verifyNoMoreInteractions(this.mockedEventHandler);
  }
  
  // receive other than NAME command a NAME
  public void testCommandBeforeActive2(){
    this.setup();
    this.protocolHandler.receive("JOIN\n");
    this.protocolHandler.receive("JOIN\n");
    this.protocolHandler.receive("JOIN\n");
    this.protocolHandler.receive("JOIN\n");
    verify(this.mockedEventHandler).handleActivation();
    this.protocolHandler.receive("testRobot1 DISCOVER 1,1 1 1 0 0\n");
    verifyNoMoreInteractions(this.mockedEventHandler);
  }
  
  // receive position
  public void testReceivePosition(){
    this.setup();
    this.protocolHandler.receive("JOIN\n");
    this.protocolHandler.receive("JOIN\n");
    this.protocolHandler.receive("JOIN\n");
    this.protocolHandler.receive("JOIN\n");
    verify(this.mockedEventHandler).handleActivation();
    this.protocolHandler.receive("testRobot1 NAME 2.1\n");
    verify(this.mockedEventHandler).handleNewAgent("testRobot1");
    this.protocolHandler.receive("testRobot2 NAME 2.1\n");
    verify(this.mockedEventHandler).handleNewAgent("testRobot2");
    this.protocolHandler.receive("testRobot3 NAME 2.1\n");
    verify(this.mockedEventHandler).handleNewAgent("testRobot3");
    
    this.protocolHandler.receive("testRobot3 POSITION 2,3\n");
    verify(this.mockedEventHandler, times(1)).handleAgentInfo("testRobot3", new Point(2,-3), 0, Bearing.N);
  }
  
  // receive discover
  public void testReceiveDiscover(){
    this.setup();
    this.protocolHandler.receive("JOIN\n");
    this.protocolHandler.receive("JOIN\n");
    this.protocolHandler.receive("JOIN\n");
    this.protocolHandler.receive("JOIN\n");
    verify(this.mockedEventHandler).handleActivation();
    this.protocolHandler.receive("testRobot1 NAME 2.1\n");
    verify(this.mockedEventHandler).handleNewAgent("testRobot1");
    this.protocolHandler.receive("testRobot2 NAME 2.1\n");
    verify(this.mockedEventHandler).handleNewAgent("testRobot2");
    this.protocolHandler.receive("testRobot3 NAME 2.1\n");
    verify(this.mockedEventHandler).handleNewAgent("testRobot3");
    
    this.protocolHandler.receive("testRobot1 DISCOVER 2,2 0 0 1 2\n");
    verify(this.mockedEventHandler, times(1)).handleSectorInfo("testRobot1", new Point(2,-2), true, false, true, false, true, true, false, false);
  }
  
  // receive barcodeat
  public void testReceiveBarcodeAt(){
    this.setup();
    this.protocolHandler.receive("JOIN\n");
    this.protocolHandler.receive("JOIN\n");
    this.protocolHandler.receive("JOIN\n");
    this.protocolHandler.receive("JOIN\n");
    verify(this.mockedEventHandler).handleActivation();
    this.protocolHandler.receive("testRobot1 NAME 2.1\n");
    verify(this.mockedEventHandler).handleNewAgent("testRobot1");
    this.protocolHandler.receive("testRobot2 NAME 2.1\n");
    verify(this.mockedEventHandler).handleNewAgent("testRobot2");
    this.protocolHandler.receive("testRobot3 NAME 2.1\n");
    verify(this.mockedEventHandler).handleNewAgent("testRobot3");
    
    this.protocolHandler.receive("testRobot2 BARCODEAT 2,2 50 1\n");
    verify(this.mockedEventHandler, times(1)).handleAgentInfo("testRobot2", new Point(2, -2), 50, Bearing.N);
  }
  
  // receive pacman
  public void testReceivePacman(){
    this.setup();
    this.protocolHandler.receive("JOIN\n");
    this.protocolHandler.receive("JOIN\n");
    this.protocolHandler.receive("JOIN\n");
    this.protocolHandler.receive("JOIN\n");
    verify(this.mockedEventHandler).handleActivation();
    this.protocolHandler.receive("testRobot1 NAME 2.1\n");
    verify(this.mockedEventHandler).handleNewAgent("testRobot1");
    this.protocolHandler.receive("testRobot2 NAME 2.1\n");
    verify(this.mockedEventHandler).handleNewAgent("testRobot2");
    this.protocolHandler.receive("testRobot3 NAME 2.1\n");
    verify(this.mockedEventHandler).handleNewAgent("testRobot3");
    
    this.protocolHandler.receive("testRobot2 PACMAN 2,2\n");
    verify(this.mockedEventHandler, times(1)).handleTargetInfo("testRobot2", new Point(2, -2));
  }
  
  public void testPlatinumCommand(){
    this.setup();
    this.protocolHandler.receive("JOIN\n");
    this.protocolHandler.receive("JOIN\n");
    // String signature = MD5.getHashString(Config.SECRET + " " + 1 + " FORCESTART");
    String signature = "blahblah";
    this.protocolHandler.receive(NAME + " PLATINUM_CMD "+ signature + " 1 FORCESTART\n");
    verify(this.mockedEventHandler).handleActivation();
    verify(this.mockedGatewayClient).send(NAME + " NAME " + this.protocolHandler.getVersion() + "\n",Config.BT_GHOST_PROTOCOL);
  }

  // constructors
  
  public void testJoinAfterActive(){
    this.setup();
    this.protocolHandler.receive("JOIN\n");
    this.protocolHandler.receive("JOIN\n");
    this.protocolHandler.receive("JOIN\n");
    this.protocolHandler.receive("JOIN\n");
    verify(this.mockedEventHandler).handleActivation();
    this.protocolHandler.receive("testRobot1 NAME 2.1\n");
    verify(this.mockedEventHandler).handleNewAgent("testRobot1");
    this.protocolHandler.receive("testRobot2 NAME 2.1\n");
    verify(this.mockedEventHandler).handleNewAgent("testRobot2");
    this.protocolHandler.receive("testRobot3 NAME 2.1\n");
    verify(this.mockedEventHandler).handleNewAgent("testRobot3");
    
    this.protocolHandler.receive("JOIN\n");
    
    this.protocolHandler.receive("testRobot2 RENAME 2.1\n");
    this.protocolHandler.receive("testRobot3 RENAME 2.1\n");
    verifyNoMoreInteractions(this.mockedEventHandler);
    this.protocolHandler.receive("testRobot4 NAME 2.1\n");
    verify(this.mockedEventHandler).handleNewAgent("testRobot4");
    verify(this.mockedEventHandler).handleRemoveAgent("testRobot1");
    this.protocolHandler.receive("testRobot1 DISCOVER 1,1 1 1 0 0\n");
    verifyNoMoreInteractions(this.mockedEventHandler);
  }
  
  public void testScenario(){
    this.setup();
    //We start bij joining
    this.protocolHandler.receive("JOIN\n");
    this.protocolHandler.receive("JOIN\n");
    this.protocolHandler.receive("JOIN\n");
    this.protocolHandler.receive("JOIN\n");
    verify(this.mockedEventHandler).handleActivation();
    verify(this.mockedGatewayClient).send(NAME + " NAME 2.1\n", Config.BT_GHOST_PROTOCOL);
    this.protocolHandler.receive("testRobot1 NAME 2.1\n");
    verify(this.mockedEventHandler).handleNewAgent("testRobot1");
    this.protocolHandler.receive("testRobot2 NAME 2.1\n");
    verify(this.mockedEventHandler).handleNewAgent("testRobot2");
    this.protocolHandler.receive("testRobot3 NAME 2.1\n");
    verify(this.mockedEventHandler).handleNewAgent("testRobot3");
    //We send and receive some communication
    
    this.protocolHandler.receive("testRobot2 POSITION 1,0\n");
    verify(this.mockedEventHandler, times(1)).handleAgentInfo("testRobot2", new Point(1,0), 0, Bearing.N);
    this.protocolHandler.receive("testRobot3 POSITION 1,0\n");
    verify(this.mockedEventHandler, times(1)).handleAgentInfo("testRobot3", new Point(1,0), 0, Bearing.N);
    this.protocolHandler.receive("testRobot1 POSITION 0,1\n");
    verify(this.mockedEventHandler, times(1)).handleAgentInfo("testRobot1", new Point(0,-1), 0, Bearing.N);
    
    this.protocolHandler.handleEnterSector(mockSector(1, 0, true, true, true, false, true, false, true, false));
    verify(this.mockedGatewayClient).send(NAME + " POSITION 1,0\n", Config.BT_GHOST_PROTOCOL);
    verifyNoMoreInteractions(this.mockedGatewayClient);
    
    this.protocolHandler.receive("testRobot1 DISCOVER 0,1 0 0 1 2\n");
    verify(this.mockedEventHandler, times(1)).handleSectorInfo("testRobot1", new Point(0,-1), true, false, true, false, true, true, false, false);
    this.protocolHandler.receive("testRobot2 DISCOVER 1,0 0 0 1 2\n");
    verify(this.mockedEventHandler, times(1)).handleSectorInfo("testRobot2", new Point(1,0), true, false, true, false, true, true, false, false);
    this.protocolHandler.receive("testRobot3 DISCOVER 1,0 0 0 1 2\n");
    verify(this.mockedEventHandler, times(1)).handleSectorInfo("testRobot3", new Point(1,0), true, false, true, false, true, true, false, false);
    
    this.protocolHandler.handleFoundSector(mockSector(1, 0, true, true, true, false, true, false, true, false));
    verify(this.mockedGatewayClient).send(NAME +" DISCOVER 1,0 1 0 0 0\n", Config.BT_GHOST_PROTOCOL);
    
    this.protocolHandler.receive("testRobot2 POSITION 2,0\n");
    verify(this.mockedEventHandler, times(1)).handleAgentInfo("testRobot2", new Point(2,0), 0, Bearing.N);
    this.protocolHandler.receive("testRobot3 POSITION 2,0\n");
    verify(this.mockedEventHandler, times(1)).handleAgentInfo("testRobot3", new Point(2,0), 0, Bearing.N);
    this.protocolHandler.receive("testRobot1 POSITION 0,2\n");
    verify(this.mockedEventHandler, times(1)).handleAgentInfo("testRobot1", new Point(0,-2), 0, Bearing.N);
    
    this.protocolHandler.handleEnterSector(mockSector(1, 1, true, true, true, false, true, false, true, false));
    verify(this.mockedGatewayClient).send(NAME + " POSITION 1,-1\n", Config.BT_GHOST_PROTOCOL);
    verifyNoMoreInteractions(this.mockedGatewayClient);
    
    // someone sends join when we are already active
    this.protocolHandler.receive("JOIN\n");
    verify(this.mockedGatewayClient).send(NAME+" RENAME 2.1\n", Config.BT_GHOST_PROTOCOL);
    this.protocolHandler.receive("testRobot2 RENAME 2.1\n");
    this.protocolHandler.receive("testRobot3 RENAME 2.1\n");
    verifyNoMoreInteractions(this.mockedEventHandler);
    this.protocolHandler.receive("testRobot4 NAME 2.1\n");
    verify(this.mockedEventHandler).handleNewAgent("testRobot4");
    verify(this.mockedEventHandler).handleRemoveAgent("testRobot1");
    
    this.protocolHandler.receive("testRobot2 DISCOVER 2,0 0 0 1 2\n");
    verify(this.mockedEventHandler, times(1)).handleSectorInfo("testRobot2", new Point(2,0), true, false, true, false, true, true, false, false);
    this.protocolHandler.receive("testRobot3 DISCOVER 2,0 0 0 1 2\n");
    verify(this.mockedEventHandler, times(1)).handleSectorInfo("testRobot3", new Point(2,0), true, false, true, false, true, true, false, false);
    
    this.protocolHandler.handleFoundSector(mockSector(1, 1, true, true, true, false, true, false, true, false));
    verify(this.mockedGatewayClient).send(NAME +" DISCOVER 1,-1 1 0 0 0\n", Config.BT_GHOST_PROTOCOL);
    
    this.protocolHandler.receive("testRobot2 POSITION 3,0\n");
    verify(this.mockedEventHandler, times(1)).handleAgentInfo("testRobot2", new Point(3,0), 0, Bearing.N);
    this.protocolHandler.receive("testRobot3 POSITION 3,0\n");
    verify(this.mockedEventHandler, times(1)).handleAgentInfo("testRobot3", new Point(3,0), 0, Bearing.N);
    this.protocolHandler.receive("testRobot4 POSITION 0,1\n");
    verify(this.mockedEventHandler, times(1)).handleAgentInfo("testRobot4", new Point(0,-1), 0, Bearing.N);
    
    this.protocolHandler.handleEnterSector(mockSector(1, 2, true, true, true, false, true, false, true, false));
    verify(this.mockedGatewayClient).send(NAME + " POSITION 1,-2\n", Config.BT_GHOST_PROTOCOL);
    verifyNoMoreInteractions(this.mockedGatewayClient);
    
    this.protocolHandler.receive("testRobot4 DISCOVER 0,1 0 0 1 2\n");
    verify(this.mockedEventHandler, times(1)).handleSectorInfo("testRobot4", new Point(0,-1), true, false, true, false, true, true, false, false);
    this.protocolHandler.receive("testRobot2 DISCOVER 3,0 0 0 1 2\n");
    verify(this.mockedEventHandler, times(1)).handleSectorInfo("testRobot2", new Point(3,0), true, false, true, false, true, true, false, false);
    this.protocolHandler.receive("testRobot3 DISCOVER 3,0 0 0 1 2\n");
    verify(this.mockedEventHandler, times(1)).handleSectorInfo("testRobot3", new Point(3,0), true, false, true, false, true, true, false, false);
    
    this.protocolHandler.handleFoundSector(mockSector(1, 2, true, true, true, false, true, false, true, false));
    verify(this.mockedGatewayClient).send(NAME +" DISCOVER 1,-2 1 0 0 0\n", Config.BT_GHOST_PROTOCOL);
    
    this.protocolHandler.receive("testRobot2 BARCODEAT 3,0 50 2\n");
    verify(this.mockedEventHandler, times(1)).handleAgentInfo("testRobot2", new Point(3, 0), 50, Bearing.E);
    
    this.protocolHandler.receive("testRobot3 PACMAN 4,0\n");
    verify(this.mockedEventHandler, times(1)).handleTargetInfo("testRobot3", new Point(4,0));
    
    this.protocolHandler.receive("testRobot3 CAPTURED\n");
    verify(this.mockedEventHandler).handleCaptured("testRobot3");
    
    //TODO: maybe SHOWMAP when done.
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
  
  private Sector mockSector(int left, int top, boolean knowsN, boolean n,
                            boolean knowsE, boolean e, boolean knowsS, boolean s, boolean knowsW, boolean w)
  {
    Sector mockedSector = mock(Sector.class);
    Grid mockedGrid = mock(Grid.class);
    when(mockedGrid.getPositionOf(mockedSector)).thenReturn(new Point(left, top));
    when(mockedSector.getGrid()).thenReturn(mockedGrid);
    when(mockedSector.hasWall(Bearing.N)).thenReturn(n);
    when(mockedSector.hasWall(Bearing.E)).thenReturn(e);
    when(mockedSector.hasWall(Bearing.S)).thenReturn(s);
    when(mockedSector.hasWall(Bearing.W)).thenReturn(w);
    when(mockedSector.hasNoWall(Bearing.N)).thenReturn(!n);
    when(mockedSector.hasNoWall(Bearing.E)).thenReturn(!e);
    when(mockedSector.hasNoWall(Bearing.S)).thenReturn(!s);
    when(mockedSector.hasNoWall(Bearing.W)).thenReturn(!w);
    
    when(mockedSector.knowsWall(Bearing.N)).thenReturn(knowsN);
    when(mockedSector.knowsWall(Bearing.E)).thenReturn(knowsE);
    when(mockedSector.knowsWall(Bearing.S)).thenReturn(knowsS);
    when(mockedSector.knowsWall(Bearing.W)).thenReturn(knowsW);
    
    return mockedSector;
  }

  private BarcodeAgent mockBarcodeAgent(int code) {
    BarcodeAgent mockedAgent = mock(BarcodeAgent.class);
    when(mockedAgent.getValue()).thenReturn(code);
    return mockedAgent;
  }

  private PacmanAgent mockPacmanAgent() {
    return mock(PacmanAgent.class);
  }
}
