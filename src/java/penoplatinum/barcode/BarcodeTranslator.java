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
      for (int j = i * list.size() / barcodeLength + 2; j < (i + 1) * list.size() / barcodeLength - 2; j++) {
        sum += list.get(j);
      }
      int averageValue = sum / (((i + 1) * list.size() / barcodeLength) - ((i * list.size()) / barcodeLength));
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
}