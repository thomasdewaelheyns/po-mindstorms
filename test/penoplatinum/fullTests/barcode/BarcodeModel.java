package penoplatinum.fullTests.barcode;

import penoplatinum.fullTests.line.LineModel;
import penoplatinum.model.part.BarcodeModelPart;
import penoplatinum.model.part.ModelPart;
import penoplatinum.model.part.ModelPartRegistry;

public class BarcodeModel extends LineModel {
  private BarcodeModelPart barcodePart = new BarcodeModelPart();

  public BarcodeModel() {
    super();
  }
  
  public ModelPart getPart(int id) {
    switch(id) {
      case ModelPartRegistry.BARCODE_MODEL_PART : return barcodePart;
    }
    return super.getPart(id);
  }
  
}
