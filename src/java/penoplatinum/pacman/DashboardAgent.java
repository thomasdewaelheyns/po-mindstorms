/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.pacman;

import java.io.IOException;
import penoplatinum.Utils;
import penoplatinum.bluetooth.IConnection;
import penoplatinum.bluetooth.QueuedPacketTransporter;
import penoplatinum.grid.Grid;
import penoplatinum.grid.Sector;
import penoplatinum.simulator.Model;
import penoplatinum.simulator.mini.Bearing;
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

  public void sendModelDeltas() {


    Sector s;


    builder.append("\"").append(robot.getName()).append("\",");
    builder.append(model.getLightSensorValue()).append(",");
    builder.append("\"").append(model.getCurrentLightColor().toString()).append("\",");
    builder.append(model.getAverageLightValue()).append(",");
    builder.append(model.getBarcode()).append(",");
    builder.append(this.model.getSensorValue(Model.M3)).append(",");
    builder.append(this.model.getSensorValue(Model.S3)).append(",");
    builder.append(this.model.getSensorValue(Model.IR0)).append(",");
    builder.append(this.model.getSensorValue(Model.IR1)).append(",");
    builder.append(this.model.getSensorValue(Model.IR2)).append(",");
    builder.append(this.model.getSensorValue(Model.IR3)).append(",");
    builder.append(this.model.getSensorValue(Model.IR4)).append(",");
    builder.append(-1).append(","); // ir_dist the shortest distance
    builder.append((int)this.model.getAgent().getSector().getWalls()).append(",");


    s = this.model.getAgent().getSector().getNeighbour(Bearing.N);
    builder.append(s == null ? 0 : s.getValue()).append(",");
    s = this.model.getAgent().getSector().getNeighbour(Bearing.E);
    builder.append(s == null ? 0 : s.getValue()).append(",");
    s = this.model.getAgent().getSector().getNeighbour(Bearing.S);
    builder.append(s == null ? 0 : s.getValue()).append(",");
    s = this.model.getAgent().getSector().getNeighbour(Bearing.W);
    builder.append(s == null ? 0 : s.getValue()).append(",");

    String event = "", source = "", plan = "", queue = "", action = "", argument = "";


    builder.append("\"").append(event).append("\",");
    builder.append("\"").append(source).append("\",");
    builder.append("\"").append(plan).append("\",");
    builder.append("\"").append(queue).append("\",").append("\"");
    builder.append(action).append("\",").append("\"").append(argument);

    String data = builder.toString();
    builder.setLength(0);
    try {
      transporter.getSendStream().writeBytes(data);
      transporter.SendPacket(123);
    } catch (IOException ex) {
      Utils.Log("Dashboard M send error");
    }



  }

  public void sendGrid(String gridName, Grid grid) {
    for (Sector s : grid.getSectors()) {
      sendSectorValues(model.getAgent().getName(), gridName, s);
    }
    for (Agent af : grid.getAgents()) {
      sendSectorAgents(model.getAgent().getName(), gridName, af);
    }
  }

  public void sendSectorWalls(String name, String grid, Sector s) {
    builder.setLength(0);
    builder.append("\"").append(name).append("\",");
    builder.append("\"").append(grid).append("\",");
    builder.append(s.getLeft()).append(",").append(s.getTop()).append(",").append(s.getWalls());
  }

  public void sendSectorValues(String name, String grid, Sector s) {
    builder.setLength(0);
    builder.append("\"").append(name).append("\",");
    builder.append("\"").append(grid).append("\",");
    builder.append(s.getLeft()).append(",").append(s.getTop()).append(",").append(s.getValue());
  }

  public void sendSectorAgents(String name, String grid, Agent ag) {
    builder.setLength(0);
    builder.append("\"").append(name).append("\",");
    builder.append("\"").append(grid).append("\",");
    builder.append(ag.getName()).append(",");
    builder.append(ag.getSector().getLeft()).append(",");
    builder.append(ag.getSector().getTop()).append(",");
    builder.append(ag.getBearing() + 1).append(",");
    builder.append("white");

  }

  void setRobot(GhostRobot robot) {
    this.robot = robot;
  }

  void setModel(GhostModel model) {
    this.model = model;
  }
}
