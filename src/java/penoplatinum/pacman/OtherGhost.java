/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.pacman;

import penoplatinum.util.TransformationTRT;

/**
 * This class represents another ghost the model knows about. The ghost can be
 * identified by his name.
 * A transformation is provided which transforms coordinates from the remote
 * ghost to the model's coordinate system
 * 
 * @author MHGameWork
 */
public class OtherGhost {

  private String name;
  private TransformationTRT transformationTRT;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public TransformationTRT getTransformationTRT() {
    return transformationTRT;
  }

  public void setTransformationTRT(TransformationTRT transformationTRT) {
    this.transformationTRT = transformationTRT;
  }
}
