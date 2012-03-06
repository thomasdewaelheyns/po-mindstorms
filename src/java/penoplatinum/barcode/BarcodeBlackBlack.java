package penoplatinum.barcode;

import penoplatinum.modelprocessor.BufferSubset;

public class BarcodeBlackBlack implements BarcodeCorrector {
  BarcodeTranslator translator;
  

  
  @Override
  public int translate(BufferSubset list){
    return translator.translate(list, 7);
  }
  
  @Override
  public int correct(int in){
    return in;
  }
  
  @Override
  public byte[] getExpand(){
    throw new RuntimeException("Not made yet!");
    //return null;
  }
  
  
  
}
