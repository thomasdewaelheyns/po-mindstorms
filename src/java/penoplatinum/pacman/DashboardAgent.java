package penoplatinum.pacman;

import penoplatinum.model.GhostModel;
import java.io.IOException;
import penoplatinum.util.Utils;
import penoplatinum.bluetooth.IConnection;
import penoplatinum.bluetooth.QueuedPacketTransporter;
import penoplatinum.grid.Grid;
import penoplatinum.grid.Sector;
import penoplatinum.simulator.Model;
import penoplatinum.simulator.Bearing;
import penoplatinum.grid.Agent;

/**
 * Collects and sends messages to the dashboard via bluetooth
 * @author MHGameWork
 */
public class DashboardAgent {

  private final IConnection connection;
  private GhostModel model;
  private GhostRobot robot;
  private final QueuedPacketTransporter transporter;

  public DashboardAgent(IConnection connection) {
    this.connection = connection;

    this.transporter = new QueuedPacketTransporter(connection);
    connection.RegisterTransporter(transporter, 123);
    connection.RegisterTransporter(transporter, 124);
    connection.RegisterTransporter(transporter, 125);
    connection.RegisterTransporter(transporter, 126);
  }
  
  StringBuilder builder = new StringBuilder();
  long nextTime = 0;

  public void sendModelDeltas() {
    long now = System.nanoTime();

    if(now < this.nextTime) {
      return;
    }
    
    this.nextTime = now + 300000000;

    builder.delete(0, builder.length());
    
    Sector s;

    builder.append("\"").append(robot.getName()).append("\",");
    builder.append(model.getSensorPart().getLightSensorValue()).append(",");
    builder.append("\"").append(model.getLightPart().getCurrentLightColor().toString()).append("\",");
    builder.append((int)model.getLightPart().getAverageLightValue()).append(",");
    builder.append(model.getBarcodePart().getBarcode()).append(",");
    builder.append(this.model.getSensorPart().getSensorValue(Model.M3)).append(",");
    builder.append(this.model.getSensorPart().getSensorValue(Model.S3)).append(",");
    builder.append(this.model.getSensorPart().getSensorValue(Model.IR0)).append(",");
    builder.append(this.model.getSensorPart().getSensorValue(Model.IR1)).append(",");
    builder.append(this.model.getSensorPart().getSensorValue(Model.IR2)).append(",");
    builder.append(this.model.getSensorPart().getSensorValue(Model.IR3)).append(",");
    builder.append(this.model.getSensorPart().getSensorValue(Model.IR4)).append(",");
    builder.append(-1).append(","); // ir_dist the shortest distance
    builder.append((int)this.model.getGridPart().getAgent().getSector().getWalls()).append(",");


    s = this.model.getGridPart().getAgent().getSector().getNeighbour(Bearing.N);
    builder.append(s == null ? 0 : s.getValue()).append(",");
    s = this.model.getGridPart().getAgent().getSector().getNeighbour(Bearing.E);
    builder.append(s == null ? 0 : s.getValue()).append(",");
    s = this.model.getGridPart().getAgent().getSector().getNeighbour(Bearing.S);
    builder.append(s == null ? 0 : s.getValue()).append(",");
    s = this.model.getGridPart().getAgent().getSector().getNeighbour(Bearing.W);
    builder.append(s == null ? 0 : s.getValue()).append(",");

    String event = "", source = "", plan = "", queue = "", action = "", argument = "";
    int fps = 0;

    builder.append("\"").append(event).append("\",");
    builder.append("\"").append(source).append("\",");
    builder.append("\"").append(plan).append("\",");
    builder.append("\"").append(queue).append("\",").append("\"");
    builder.append(action).append("\",")
    .append("\"").append(argument).append("\",")
    .append(fps);
    sendBuffer(123);



  }

  public void sendBuffer(int packetIdentifier) {
    String data = builder.toString();
    try {
      transporter.getSendStream().writeBytes(data);
      transporter.SendPacket(packetIdentifier);
    } catch (IOException ex) {
      Utils.Log("Dashboard M send error");
    }
  }

  public void sendGrid(String gridName, Grid grid) {
    for (Sector s : grid.getSectors()) {
      sendSectorValues(model.getGridPart().getAgent().getName(), gridName, s);
    }
    for (Agent af : grid.getAgents()) {
      sendSectorAgents(model.getGridPart().getAgent().getName(), gridName, af);
    }
  }

  public void sendSectorWalls(String name, String grid, Sector s) {
    builder.delete(0, builder.length());
    builder.append("\"").append(name).append("\",");
    builder.append("\"").append(grid).append("\",");
    builder.append(s.getLeft()).append(",")
    .append(s.getTop()).append(",").append((int)s.getWalls());
    sendBuffer(124);
  }

  public void sendSectorValues(String name, String grid, Sector s) {
    builder.delete(0, builder.length());
    builder.append("\"").append(name).append("\",");
    builder.append("\"").append(grid).append("\",");
    builder.append(s.getLeft()).append(",").append(s.getTop()).append(",").append(s.getValue());
    sendBuffer(125);
  }

  public void sendSectorAgents(String name, String grid, Agent ag) {
    builder.delete(0, builder.length());
    builder.append("\"").append(name).append("\",");
    builder.append("\"").append(grid).append("\",");
    builder.append("\"").append(ag.getName()).append("\",");
    builder.append(ag.getSector().getLeft()).append(",");
    builder.append(ag.getSector().getTop()).append(",");
    builder.append(ag.getBearing() + 1).append(",");
    builder.append("\"white\"");
    
    sendBuffer(126);
  }

  void setRobot(GhostRobot robot) {
    this.robot = robot;
  }

  void setModel(GhostModel model) {
    this.model = model;
  }
}
