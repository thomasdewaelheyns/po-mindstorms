package penoplatinum.reporter;

/**
 * DashboardReporter
 * 
 * Collects and sends messages to the dashboard via bluetooth
 * 
 * @author Team Platinum
 */

import penoplatinum.Config;
import penoplatinum.gateway.GatewayClient;
import penoplatinum.grid.Grid;
import penoplatinum.grid.Sector;
import penoplatinum.grid.agent.Agent;
import penoplatinum.model.GhostModel;
import penoplatinum.model.Model;
import penoplatinum.model.part.BarcodeModelPart;
import penoplatinum.model.part.GridModelPart;
import penoplatinum.model.part.LightModelPart;
import penoplatinum.model.part.SensorModelPart;
import penoplatinum.robot.GhostRobot;
import penoplatinum.robot.Robot;
import penoplatinum.util.Bearing;
import penoplatinum.util.Point;

public class DashboardReporter implements Reporter {

  private GatewayClient client; // a reference to the client to use to comm
  private GhostRobot robot;
  private GhostModel model;

  StringBuilder sb = new StringBuilder();
  long nextTime = 0;

  @Override
  public DashboardReporter useGatewayClient(GatewayClient client) {
    this.client = client;
    return this;
  }
  
  public DashboardReporter reportFor(Robot robot) {
    this.robot = (GhostRobot)robot;
    this.model = (GhostModel)this.robot.getModel();
    return this;
  }

  private void sendModelDeltas() {
    long now = System.nanoTime();
    if(now < this.nextTime) { return; }
    this.nextTime = now + 300000000;

    SensorModelPart  sensorPart = SensorModelPart.from(this.model);
    BarcodeModelPart barcodePart = BarcodeModelPart.from(this.model);
    LightModelPart   lightPart = LightModelPart.from(this.model);
    GridModelPart    gridPart = GridModelPart.from(this.model);

    Agent agent = gridPart.getMyAgent();
    Point pos = this.getMainGrid(this.model).getPositionOf(agent);

    Sector currentSector = this.getMainGrid(this.model).getSectorAt(pos);
    
    this.clear()
        .add(this.robot.getName())                                       .c()
        .add(sensorPart.getLightSensorValue())                           .c()
        .add(lightPart.getCurrentLightColor().toString())                .c()
        .add((int)lightPart.getAverageLightValue())                      .c()
        .add(barcodePart.getLastBarcodeValue())                          .c()
        .add(sensorPart.getSonarAngle())                                 .c()
        .add(sensorPart.getSonarDistance())                              .c()
        .add(sensorPart.getIRValue(0))                                   .c()
        .add(sensorPart.getIRValue(1))                                   .c()
        .add(sensorPart.getIRValue(2))                                   .c()
        .add(sensorPart.getIRValue(3))                                   .c()
        .add(sensorPart.getIRValue(4))                                   .c()
        .add(sensorPart.getIRDistance())                                 .c()
        .add(this.getWalls(currentSector)).c();

    for(Bearing direction: Bearing.NESW){
      Sector neighbour = currentSector.getNeighbour(direction);
      this.add(neighbour == null ? 0 : neighbour.getValue())             .c();
    }

    String event = "", source = "", plan = "", 
           queue = "", action = "", argument = "";

    this.add(event)                                                      .c()
        .add(source)                                                     .c()
        .add(plan)                                                       .c()
        .add(queue)                                                      .c()
        .add(action)                                                     .c()
        .add(argument)                                                   .c()
        .add(sensorPart.getFPS());

    this.sendBuffer(Config.BT_MODEL);
  }

  private void sendGrid(String gridName, Grid grid) {
    for( Sector sector : grid.getSectors() ) {
      this.sendSectorValue(this.robot.getName(), gridName, sector);
    }
    for( Agent agent : grid.getAgents() ) {
      this.sendSectorAgent(this.robot.getName(), gridName, agent);
    }
  }

