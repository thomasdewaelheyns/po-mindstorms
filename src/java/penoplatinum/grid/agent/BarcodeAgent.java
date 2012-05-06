package penoplatinum.grid.agent;

/**
 * BarcodeAgent
 * 
 * Implementation of an Agent, extending a MovingAgent into a Barcode
 * 
 * @author: Team Platinum
 */

import penoplatinum.util.Color;
import penoplatinum.util.SimpleHashMap;

/**
 * A BarcodeAgent is supposed to be unique for each type of barcode!!
 * Note: barcodes should be normalized!!! (so no reversed BarcodeAgents are 
 * allowed to exists)
 * 
 * @author MHGameWork
 */
public class BarcodeAgent extends MovingAgent {
  
  private  static SimpleHashMap<Integer,BarcodeAgent> agents = new SimpleHashMap<Integer, BarcodeAgent>();
  
  public static BarcodeAgent getBarcodeAgent(int code)
  {
    BarcodeAgent ret = agents.get(code);
    if (ret == null)
    {
      ret = new BarcodeAgent(code);
      agents.put(code, ret);
    }
    return ret;
  }
  
  private int code;
  
  private BarcodeAgent(int code) { 
    super("barcode-" + code);
    this.code = code;
  }

  @Override
  public int   getValue() { return this.code; }
  @Override
  public Color getColor() { return null;      }

}
