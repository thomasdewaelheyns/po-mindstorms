package penoplatinum.ui;

/**
 * UIView interface
 * 
 * Defines the interface for Viewers for the UI
 * 
 * Author: Team Platinum
 */
public interface UIView {
  public static int NONE=-1;

  // detected color
  public static int BLACK=0;
  public static int WHITE=1;
  public static int BROWN=3;

  // direction, based on the barcode
  public static int GO_FORWARD=0;
  public static int GO_LEFT=1;
  public static int GO_RIGHT=2;

  // unique id's for each of the supported sensors
  public static int LIGHT   = 323962144;
  public static int SONAR   = 721955983;
  public static int BARCODE = 414143066;

  // update methods for sensor information:
  // lightsensor provides a lightValue (0-1024)
  // the robot translates this value to a lightColor (black,white or brown)
  public void updateLight  ( int lightValue, int lightColor );
  // the sonarsensor is composed of an angle (zero-based north facing)
  // and a measured distance in mm
  public void updateSonar  ( int angle,      int distance );
  // the robot can detect barcodes, which are interpreted as a direction
  public void updateBarcode( int barcode,    int direction );
}
