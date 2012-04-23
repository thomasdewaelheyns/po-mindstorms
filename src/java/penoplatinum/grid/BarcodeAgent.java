package penoplatinum.grid;

/**
 * BarcodeAgent
 * 
 * Implementation of an Agent, extending a MovingAgent into a Barcode
 * 
 * @author: Team Platinum
 */

import penoplatinum.util.Color;


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
