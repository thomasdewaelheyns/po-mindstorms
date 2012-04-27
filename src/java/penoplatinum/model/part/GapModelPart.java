package penoplatinum.model.part;

/**
 * GapModelPart
 *
 * Contains information about the last gap detected by the sonar.
 * 
 * @author Team Platinum
 */


public class GapModelPart implements ModelPart {

  private boolean foundGap;
  private int     gapStart, gapEnd;


  public GapModelPart() {
    this.clearGap();
  }

  public void markGap(int start, int end) {
    this.foundGap = true;
    this.gapStart = start;
    this.gapEnd   = end;
  }
  
  public void clearGap() {
    this.foundGap = false;
    this.gapStart = 0;
    this.gapEnd = 0;
  }

  public int getGapStart() {
    return this.gapStart;
  }

  public int getGapEnd() {
    return this.gapEnd;
  }

  public boolean foundGap() {
    return this.foundGap;
  }
}
