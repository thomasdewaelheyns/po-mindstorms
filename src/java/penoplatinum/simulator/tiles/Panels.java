package penoplatinum.simulator.tiles;

import penoplatinum.simulator.Barcode;
import penoplatinum.simulator.Bearing;

/**
 * Enumeration of commonly used Panels
 * 
 * @author: Team Platinum
 */

public class Panels {
  // static instantiations of common tiles
  
  // up-down, down-up
  public static Panel N_S = new Panel()
    .withBarcode(Barcode.Forward)       .putBarcodeAt(Bearing.N)
    .withWall   (Bearing.E)              .withWall    (Bearing.W)
    .withLine   (Bearing.E,  Panel.WHITE) .withLine    (Bearing.W, Panel.BLACK);
  public static Panel S_N = new Panel()
    .withBarcode(Barcode.Forward)       .putBarcodeAt(Bearing.S)
    .withWall   (Bearing.E)              .withWall    (Bearing.W)
    .withLine   (Bearing.E,  Panel.BLACK) .withLine    (Bearing.W, Panel.WHITE);

  // left-right, right-left
  public static Panel W_E = new Panel()
    .withBarcode(Barcode.Forward)       .putBarcodeAt(Bearing.W)
    .withWall   (Bearing.S)              .withWall    (Bearing.N)
    .withLine   (Bearing.N,  Panel.WHITE) .withLine    (Bearing.S, Panel.BLACK);
  public static Panel E_W = new Panel()
    .withBarcode(Barcode.Forward)       .putBarcodeAt(Bearing.E)
    .withWall   (Bearing.N)              .withWall    (Bearing.S)
    .withLine   (Bearing.N,  Panel.BLACK) .withLine    (Bearing.S, Panel.WHITE);

  // up-right, right-up
  public static Panel N_E = new Panel()
    .withBarcode(Barcode.Left)          .putBarcodeAt(Bearing.N)
    .withWall   (Bearing.S)              .withWall    (Bearing.W)
    .withLine   (Bearing.S,  Panel.BLACK) .withLine    (Bearing.W, Panel.BLACK)
    .withCorner (Bearing.NE, Panel.WHITE);
  public static Panel E_N = new Panel()
    .withBarcode(Barcode.Right)         .putBarcodeAt(Bearing.E)
    .withWall   (Bearing.S)              .withWall    (Bearing.W)
    .withLine   (Bearing.S,  Panel.WHITE) .withLine    (Bearing.W, Panel.WHITE)
    .withCorner (Bearing.NE, Panel.BLACK);

  // down-right, right-down
  public static Panel S_E = new Panel()
    .withBarcode(Barcode.Right)         .putBarcodeAt(Bearing.S)
    .withWall   (Bearing.N)              .withWall    (Bearing.W)
    .withLine   (Bearing.N,  Panel.WHITE) .withLine    (Bearing.W, Panel.WHITE)
    .withCorner (Bearing.SE, Panel.BLACK);
  public static Panel E_S = new Panel()
    .withBarcode(Barcode.Left)          .putBarcodeAt(Bearing.E)
    .withWall   (Bearing.N)              .withWall    (Bearing.W)
    .withLine   (Bearing.N,  Panel.WHITE) .withLine    (Bearing.W, Panel.WHITE)
    .withCorner (Bearing.SE, Panel.BLACK);

  // down-left, left-down
  public static Panel S_W = new Panel()
    .withBarcode(Barcode.Left)          .putBarcodeAt(Bearing.S)
    .withWall   (Bearing.N)              .withWall    (Bearing.E)
    .withLine   (Bearing.N,  Panel.BLACK) .withLine    (Bearing.E, Panel.BLACK)
    .withCorner (Bearing.SW, Panel.WHITE);
  public static Panel W_S = new Panel()
    .withBarcode(Barcode.Right)         .putBarcodeAt(Bearing.W)
    .withWall   (Bearing.N)              .withWall    (Bearing.E)
    .withLine   (Bearing.N,  Panel.WHITE) .withLine    (Bearing.E, Panel.WHITE)
    .withCorner (Bearing.SW, Panel.BLACK);

  // up-left, left-up
  public static Panel N_W = new Panel()
    .withBarcode(Barcode.Right)         .putBarcodeAt(Bearing.N)
    .withWall   (Bearing.S)              .withWall    (Bearing.E)
    .withLine   (Bearing.S,  Panel.WHITE) .withLine    (Bearing.E, Panel.WHITE)
    .withCorner (Bearing.NW, Panel.BLACK);
  
  public static Panel W_N = new Panel()  
    .withBarcode(Barcode.Left)          .putBarcodeAt(Bearing.W)
    .withWall   (Bearing.S)              .withWall    (Bearing.E)
    .withLine   (Bearing.S,  Panel.BLACK) .withLine    (Bearing.E, Panel.BLACK)
    .withCorner (Bearing.NW, Panel.WHITE);
  public static Panel NONE = new Panel()
          .withWall(Bearing.N)
          .withWall(Bearing.E)
          .withWall(Bearing.S)
          .withWall(Bearing.W);
  public static Panel N = new Panel()
          .withWall(Bearing.E)
          .withWall(Bearing.S)
          .withWall(Bearing.W);
  public static Panel E = new Panel()
          .withWall(Bearing.N)
          .withWall(Bearing.S)
          .withWall(Bearing.W);
  public static Panel S = new Panel()
          .withWall(Bearing.N)
          .withWall(Bearing.E)
          .withWall(Bearing.W);
  public static Panel W = new Panel()
          .withWall(Bearing.N)
          .withWall(Bearing.E)
          .withWall(Bearing.S);
  public static Panel E_S_W = new Panel()
          .withWall(Bearing.N);
    public static Panel N_E_W = new Panel()
          .withWall(Bearing.S);
}
