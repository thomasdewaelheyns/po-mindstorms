package penoplatinum.model.part;

/**
 * Barcode
 * 
 * Accepts lightcolors and builds a barcode from them.
 * 
 * @author Team Platinum
 */

import java.util.List;
import java.util.ArrayList;

import penoplatinum.util.LightColor;


public class Barcode {

  // internal color coding
  private static final int BROWN =  0;
  private static final int WHITE =  1;
  private static final int BLACK = -1;

  private static final int BARCODE_LENGTH = 8;
  // how much must a line differ from 0
  private static final int MINIMUM_BETTER = 1;

  private List<Integer> colorBuffer = new ArrayList<Integer>();
  private int brownCount = 0;
  
  // private static byte[] expand = 
  //   new byte[]{  1,  2,  3,  4,  5,  6,  7,  9, 10, 11, 13, 14, 15, 
  //               17, 19, 21, 22, 23, 25, 27, 29, 31, 35, 37, 39, 43, 47, 55 };

  public Barcode() {}

  // copy constructor
  public Barcode(Barcode barcode) {
    if(barcode == null) { return; }
    this.colorBuffer.addAll(barcode.colorBuffer);
  }
  
  public void addColor(LightColor color){
    this.colorBuffer.add(this.toColor(color));
  }
  
  /**
   * This method divides the buffer in 8 equals parts,
   * The number of black values and white values are counted
   * The color that is best is taken as the barcode
   * @return The interpreted barcode
   */
  public int translate() {
    this.trimBrown();
    // we need at least enough colors equal to the length of a barcode
    if( this.colorBuffer.size() < BARCODE_LENGTH ) { return -1; }

    int barcodeOut = 0;
    for(int i = 0; i<BARCODE_LENGTH; i++) {
      int totalColor = 0,
          start = i * this.colorBuffer.size() / BARCODE_LENGTH,
          stop  = (i + 1) * this.colorBuffer.size() / BARCODE_LENGTH;

      for(int j = start; j<stop; j++) {
        totalColor += this.colorBuffer.get(j);
      }

      if(totalColor < MINIMUM_BETTER && totalColor > -MINIMUM_BETTER) { return -1; }
      barcodeOut = barcodeOut * 2 + (totalColor > 0 ? 1 : 0);
    }
    return barcodeOut;
  }
  
  private void trimBrown() {
    while(this.colorBuffer.size() > 0 && this.colorBuffer.get(0) == BROWN) {
      this.colorBuffer.remove(0);
    }
    while(this.colorBuffer.size() > 0 && 
          this.colorBuffer.get(this.colorBuffer.size()-1) == BROWN)
    {
      this.colorBuffer.remove(this.colorBuffer.size()-1);
    }
  }

  // shouldn't this be an instance method ?
  public static int reverse(int code, int length) {
    int out = 0;
    for (int i = 0; i < length; i++) {
      out <<= 1;
      out |= code & 1;
      code >>= 1;
    }
    return out;
  }

  // NOT IN USE ? 
  // public byte expandBarcode(int compressed) {
  //   if(compressed >= expand.length) { return 0; }
  //   return expand[compressed];
  // }

  // translate LightColor to internal int representation
  private int toColor(LightColor color){
    switch(color){
      case BLACK: return BLACK;
      case WHITE: return WHITE;
      default:    return BROWN;
    }
  }
}
