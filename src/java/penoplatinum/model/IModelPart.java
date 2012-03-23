/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.model;

/**
 *
 * @author MHGameWork
 */
public interface IModelPart {
  /**
   * Marks all the changes in this part as processed by the modelprocessors
   */
  void clearDirty();
  
}
