package penoplatinum.util;

/**
 * An enumeration of the Colors with values BLACK, WHITE and BROWN
 *
 * @author Team Platinum
 */

public enum LightColor {

  BLACK(-1),
  WHITE (1),
  BROWN (0);
  
  private final int value;

  private LightColor(int value) {
    this.value = value;
  }

  public String toString() {
    switch(this.value) {
      case -1: return "BLACK";
      case  1: return "WHITE";
      case  0: return "BROWN";
    }
//    throw new RuntimeException( "Impossible Unknown Enum value." );
    throw new RuntimeException();
  }
}
