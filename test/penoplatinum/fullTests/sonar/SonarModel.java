package penoplatinum.fullTests.sonar;

import penoplatinum.fullTests.gatewayclient.GateModel;
import penoplatinum.model.part.ModelPart;
import penoplatinum.model.part.ModelPartRegistry;
import penoplatinum.model.part.SonarModelPart;
import penoplatinum.model.part.WallsModelPart;

public class SonarModel extends GateModel {
  private SonarModelPart sonarPart = new SonarModelPart();
  private WallsModelPart wallPart = new WallsModelPart();

  @Override
  public ModelPart getPart(int id) {
    if(ModelPartRegistry.SONAR_MODEL_PART==id){
      return sonarPart;
    }
    if( ModelPartRegistry.WALLS_MODEL_PART == id){
      return wallPart;
    }
    return super.getPart(id);
  }
  
  
}
