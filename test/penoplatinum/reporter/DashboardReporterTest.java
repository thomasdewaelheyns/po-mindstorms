/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.reporter;

import junit.framework.TestCase;
import static org.mockito.Mockito.*;
import penoplatinum.Config;
import penoplatinum.gateway.GatewayClient;
import penoplatinum.grid.Grid;
import penoplatinum.grid.Sector;
import penoplatinum.grid.agent.Agent;
import penoplatinum.model.GhostModel;
import penoplatinum.model.part.*;
import penoplatinum.robot.GhostRobot;
import penoplatinum.util.Bearing;
import penoplatinum.util.LightColor;
import penoplatinum.util.Point;

/**
 *
 * @author Penoplatinum
 */
public class DashboardReporterTest extends TestCase{
  
  public DashboardReporterTest(String name) {
    super(name);
  }
  
  public void testReportModelUpdate(){
    DashboardReporter reporter = new DashboardReporter();
    GatewayClient client = mockGatewayClient();
    GhostRobot robot = mockGhostRobot();
    Sector sector = mockSector(1, 1, true, true, false, false, true, false, false, false);
    GhostModel ghostModel = mockGhostModel();
    GridModelPart gridPart = mockGridModelPart();
    SensorModelPart sensorPart = mockSensorModelPart();
    LightModelPart lightPart = mockLightModelPart();
    BarcodeModelPart barcodePart = mockBarcodeModelPart();
    Agent agent = mockAgent();
    Point point = new Point(1,1);
    Grid grid = mockGrid();
    when(robot.getModel()).thenReturn(ghostModel);
    when(ghostModel.getPart(ModelPartRegistry.GRID_MODEL_PART)).thenReturn(gridPart);
    when(ghostModel.getPart(ModelPartRegistry.SENSOR_MODEL_PART)).thenReturn(sensorPart);
    when(ghostModel.getPart(ModelPartRegistry.LIGHT_MODEL_PART)).thenReturn(lightPart);
    when(ghostModel.getPart(ModelPartRegistry.BARCODE_MODEL_PART)).thenReturn(barcodePart);
    when(sensorPart.getLightSensorValue()).thenReturn(90);
    when(lightPart.getCurrentLightColor()).thenReturn(LightColor.WHITE);
    when(lightPart.getAverageLightValue()).thenReturn(70f);
    when(barcodePart.getLastBarcodeValue()).thenReturn(8);
    when(sensorPart.getSonarAngle()).thenReturn(30);
    when(sensorPart.getSonarDistance()).thenReturn(40);
    when(sensorPart.getIRValue(0)).thenReturn(0);
    when(sensorPart.getIRValue(1)).thenReturn(20);
    when(sensorPart.getIRValue(2)).thenReturn(200);
    when(sensorPart.getIRValue(3)).thenReturn(20);
    when(sensorPart.getIRValue(4)).thenReturn(0);
    when(grid.getPositionOf(agent)).thenReturn(point);
    when(gridPart.getMyGrid()).thenReturn(grid);
    when(gridPart.getMyAgent()).thenReturn(agent);
    when(grid.getPositionOf(sector)).thenReturn(point);
    when(grid.getSectorAt(point)).thenReturn(sector);
    
    Sector neighbourN = mockSector(1, 0, true, false, true, false, true, true, true, true);
    Sector neighbourE = mockSector(2, 1, true, false, true, false, true, true, true, true);
    Sector neighbourS = mockSector(1, 2, true, false, true, false, true, true, true, true);
    Sector neighbourW = mockSector(0, 1, true, false, true, false, true, true, true, true);
    
    when(sector.getNeighbour(Bearing.N)).thenReturn(neighbourN);
    when(sector.getNeighbour(Bearing.E)).thenReturn(neighbourE);
    when(sector.getNeighbour(Bearing.S)).thenReturn(neighbourS);
    when(sector.getNeighbour(Bearing.W)).thenReturn(neighbourW);
    
    when(neighbourN.getValue()).thenReturn(20);
    when(neighbourE.getValue()).thenReturn(30);
    when(neighbourS.getValue()).thenReturn(10);
    when(neighbourW.getValue()).thenReturn(50);
    
    reporter.useGatewayClient(client);
    reporter.setRobot(robot);
    reporter.reportModelUpdate();
    verify(client).send("\"Angie\",90,\"WHITE\",70,8,30,40,0,20,200,20,0,-1,8,20,30,10,50,\"\",\"\",\"\",\"\",\"\",\"\",0", Config.BT_MODEL);
  }
  
