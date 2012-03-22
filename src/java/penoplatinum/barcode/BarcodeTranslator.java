package penoplatinum.barcode;

import penoplatinum.util.Utils;
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
    boolean fail = false;
    for (int i = 0; i < barcodeLength; i++) {
      int sum = 0;
//      Utils.print("\n[");
      System.out.print((((i + 1) * list.size() / barcodeLength) - ((i * list.size()) / barcodeLength))+" ");
      for (int j = i * list.size() / barcodeLength + 2; j < (i + 1) * list.size() / barcodeLength - 2; j++) {
        sum += list.get(j);
        Utils.print(list.get(j)+1 + ":");
      }
      int averageValue = sum / (((i + 1) * list.size() / barcodeLength) - ((i * list.size()) / barcodeLength));
      if (sum < MINIMUM_BETTER && sum > -MINIMUM_BETTER) {
        Utils.print("]\n");
        fail = true;
      }
//      System.out.print(sum);
      sum = (sum > 0 ? 1 : 0);
      if (reverse) {
        val += pos * sum;
        pos *= 2;
      } else {
        val *= 2;
        val += sum;
      }
    }
//    Utils.print("]\n");
    if(fail){
      return -1;
    }
    return val;
  }
  
  
  public static int invertBarcode(int code) {
    int out = 0;
    for (int i = 0; i < 6; i++) { //TODO: hardcoded barcode length!!!
      out |= code & 1;
      code >>= 1;
      out <<= 1;
    }
    out >>= 1;
    return out;
  }
}