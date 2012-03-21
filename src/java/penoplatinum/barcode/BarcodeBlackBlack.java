package penoplatinum.barcode;

import penoplatinum.util.BufferSubset;

public class BarcodeBlackBlack implements BarcodeCorrector {

  BarcodeTranslator translator = new BarcodeTranslator();

  public BarcodeBlackBlack() {}

  @Override
  public int translate(BufferSubset list) {
    int temp = translator.translate(list, 8, false);
    return temp;
  }

  @Override
  public int correct(int in) {
    return in;
  }
  public static byte[] expand = new byte[]{1, 2, 3, 4, 5, 6, 7, 9, 10, 11, 13, 14, 15, 
                                           17, 19, 21, 22, 23, 25, 27, 29, 31, 35, 37, 
                                           39, 43, 47, 55};

  public byte[] getExpand() {
    return expand;
  }
  
  @Override
  public byte expandBarcode(int compressed)
  {
    if (compressed >= expand.length)
    {
//      System.out.println("Warning: unknown compressed barcode " + compressed);
      return 0;
    }
    return expand[compressed];
  }
}