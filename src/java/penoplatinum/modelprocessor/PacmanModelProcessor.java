package penoplatinum.modelprocessor;

import penoplatinum.grid.Sector;
import penoplatinum.pacman.GhostModel;
import penoplatinum.pacman.PacmanAgent;

public class PacmanModelProcessor extends ModelProcessor {

  public PacmanModelProcessor() {
  }

  public PacmanModelProcessor(ModelProcessor nextProcessor) {
    super(nextProcessor);
  }

  @Override
  protected void work() {
    GhostModel m = (GhostModel) model;
    if (m.isIsNextToPacman()) {
      System.out.println("PACMAN!!!!!");
      PacmanAgent pacman = new PacmanAgent();
      Sector s = m.getGrid().getSector(m.getPacmanX(), m.getPacmanY());
      pacman.assignSector(s, 0);
      m.getGrid().addAgent(pacman);
    }
  }
}
