package penoplatinum.simulator;

/**
 * Enumeration of commonly used Panels
 * 
 * @author: Team Platinum
 */

public class Panels {
  // static instantiations of common tiles
  
  // up-down, down-up
  public static Panel N_S = new Panel()
    .withBarcode(Barcode.Forward)       .putBarcodeAt(Baring.N)
    .withWall   (Baring.E)              .withWall    (Baring.W)
    .withLine   (Baring.E,  Panel.WHITE) .withLine    (Baring.W, Panel.BLACK);
  public static Panel S_N = new Panel()
    .withBarcode(Barcode.Forward)       .putBarcodeAt(Baring.S)
    .withWall   (Baring.E)              .withWall    (Baring.W)
    .withLine   (Baring.E,  Panel.BLACK) .withLine    (Baring.W, Panel.WHITE);

  // left-right, right-left
  public static Panel W_E = new Panel()
    .withBarcode(Barcode.Forward)       .putBarcodeAt(Baring.W)
    .withWall   (Baring.S)              .withWall    (Baring.N)
    .withLine   (Baring.N,  Panel.WHITE) .withLine    (Baring.S, Panel.BLACK);
  public static Panel E_W = new Panel()
    .withBarcode(Barcode.Forward)       .putBarcodeAt(Baring.E)
    .withWall   (Baring.N)              .withWall    (Baring.S)
    .withLine   (Baring.N,  Panel.BLACK) .withLine    (Baring.S, Panel.WHITE);

  // up-right, right-up
  public static Panel N_E = new Panel()
    .withBarcode(Barcode.Left)          .putBarcodeAt(Baring.N)
    .withWall   (Baring.S)              .withWall    (Baring.W)
    .withLine   (Baring.S,  Panel.BLACK) .withLine    (Baring.W, Panel.BLACK)
    .withCorner (Baring.NE, Panel.WHITE);
  public static Panel E_N = new Panel()
    .withBarcode(Barcode.Right)         .putBarcodeAt(Baring.E)
    .withWall   (Baring.S)              .withWall    (Baring.W)
    .withLine   (Baring.S,  Panel.WHITE) .withLine    (Baring.W, Panel.WHITE)
    .withCorner (Baring.NE, Panel.BLACK);

  // down-right, right-down
  public static Panel S_E = new Panel()
    .withBarcode(Barcode.Right)         .putBarcodeAt(Baring.S)
    .withWall   (Baring.N)              .withWall    (Baring.W)
    .withLine   (Baring.N,  Panel.WHITE) .withLine    (Baring.W, Panel.WHITE)
    .withCorner (Baring.SE, Panel.BLACK);
  public static Panel E_S = new Panel()
    .withBarcode(Barcode.Left)          .putBarcodeAt(Baring.E)
    .withWall   (Baring.N)              .withWall    (Baring.W)
    .withLine   (Baring.N,  Panel.WHITE) .withLine    (Baring.W, Panel.WHITE)
    .withCorner (Baring.SE, Panel.BLACK);

  // down-left, left-down
  public static Panel S_W = new Panel()
    .withBarcode(Barcode.Left)          .putBarcodeAt(Baring.S)
    .withWall   (Baring.N)              .withWall    (Baring.E)
    .withLine   (Baring.N,  Panel.BLACK) .withLine    (Baring.E, Panel.BLACK)
    .withCorner (Baring.SW, Panel.WHITE);
  public static Panel W_S = new Panel()
    .withBarcode(Barcode.Right)         .putBarcodeAt(Baring.W)
    .withWall   (Baring.N)              .withWall    (Baring.E)
    .withLine   (Baring.N,  Panel.WHITE) .withLine    (Baring.E, Panel.WHITE)
    .withCorner (Baring.SW, Panel.BLACK);

  // up-left, left-up
  public static Panel N_W = new Panel()
    .withBarcode(Barcode.Right)         .putBarcodeAt(Baring.N)
    .withWall   (Baring.S)              .withWall    (Baring.E)
    .withLine   (Baring.S,  Panel.WHITE) .withLine    (Baring.E, Panel.WHITE)
    .withCorner (Baring.NW, Panel.BLACK);
  
  public static Panel W_N = new Panel()  
    .withBarcode(Barcode.Left)          .putBarcodeAt(Baring.W)
    .withWall   (Baring.S)              .withWall    (Baring.E)
    .withLine   (Baring.S,  Panel.BLACK) .withLine    (Baring.E, Panel.BLACK)
    .withCorner (Baring.NW, Panel.WHITE);
  public static Panel NONE = new Panel()
          .withWall(Baring.N)
          .withWall(Baring.E)
          .withWall(Baring.S)
          .withWall(Baring.W);
  public static Panel N = new Panel()
          .withWall(Baring.E)
          .withWall(Baring.S)
          .withWall(Baring.W);
  public static Panel E = new Panel()
          .withWall(Baring.N)
          .withWall(Baring.S)
          .withWall(Baring.W);
  public static Panel S = new Panel()
          .withWall(Baring.N)
          .withWall(Baring.E)
          .withWall(Baring.W);
  public static Panel W = new Panel()
          .withWall(Baring.N)
          .withWall(Baring.E)
          .withWall(Baring.S);
}
