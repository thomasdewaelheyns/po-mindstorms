/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.pacman;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import penoplatinum.Utils;
import penoplatinum.bluetooth.IConnection;
import penoplatinum.bluetooth.QueuedPacketTransporter;
import penoplatinum.grid.Sector;
import penoplatinum.simulator.Model;
import penoplatinum.simulator.mini.Bearing;

/**
 * Collects and sends messages to the dashboard via bluetooth
 * @author MHGameWork
 */
public class DashboardAgent {

  private final IConnection connection;
  private final GhostModel model;
  private final GhostRobot robot;
  private final QueuedPacketTransporter transporter;

  public DashboardAgent(IConnection connection, GhostModel model, GhostRobot robot) {
    this.connection = connection;
    this.model = model;
    this.robot = robot;
    
    this.transporter = new QueuedPacketTransporter(connection);
    connection.RegisterTransporter(transporter, 123);
    connection.RegisterTransporter(transporter, 124);
    connection.RegisterTransporter(transporter, 125);
    connection.RegisterTransporter(transporter, 126);
    

  }
  StringBuilder builder = new StringBuilder();

  private void sendModelDeltas() {


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
    builder.append(this.model.getAgent().getSector().getWalls()).append(",");


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
    try {
      transporter.getSendStream().writeBytes(data);
      transporter.SendPacket(123);
    } catch (IOException ex) {
      Utils.Log("Dashboard M send error");
    }
    
    

  }
}
