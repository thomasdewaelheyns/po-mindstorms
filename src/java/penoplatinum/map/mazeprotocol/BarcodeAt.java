package penoplatinum.map.mazeprotocol;

import java.util.Scanner;
import penoplatinum.barcode.BarcodeBlackBlack;
import penoplatinum.barcode.BarcodeTranslator;
import penoplatinum.map.MapHashed;
import penoplatinum.simulator.tiles.Sector;

public class BarcodeAt implements Commando {
  
  private final boolean INVERT_CODE = true;
  private final boolean ALLOW_MIRROR = true;

  @Override
  public void interpret(MapHashed m, Scanner sc) {
    int x = sc.nextInt();
    int y = sc.nextInt();
    int code = sc.nextInt();
    int dir = sc.nextInt()-1;
    Sector s = new Sector();
    
    s.addWall((dir+1)%4);
    s.addWall((dir+3)%4);
    s.removeWall((dir+2)%4);
    s.removeWall(dir);
    if(INVERT_CODE){
      code = inverse(code);
      if(code<0){
        code = -code-1;
        dir = (dir+2)%4;
      }
    }
    s.addBarcode(code, dir);
    
    m.put(s, x, -y);
  }

  @Override
  public String getName() {
    return "BARCODEAT";
  }

  private int inverse(int code) {
    for(int i = 0; i<BarcodeBlackBlack.expand.length; i++){
      if(BarcodeBlackBlack.expand[i] == code){
        return i;
      }
      if(ALLOW_MIRROR && BarcodeBlackBlack.expand[i] == BarcodeTranslator.reverse(code, 6)){
        return -i-1;
      }
    }
    if(ALLOW_MIRROR){
      throw new RuntimeException("Incorrect barcode, symmetrical.");
    } else {
      throw new RuntimeException("Incorrect barcode, symmetrical or mirrored");
    }
  }
  
}
