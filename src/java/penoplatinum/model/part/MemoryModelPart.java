/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.model.part;

import penoplatinum.model.Model;


/**
 *
 * @author Florian
 */
public class MemoryModelPart implements ModelPart  {
  
  public static MemoryModelPart from(Model model) {
    return (MemoryModelPart) model.getPart(ModelPartRegistry.MEMORY_MODEL_PART);
  }
  
  private long amountOfFreeMemory;
  private long amountofTotalMemory;
  private int fps;
  
  public void update(long free, long total, int fps){
    this.amountOfFreeMemory = free;
    this.amountofTotalMemory = total;
    this.fps = fps;
  }
  
  public long getFreeMemory(){
    return this.amountOfFreeMemory;
  }
  public long getTotalMemory(){
    return this.amountofTotalMemory;
  }
  public int getFps(){
    return this.fps;
  }
}
