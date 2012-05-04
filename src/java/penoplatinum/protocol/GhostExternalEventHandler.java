/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.protocol;

import penoplatinum.grid.*;
import penoplatinum.model.Model;
import penoplatinum.model.part.GridModelPart;
import penoplatinum.util.Bearing;
import penoplatinum.util.Point;

/**
 *
 * @author Thomas
 */
public class GhostExternalEventHandler implements ExternalEventHandler{

  private Model model = null;
  
  public GhostExternalEventHandler useModel(Model model){
    if(model == null)
      throw new IllegalArgumentException("The given model is not valid.");
    this.model = model;
    return this;
  }
          
          
  @Override
  public void handleActivation() {
    GridModelPart.from(this.model).getMyAgent().activate();
  }

  @Override
  public void handleSectorInfo(String agentName, Point position, boolean knowsN, boolean n, boolean knowsE, boolean e, boolean knowsS, boolean s, boolean knowsW, boolean w) {
    GridModelPart gridModel = GridModelPart.from(this.model);
    Grid g = gridModel.getGridOf(agentName);
    Sector sec = g.getSectorAt(position);
    if(sec == null){
      sec = new LinkedSector();
      g.add(sec, position);
    }
    
    if(knowsN){
      if(n){ sec.setWall(Bearing.N); } else{ sec.setNoWall(Bearing.N); }
    }
    if(knowsE){
      if(e){ sec.setWall(Bearing.E); } else{ sec.setNoWall(Bearing.E); }
    }
    if(knowsS){
      if(s){ sec.setWall(Bearing.S); } else{ sec.setNoWall(Bearing.S); }
    }
    if(knowsW){
      if(w){ sec.setWall(Bearing.W); } else{ sec.setNoWall(Bearing.W); }
    }
  }

  @Override
  public void handleNewAgent(String agentName) {
    GridModelPart gridModel = GridModelPart.from(this.model);
    gridModel.getGridOf(agentName);
  }

  @Override
  public void handleAgentInfo(String agentName, Point position, int value, Bearing bearing) {
    GridModelPart gridModel = GridModelPart.from(this.model);
    Grid g = gridModel.getGridOf(agentName);
    Sector sec = g.getSectorAt(position);
    if(sec == null){
      sec = new LinkedSector();
      g.add(sec, position);
    }
    if(value == 0){
      Agent agent = g.getAgent(agentName);
      g.moveTo(agent, position, bearing);
    }
    else{
      BarcodeAgent barcode = new BarcodeAgent(value);
      g.add(barcode, position, bearing);
    }
  }

  @Override
  public void handleTargetInfo(String agentName, Point position) {
    GridModelPart gridModel = GridModelPart.from(this.model);
    gridModel.setPacman(position);
  }

  @Override
  public void handleSendGridInformation() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void handleCaptured(String agentName) {
    
  }

  @Override
  public void handleRemoveAgent(String agentName) {
    
  }
  
}
