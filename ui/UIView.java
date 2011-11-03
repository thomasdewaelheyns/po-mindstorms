/**
 * UIView interface
 * 
 * Defines the interface for Viewers for the UI
 * 
 * Author: Team Platinum
 */
public interface UIView {
  public static int BLACK=0;
  public static int WHITE=1;
  public static int BROWN=3;

  public static int GO_FORWARD=0;
  public static int GO_LEFT=1;
  public static int GO_RIGHT=2;
  
  public void update( int lightValue, int lightColor, 
                      int barcode, int direction );
}
