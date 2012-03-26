package penoplatinum.pacman;

/**
 * DashboardReporter
 * 
 * Collects and sends messages to the dashboard via bluetooth
 * 
 * @author Team Platinum
 */

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import penoplatinum.Config;

import penoplatinum.util.Utils;

import penoplatinum.model.GhostModel;
import penoplatinum.model.Reporter;

import penoplatinum.bluetooth.IConnection;
import penoplatinum.bluetooth.QueuedPacketTransporter;

import penoplatinum.grid.Grid;
import penoplatinum.grid.Sector;
import penoplatinum.grid.Agent;

import penoplatinum.simulator.Model;
import penoplatinum.simulator.Robot;
import penoplatinum.simulator.Bearing;

import penoplatinum.gateway.GatewayClient;

public class DashboardReporter implements Reporter {

  private GatewayClient client; // a reference to the client to use to comm
  private GhostRobot robot;
  private GhostModel model;

  StringBuilder sb = new StringBuilder();
  long nextTime = 0;

  public DashboardReporter useGatewayClient(GatewayClient client) {
    this.client = client;
    return this;
  }
  
  public DashboardReporter setRobot(Robot robot) {
    this.robot = (GhostRobot)robot;
    this.model = (GhostModel)this.robot.getModel();
    return this;
  }

  public DashboardReporter report() {
    if( this.client == null || this.robot == null || this.model == null ) {
      System.err.println( "DashboardReporter: missing client/robot/model" );
      return this;
    }
    
    this.sendModelDeltas();

    this.sendGrid("myGrid", this.model.getGridPart().getGrid());

    ArrayList<Sector> changed = this.model.getGridPart().getChangedSectors();
    for (int i = 0; i < changed.size(); i++) {
      this.sendSectorWalls(this.robot.getName(), "myGrid", changed.get(i));
    }
    return this;
  }

  private void sendModelDeltas() {
    long now = System.nanoTime();
    if(now < this.nextTime) { return; }
    this.nextTime = now + 300000000;

    Sector currentSector = this.model.getGridPart().getAgent().getSector();
    
    this.clear()
        .add(this.robot.getName())                                       .c()
        .add(this.model.getSensorPart().getLightSensorValue())           .c()
        .add(this.model.getLightPart().getCurrentLightColor().toString()).c()
        .add((int)this.model.getLightPart().getAverageLightValue())      .c()
        .add(this.model.getBarcodePart().getBarcode())                   .c()
        .add(this.model.getSensorPart().getSensorValue(Model.M3))        .c()
        .add(this.model.getSensorPart().getSensorValue(Model.S3))        .c()
        .add(this.model.getSensorPart().getSensorValue(Model.IR0))       .c()
        .add(this.model.getSensorPart().getSensorValue(Model.IR1))       .c()
        .add(this.model.getSensorPart().getSensorValue(Model.IR2))       .c()
        .add(this.model.getSensorPart().getSensorValue(Model.IR3))       .c()
        .add(this.model.getSensorPart().getSensorValue(Model.IR4))       .c()
        // ir_dist the shortest distance        
        .add(-1)                                                         .c()
        .add((int)currentSector.getWalls()).c();

    for(int atLocation=Bearing.N; atLocation<=Bearing.W; atLocation++ ) {
      Sector neighbour = currentSector.getNeighbour(atLocation);
      this.add(neighbour == null ? 0 : neighbour.getValue())             .c();
    }

    String event = "", source = "", plan = "", 
           queue = "", action = "", argument = "";
    int fps = 0;

    this.add(event)                                                      .c()
        .add(source)                                                     .c()
        .add(plan)                                                       .c()
        .add(queue)                                                      .c()
        .add(action)                                                     .c()
        .add(argument)                                                   .c()
        .add(fps);

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

  public Reporter reportWalls(Sector sector) {
    this.sendSectorWalls(this.robot.getName(), "myGrid", sector);
    return this;
  }


  private void sendSectorWalls(String name, String grid, Sector sector) {
    this.clear()
        .addSector(name, grid, sector)                                   .c()
        .add((int)sector.getWalls())
        .sendBuffer(Config.BT_WALLS);
  }

  private void sendSectorValue(String name, String grid, Sector sector) {
    this.clear()
        .addSector(name, grid, sector)                                   .c()
        .add(sector.getValue())
        .sendBuffer(Config.BT_VALUES);
  }

  private DashboardReporter addSector(String name, String grid, Sector sector) {
    this.add(name)                                                       .c()
        .add(grid)                                                       .c()
        .add(sector.getLeft())                                           .c()
        .add(sector.getTop());
    return this;
  }

  private DashboardReporter sendSectorAgent(String name, String grid, Agent agent) {
    this.clear()
        .add(name)                                                       .c()
        .add(grid)                                                       .c()
        .add(agent.getName())                                            .c()
        .add(agent.getSector().getLeft())                                .c()
        .add(agent.getSector().getTop())                                 .c()
        .add(agent.getBearing() + 1)                         .c()
        .add("white")
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

}
