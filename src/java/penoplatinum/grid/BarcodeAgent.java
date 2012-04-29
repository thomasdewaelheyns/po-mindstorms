package penoplatinum.grid;

/**
 * BarcodeAgent
 * 
 * Implementation of an Agent, extending a MovingAgent into a Barcode
 * 
 * @author: Team Platinum
 */

import penoplatinum.util.Color;

/**
 * A BarcodeAgent is supposed to be unique for each type of barcode!!
 * Note: barcodes should be normalized!!! (so no reversed BarcodeAgents are 
 * allowed to exists)
 * 
 * @author MHGameWork
 */
public class BarcodeAgent extends MovingAgent {
  
  private int code;
  
  public BarcodeAgent(int code) { 
    super("barcode-" + code);
    this.code = code;
  }

  @Override
  public int   getValue() { return this.code; }
  @Override
  public Color getColor() { return null;      }

}
