package penoplatinum.barcode;

import penoplatinum.util.BufferSubset;

public class BarcodeTranslator {

  private static final int MINIMUM_BETTER = 1;

  public int translate(BufferSubset list, int length) {
    return translate(list, length, false);
  }

  public int translate(BufferSubset list, int barcodeLength, boolean reverse) {
    if (list.size() < barcodeLength) {
      return -1;
    }
    int val = 0;
    int pos = 1;
    for (int i = 0; i < barcodeLength; i++) {
      int sum = 0;
      //Utils.print((((i + 1) * list.size() / barcodeLength) - ((i * list.size()) / barcodeLength)) + " ");
      for (int j = i * list.size() / barcodeLength; j < (i + 1) * list.size() / barcodeLength; j++) {
        sum += list.get(j);
      }
      //int averageValue = sum / (((i + 1) * list.size() / barcodeLength) - ((i * list.size()) / barcodeLength));
      if (sum < MINIMUM_BETTER && sum > -MINIMUM_BETTER) {
        return -1;
      }
      sum = (sum > 0 ? 1 : 0);
      if (reverse) {
        val += pos * sum;
        pos *= 2;
      } else {
        val *= 2;
        val += sum;
      }
    }
    return val;
  }

  public static int reverse(int code, int length) {
    int out = 0;
    for (int i = 0; i < length; i++) {
      out <<= 1;
      out |= code & 1;
      code >>= 1;
    }
    return out;
  }
  
  public int correct(int in) {
    return in;
  }
  
  public static byte[] expand = 
    new byte[]{1, 2, 3, 4, 5, 6, 7, 9, 10, 11, 13, 14, 15, 
               17, 19, 21, 22, 23, 25, 27, 29, 31, 35, 37, 
               39, 43, 47, 55};

  public byte[] getExpand() {
    return expand;
  }
  
  
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