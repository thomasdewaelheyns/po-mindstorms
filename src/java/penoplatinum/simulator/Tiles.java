package penoplatinum.simulator;

/**
 * Enumeration of commonly used Tiles
 * 
 * Author: Team Platinum
 */

public class Tiles {
  // static instantiations of common tiles
  
  // up-down, down-up
  public static Tile N_S = new Tile()
    .withBarcode(Barcode.Forward)       .putBarcodeAt(Baring.N)
    .withWall   (Baring.E)              .withWall    (Baring.W)
    .withLine   (Baring.E,  Tile.BLACK) .withLine    (Baring.W, Tile.WHITE);
  public static Tile S_N = new Tile()
    .withBarcode(Barcode.Forward)       .putBarcodeAt(Baring.S)
    .withWall   (Baring.E)              .withWall    (Baring.W)
    .withLine   (Baring.E,  Tile.WHITE) .withLine    (Baring.W, Tile.BLACK);

  // left-right, right-left
  public static Tile W_E = new Tile()
    .withBarcode(Barcode.Forward)       .putBarcodeAt(Baring.W)
    .withWall   (Baring.N)              .withWall    (Baring.S)
    .withLine   (Baring.N,  Tile.BLACK) .withLine    (Baring.S, Tile.WHITE);
  public static Tile E_W = new Tile()
    .withBarcode(Barcode.Forward)       .putBarcodeAt(Baring.E)
    .withWall   (Baring.N)              .withWall    (Baring.S)
    .withLine   (Baring.N,  Tile.WHITE) .withLine    (Baring.S, Tile.BLACK);

  // up-right, right-up
  public static Tile N_E = new Tile()
    .withBarcode(Barcode.Left)          .putBarcodeAt(Baring.N)
    .withWall   (Baring.S)              .withWall    (Baring.W)
    .withLine   (Baring.S,  Tile.WHITE) .withLine    (Baring.W, Tile.WHITE)
    .withCorner (Baring.NE, Tile.BLACK);
  public static Tile E_N = new Tile()
    .withBarcode(Barcode.Right)         .putBarcodeAt(Baring.E)
    .withWall   (Baring.S)              .withWall    (Baring.W)
    .withLine   (Baring.S,  Tile.BLACK) .withLine    (Baring.W, Tile.BLACK)
    .withCorner (Baring.NE, Tile.WHITE);

  // down-right, right-down
  public static Tile S_E = new Tile()
    .withBarcode(Barcode.Right)         .putBarcodeAt(Baring.S)
    .withWall   (Baring.N)              .withWall    (Baring.W)
    .withLine   (Baring.N,  Tile.BLACK) .withLine    (Baring.W, Tile.BLACK)
    .withCorner (Baring.SE, Tile.WHITE);
  public static Tile E_S = new Tile()
    .withBarcode(Barcode.Left)          .putBarcodeAt(Baring.E)
    .withWall   (Baring.N)              .withWall    (Baring.W)
    .withLine   (Baring.N,  Tile.BLACK) .withLine    (Baring.W, Tile.BLACK)
    .withCorner (Baring.SE, Tile.WHITE);

  // down-left, left-down
  public static Tile S_W = new Tile()
    .withBarcode(Barcode.Left)          .putBarcodeAt(Baring.S)
    .withWall   (Baring.N)              .withWall    (Baring.E)
    .withLine   (Baring.N,  Tile.WHITE) .withLine    (Baring.E, Tile.WHITE)
    .withCorner (Baring.SW, Tile.BLACK);
  public static Tile W_S = new Tile()
    .withBarcode(Barcode.Right)         .putBarcodeAt(Baring.W)
    .withWall   (Baring.N)              .withWall    (Baring.E)
    .withLine   (Baring.N,  Tile.BLACK) .withLine    (Baring.E, Tile.BLACK)
    .withCorner (Baring.SW, Tile.WHITE);

  // up-left, left-up
  public static Tile N_W = new Tile()
    .withBarcode(Barcode.Right)         .putBarcodeAt(Baring.N)
    .withWall   (Baring.S)              .withWall    (Baring.E)
    .withLine   (Baring.S,  Tile.BLACK) .withLine    (Baring.E, Tile.BLACK)
    .withCorner (Baring.NW, Tile.WHITE);
  
  public static Tile W_N = new Tile()  
    .withBarcode(Barcode.Left)          .putBarcodeAt(Baring.W)
    .withWall   (Baring.S)              .withWall    (Baring.E)
    .withLine   (Baring.S,  Tile.WHITE) .withLine    (Baring.E, Tile.WHITE)
    .withCorner (Baring.NW, Tile.BLACK);
  public static Tile NONE = new Tile()
          .withWall(Baring.N)
          .withWall(Baring.E)
          .withWall(Baring.S)
          .withWall(Baring.W);
}
