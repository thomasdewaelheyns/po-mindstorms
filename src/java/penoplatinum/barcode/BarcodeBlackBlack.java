package penoplatinum.barcode;

import penoplatinum.modelprocessor.BufferSubset;
import penoplatinum.modelprocessor.ColorInterpreter;

public class BarcodeBlackBlack implements BarcodeCorrector {

  BarcodeTranslator translator;

  public BarcodeBlackBlack(ColorInterpreter interpreter) {
    this.translator = new BarcodeTranslator(interpreter);
  }

  @Override
  public int translate(BufferSubset list) {
    return translator.translate(list, 8, true);
  }

  @Override
  public int correct(int in) {
    return in;
  }
  public static byte[] expand = new byte[]{1, 2, 3, 4, 5, 6, 7, 9, 10, 11, 13, 14, 15, 
                                           17, 19, 21, 22, 23, 25, 27, 29, 31, 35, 37, 
                                           39, 43, 47, 55};

  @Override
  public byte[] getExpand() {
    return expand;
  }
}