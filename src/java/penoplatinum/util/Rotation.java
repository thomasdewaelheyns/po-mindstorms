package penoplatinum.util;

/**
 * Rotation
 * 
 * Basic enumeration of rotations
 * 
 * @author: Team Platinum
 */
public enum Rotation {

  NONE(0),
  L90(-1), L180(-2), L270(-3), L360(0),
  R90(1), R180(2), R270(3), R360(0);
  private int rotation;

  private Rotation(int rotation) {
    this.rotation = rotation;
  }

  private int getValue() {
    return this.rotation;
  }

  private Rotation get(int value) {
    switch (value) {
      case -1:
        return L90;
      case -2:
        return L180;
      case -3:
        return L270;
      case -4:
        return L360;
      case 1:
        return R90;
      case 2:
        return R180;
      case 3:
        return R270;
      case 4:
        return R360;
    }
    return NONE;
  }

  public Rotation add(Rotation rotation) {
    return this.get((this.rotation + rotation.getValue()) % 4);
  }

  public Rotation min() {
    switch (this.rotation) {
      case -1:
        return L90;
      case -2:
        return L180;
      case -3:
        return R90;
      case 1:
        return R90;
      case 2:
        return R180;
      case 3:
        return L90;
    }
    return NONE;
  }

  public Rotation invert() {
    return this.get(this.rotation * -1);
  }

}
