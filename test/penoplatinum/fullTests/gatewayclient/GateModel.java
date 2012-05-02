package penoplatinum.fullTests.gatewayclient;

import penoplatinum.fullTests.barcode.BarcodeModel;
import penoplatinum.model.part.MessageModelPart;
import penoplatinum.model.part.ModelPart;
import penoplatinum.model.part.ModelPartRegistry;

public class GateModel extends BarcodeModel {

  private MessageModelPart messagePart;

  public GateModel() {
    super();
  }

  @Override
  public ModelPart getPart(int id) {
    switch (id) {
      case ModelPartRegistry.MESSAGE_MODEL_PART: return messagePart;
    }
    return super.getPart(id);
  }
}