  public void testReportSectorUpdate(){
    DashboardReporter reporter = new DashboardReporter();
    GatewayClient client = mockGatewayClient();
    GhostRobot robot = mockGhostRobot();
    Sector sector = mockSector(1, 1, true, true, false, false, true, false, false, false);
    GhostModel ghostModel = mockGhostModel();
    GridModelPart gridPart = mockGridModelPart();
    Grid grid = mockGrid();
    when(robot.getModel()).thenReturn(ghostModel);
    when(ghostModel.getPart(ModelPartRegistry.GRID_MODEL_PART)).thenReturn(gridPart);
    when(gridPart.getMyGrid()).thenReturn(grid);
    when(grid.getPositionOf(sector)).thenReturn(new Point(1,1));
    
    reporter.useGatewayClient(client);
    reporter.setRobot(robot);
    reporter.reportSectorUpdate(sector);
    verify(client).send("\"Angie\",\"myGrid\",1,1,8", Config.BT_WALLS);
  }
  
  public void testReportAgentUpdate(){
    DashboardReporter reporter = new DashboardReporter();
    GatewayClient client = mockGatewayClient();
    GhostRobot robot = mockGhostRobot();
    GhostModel ghostModel = mockGhostModel();
    GridModelPart gridPart = mockGridModelPart();
    Grid grid = mockGrid();
    Agent agent = mockAgent();
    Sector sector = mockSector(1, 1, true, true, false, false, true, false, false, false);
    Point point = new Point(1,1);
    when(robot.getModel()).thenReturn(ghostModel);
    when(ghostModel.getPart(ModelPartRegistry.GRID_MODEL_PART)).thenReturn(gridPart);
    when(gridPart.getMyGrid()).thenReturn(grid);
    when(grid.getPositionOf(agent)).thenReturn(point);
    when(agent.getName()).thenReturn("Megatron");
    when(grid.getBearingOf(agent)).thenReturn(Bearing.N);
    when(grid.getSectorAt(point)).thenReturn(sector);
    //TODO hier verder
    reporter.useGatewayClient(client);
    reporter.setRobot(robot);
    
    reporter.reportAgentUpdate(agent);
    verify(client).send("\"Angie\",\"myGrid\",\"Megatron\",1,1,1,\"white\"", Config.BT_AGENTS);
  }
  
  private GatewayClient mockGatewayClient(){
    GatewayClient mockedGateway = mock(GatewayClient.class);
    return mockedGateway;
  }
  
  private GhostRobot mockGhostRobot(){
    GhostRobot mockedGhostRobot = mock(GhostRobot.class);
    when(mockedGhostRobot.getName()).thenReturn("Angie");
    return mockedGhostRobot;
  }
  
  private GhostModel mockGhostModel(){
    GhostModel mockedGhostModel = mock(GhostModel.class);
    return mockedGhostModel;
  }
  
  private BarcodeModelPart mockBarcodeModelPart(){
    BarcodeModelPart barcode = mock(BarcodeModelPart.class);
    return barcode;
  }
  
  private GridModelPart mockGridModelPart(){
    GridModelPart gridPart = mock(GridModelPart.class);
    
    return gridPart;
  }
  
  private Grid mockGrid(){
    Grid grid = mock(Grid.class);
    return grid;
  }
  
  private Agent mockAgent(){
    Agent agent = mock(Agent.class);
    return agent;
  }
  
  private Sector mockSector(int left, int top, boolean knowsN, boolean n,
                            boolean knowsE, boolean e, boolean knowsS, boolean s, boolean knowsW, boolean w)
  {
    Sector mockedSector = mock(Sector.class);
    Grid mockedGrid = mock(Grid.class);
    when(mockedGrid.getPositionOf(mockedSector)).thenReturn(new Point(left, top));
    when(mockedSector.getGrid()).thenReturn(mockedGrid);
    when(mockedSector.getValue()).thenReturn(0);
    
    when(mockedSector.hasNoWall(Bearing.N)).thenReturn(!n);
    when(mockedSector.hasNoWall(Bearing.E)).thenReturn(!e);
    when(mockedSector.hasNoWall(Bearing.S)).thenReturn(!s);
    when(mockedSector.hasNoWall(Bearing.W)).thenReturn(!w);
    
    when(mockedSector.hasWall(Bearing.N)).thenReturn(n);
    when(mockedSector.hasWall(Bearing.E)).thenReturn(e);
    when(mockedSector.hasWall(Bearing.S)).thenReturn(s);
    when(mockedSector.hasWall(Bearing.W)).thenReturn(w);
    
    when(mockedSector.knowsWall(Bearing.N)).thenReturn(knowsN);
    when(mockedSector.knowsWall(Bearing.E)).thenReturn(knowsE);
    when(mockedSector.knowsWall(Bearing.S)).thenReturn(knowsS);
    when(mockedSector.knowsWall(Bearing.W)).thenReturn(knowsW);
    
    return mockedSector;
  }
  
  private SensorModelPart mockSensorModelPart(){
    SensorModelPart sensorPart = mock(SensorModelPart.class);
    return sensorPart;
  }
  
  private LightModelPart mockLightModelPart(){
    LightModelPart lightPart = mock(LightModelPart.class);
    return lightPart;
  }
}