  // public Reporter reportWalls(Sector sector) {
  //   this.sendSectorWalls(this.robot.getName(), "myGrid", sector);
  //   return this;
  // }

  private void sendSectorWalls(String name, String grid, Sector sector) {
    this.clear()
        .addSector(name, grid, sector)                                   .c()
        .add(this.getWalls(sector))
        .sendBuffer(Config.BT_WALLS);
  }

  private void sendSectorValue(String name, String grid, Sector sector) {
    this.clear()
        .addSector(name, grid, sector)                                   .c()
        .add(sector.getValue())
        .sendBuffer(Config.BT_VALUES);
  }

  private DashboardReporter addSector(String name, String grid, Sector sector) {
    Grid mainGrid = this.getMainGrid(this.model);
    this.add(name)                                                       .c()
        .add(grid)                                                       .c()
        .add(mainGrid.getPositionOf(sector).getX())                      .c()
        .add(mainGrid.getPositionOf(sector).getY());
    return this;
  }

  private DashboardReporter sendSectorAgent(String name, String grid, Agent agent) {
    Grid mainGrid = this.getMainGrid(this.model);
    Point pos = mainGrid.getPositionOf(agent);
    Sector sec = mainGrid.getSectorAt(pos);
    this.clear()
        .add(name)                                                       .c()
        .add(grid)                                                       .c()
        .add(agent.getName())                                            .c()
        .add(pos.getX())                                                 .c()
        .add(pos.getY())                                                 .c()
        .add(translateBearingToInt(mainGrid.getBearingOf(agent)))        .c()
        .add(agent.getColor().toString())
        .sendBuffer(Config.BT_AGENTS);
    return this;
  }

  private DashboardReporter add(String string) {
    this.sb.append("\"").append(string).append("\"");
    return this;
  }

  private DashboardReporter add(int integer) {
    this.sb.append(integer);
    return this;
  }

  private DashboardReporter c() {
    this.sb.append(",");
    return this;
  }

  private DashboardReporter clear() {
    this.sb.delete(0, sb.length());
    return this;
  }

  private void sendBuffer(int channel) {
    this.client.send(this.sb.toString(), channel); 
  }
  
  private Grid getMainGrid(Model model){
    GridModelPart gridPart = GridModelPart.from(model);
    return gridPart.getMyGrid();
  }
  
  private int getWalls(Sector sector){
    int code = 0;
    code += ((!sector.knowsWall(Bearing.N) || sector.hasNoWall(Bearing.N)) ? 0 : 1)<<3;
    code += ((!sector.knowsWall(Bearing.E) || sector.hasNoWall(Bearing.E)) ? 0 : 1)<<2;
    code += ((!sector.knowsWall(Bearing.S) || sector.hasNoWall(Bearing.S)) ? 0 : 1)<<1;
    code += ((!sector.knowsWall(Bearing.W) || sector.hasNoWall(Bearing.W)) ? 0 : 1);
    return code;
  }

  @Override
  public Reporter reportModelUpdate() {
    this.sendModelDeltas();
    return this;
  }

  @Override
  public Reporter reportSectorUpdate(Sector sector, String dashboardGridName) {
    this.sendSectorWalls(this.robot.getName(), dashboardGridName, sector);
    return this;
  }
  
  public Reporter reportValueUpdate(Sector sector) {
    this.sendSectorValue(this.robot.getName(), "myGrid", sector);
    return this;
  }

  @Override
  public Reporter reportAgentUpdate(Agent agent) {
    this.sendSectorAgent(this.robot.getName(), "myGrid", agent);
    return this;
  }
  
  private int translateBearingToInt(Bearing bear){
    if(bear == Bearing.N)
      return 1;
    if(bear == Bearing.E)
      return 2;
    if(bear == Bearing.S)
      return 3;
    if(bear == Bearing.W)
      return 4;
    return 0;
  }

  @Override
  public void reportCaptured() {
    
  }
}
