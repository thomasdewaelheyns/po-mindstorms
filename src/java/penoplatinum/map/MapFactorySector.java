/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.map;

import java.util.HashMap;
import java.util.Scanner;
import penoplatinum.simulator.Map;
import penoplatinum.simulator.Map3D;
import penoplatinum.simulator.Sector;
import penoplatinum.simulator.Sectors;

/**
 *
 * @author Thomas
 */
public class MapFactorySector {
  HashMap<String, Sector> sectors = new HashMap<String, Sector>();
  HashMap<String, Boolean> commands = new HashMap<String, Boolean>();
  HashMap<String, Integer> directions = new HashMap<String, Integer>();
  
  public MapFactorySector(){
    addSectors();
    addCommands();
    addDirections();
  }
  
  public Map getMap(Scanner sc){
    int width = sc.nextInt();
    int length = sc.nextInt();
    Map map = new Map3D(length, width);
    for(int i = 0; i <length; i++){
      for(int j = 0; j <width; j++){
        String str = sc.next();
        Sector next = sectors.get(str);
        if(next == null){
          next = Sectors.NONE;
        }
        str = sc.next();
        Boolean test = commands.get(str);
        if(!(test == null) && (test)){
          int barcode = sc.nextInt();
          String direction = sc.next();
          next.addBarcode(barcode, directions.get(direction));
        }
        map.add(next);
      }
    }
    return map;
  }
  
  
  private void addSectors() {
    sectors.put("N", Sectors.N);
    sectors.put("E", Sectors.E);
    sectors.put("S", Sectors.S);
    sectors.put("W", Sectors.W);
    
    sectors.put("NE", Sectors.NE);
    sectors.put("NS", Sectors.NS);
    sectors.put("NW", Sectors.NW);
    sectors.put("ES", Sectors.ES);
    sectors.put("EW", Sectors.EW);
    sectors.put("SW", Sectors.SW);
    
    sectors.put("NES", Sectors.NES);
    sectors.put("NEW", Sectors.NEW);
    sectors.put("NSW", Sectors.NSW);
    sectors.put("ESW", Sectors.ESW);
    
    sectors.put("NESW", Sectors.NESW);
    
    sectors.put("NOWALL", Sectors.NOWALL);
    
    sectors.put("NONE", Sectors.NONE);
   
  }
  
  private void addCommands(){
    commands.put( "BARCODE", Boolean.TRUE);
    commands.put( "NOBARCODE", Boolean.FALSE);
  }
  
  private void addDirections(){
    directions.put("NS", 0);
    directions.put("SN", 1);
    directions.put("EW", 2);
    directions.put("WE", 3);
  }
}