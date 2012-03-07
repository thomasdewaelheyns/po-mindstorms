package penoplatinum.barcode;

import penoplatinum.modelprocessor.BufferSubset;

public class BarcodeTranslator {

  private static final int MINIMUM_BETTER = 3;

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
      //int averageValue = sum / (((i + 1) * list.size() / barcodeLength) - ((i * list.size()) / barcodeLength));
      if (sum < MINIMUM_BETTER && sum > -MINIMUM_BETTER) {
        return -1;
      }
      val *= 2;
      val += (sum > 0 ? 1 : 0);
    }
    return val;
  }
}
