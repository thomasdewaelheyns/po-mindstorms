/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.grid;

import penoplatinum.grid.agent.Agent;

/**
 *
 * @author MHGameWork
 */
public interface GridObserver {

  void agentChanged(Grid g, Agent a);
  void sectorChanged(Grid g, Sector s);
}
