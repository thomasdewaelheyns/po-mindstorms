package penoplatinum.barcode;

import penoplatinum.modelprocessor.BufferSubset;
import penoplatinum.modelprocessor.ColorInterpreter;

public class BarcodeTranslator {

  ColorInterpreter interpreter;

  public BarcodeTranslator(ColorInterpreter interpreter) {
    this.interpreter = interpreter;
  }

  public int translate(BufferSubset list, int barcodeLength) {
    if (list.size() < barcodeLength) {
      return -1;
    }
    int val = 0;
    for (int i = 0; i < barcodeLength; i++) {
      int sum = 0;
      for (int j = (i * list.size()) / barcodeLength; j < (i + 1) * list.size() / barcodeLength; j++) {
        sum += list.get(j);
      }
      int averageValue = sum / (((i + 1) * list.size() / barcodeLength) - ((i * list.size()) / barcodeLength));
      val *= 2;
      val += interpreter.isBlackOrWhite(averageValue);
    }
    return val;
  }
}
