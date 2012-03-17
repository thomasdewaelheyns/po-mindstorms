package penoplatinum.util;

/**
 * An enumeration of the Colors with values BLACK, WHITE and BROWN
 */
/**
 *
 * @author MHGameWork
 */
public enum LightColor {

  Black(-1), White(1), Brown(0);
  private final int val;

  LightColor(int val) {
    this.val = val;
  }

  public int getVal() {
    return val;
  }

  @Override
  public String toString() {
    switch (val) {
      case -1:
        return "BLACK";
      case 0:
        return "BROWN";
      case 1:
        return "WHITE";
    }
    return "WHUT??";
  }
}
