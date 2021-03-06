/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.grid;

import penoplatinum.grid.agent.BarcodeAgent;
import penoplatinum.util.Bearing;
import penoplatinum.util.Point;

/**
 *
 * @author Florian
 */
public class GridTestUtil {

  public static Grid createGridNorth(Grid northGrid) {
    //CREATE SECTORS
    Sector Sector00 = createSector().setWall(Bearing.W);
    Sector Sector10 = createSector();
    Sector Sector20 = createSector().setWall(Bearing.N).setWall(Bearing.E).setWall(Bearing.S);
    Sector Sector01 = createSector().setWall(Bearing.W).setWall(Bearing.E);
    Sector Sector11 = createSector().setWall(Bearing.W).setWall(Bearing.S);
    Sector Sector21 = createSector().setWall(Bearing.E).setWall(Bearing.N);
    Sector Sector02 = createSector().setWall(Bearing.W).setWall(Bearing.S);
    Sector Sector12 = createSector().setWall(Bearing.S).setWall(Bearing.N).setWall(Bearing.E);
    Sector Sector22 = createSector().setWall(Bearing.S).setWall(Bearing.E).setWall(Bearing.W);
    //ADD NEIGHBOURS
    northGrid.add(Sector00, new Point(0, 0));
    northGrid.add(Sector10, new Point(1, 0));
    northGrid.add(Sector20, new Point(2, 0));
    northGrid.add(Sector01, new Point(0, 1));
    northGrid.add(Sector11, new Point(1, 1));
    northGrid.add(Sector21, new Point(2, 1));
    northGrid.add(Sector02, new Point(0, 2));
    northGrid.add(Sector12, new Point(1, 2));
    northGrid.add(Sector22, new Point(2, 2));
    //Barcode
    northGrid.add(BarcodeAgent.getBarcodeAgent(19), new Point(1, 0), Bearing.N);

    return northGrid;
  }

  public static Grid createGridEast(Grid eastGrid) {
    Sector Sector00 = createSector();
    Sector Sector10 = createSector().setWall(Bearing.S).setWall(Bearing.E);
    Sector Sector20 = createSector().setWall(Bearing.W);
    Sector Sector01 = createSector();
    Sector Sector11 = createSector().setWall(Bearing.N).setWall(Bearing.E);
    Sector Sector21 = createSector().setWall(Bearing.W);
    Sector Sector02 = createSector();
    Sector Sector12 = createSector();
    Sector Sector22 = createSector();
    //ADD NEIGHBOURS
    eastGrid.add(Sector00, new Point(0, 0));
    eastGrid.add(Sector10, new Point(1, 0));
    eastGrid.add(Sector20, new Point(2, 0));
    eastGrid.add(Sector01, new Point(0, 1));
    eastGrid.add(Sector11, new Point(1, 1));
    eastGrid.add(Sector21, new Point(2, 1));
    eastGrid.add(Sector02, new Point(0, 2));
    eastGrid.add(Sector12, new Point(1, 2));
    eastGrid.add(Sector22, new Point(2, 2));
    eastGrid.add(BarcodeAgent.getBarcodeAgent(13), new Point(1, 0), Bearing.N);
    //Barcode
    return eastGrid;
  }

  public static Grid createGridWest(Grid eastGrid) {
    Sector Sector00 = createSector();
    Sector Sector10 = createSector().setWall(Bearing.E);
    Sector Sector20 = createSector().setWall(Bearing.W).setWall(Bearing.N);
    Sector Sector01 = createSector().setWall(Bearing.W).setWall(Bearing.S);
    Sector Sector11 = createSector();
    Sector Sector21 = createSector().setWall(Bearing.S).setWall(Bearing.E);
    Sector Sector02 = createSector().setWall(Bearing.N).setWall(Bearing.W);
    Sector Sector12 = createSector().setWall(Bearing.S);
    Sector Sector22 = createSector().setWall(Bearing.N);
    Sector Sector03 = createSector().setWall(Bearing.W).setWall(Bearing.S);
    Sector Sector13 = createSector().setWall(Bearing.N).setWall(Bearing.S);
    Sector Sector23 = createSector().setWall(Bearing.S).setWall(Bearing.E);
    //ADD NEIGHBOURS
    eastGrid.add(Sector00, new Point(0, 0));
    eastGrid.add(Sector10, new Point(1, 0));
    eastGrid.add(Sector20, new Point(2, 0));
    eastGrid.add(Sector01, new Point(0, 1));
    eastGrid.add(Sector11, new Point(1, 1));
    eastGrid.add(Sector21, new Point(2, 1));
    eastGrid.add(Sector02, new Point(0, 2));
    eastGrid.add(Sector12, new Point(1, 2));
    eastGrid.add(Sector22, new Point(2, 2));
    eastGrid.add(Sector03, new Point(0, 3));
    eastGrid.add(Sector13, new Point(1, 3));
    eastGrid.add(Sector23, new Point(2, 3));
    //Barcode
    eastGrid.add(BarcodeAgent.getBarcodeAgent(13), new Point(0, 2), Bearing.S);
    eastGrid.add(BarcodeAgent.getBarcodeAgent(19), new Point(1, 1), Bearing.W);

    return eastGrid;
  }

  public static Grid createMergedGrid() {
    Sector Sector0_2 = createSector().setWall(Bearing.S);
    Sector Sector1_2 = createSector().setWall(Bearing.S);
    Sector Sector2_2 = createSector();
    Sector Sector0_1 = createSector().setWall(Bearing.N).setWall(Bearing.E);
    Sector Sector1_1 = createSector().setWall(Bearing.N).setWall(Bearing.W);
    Sector Sector2_1 = createSector().clearWall(Bearing.S);
    Sector Sector00 = createSector().setWall(Bearing.W);
    Sector Sector10 = createSector();
    Sector Sector20 = createSector().clearWall(Bearing.N).clearWall(Bearing.E).clearWall(Bearing.S);
    Sector Sector01 = createSector().setWall(Bearing.W).setWall(Bearing.E);
    Sector Sector11 = createSector().setWall(Bearing.W).setWall(Bearing.S);
    Sector Sector21 = createSector().setWall(Bearing.E).clearWall(Bearing.N);
    Sector Sector02 = createSector().setWall(Bearing.W).setWall(Bearing.S);
    Sector Sector12 = createSector().setWall(Bearing.S).setWall(Bearing.N).setWall(Bearing.E);
    Sector Sector22 = createSector().setWall(Bearing.S).setWall(Bearing.E).setWall(Bearing.W);
    //ADD NEIGHBOURS
    LinkedGrid mergedGrid = new LinkedGrid();
    mergedGrid.add(Sector0_2, new Point(0, -2));
    mergedGrid.add(Sector1_2, new Point(1, -2));
    mergedGrid.add(Sector2_2, new Point(2, -2));
    mergedGrid.add(Sector0_1, new Point(0, -1));
    mergedGrid.add(Sector1_1, new Point(1, -1));
    mergedGrid.add(Sector2_1, new Point(2, -1));
    mergedGrid.add(Sector00, new Point(0, 0));
    mergedGrid.add(Sector10, new Point(1, 0));
    mergedGrid.add(Sector20, new Point(2, 0));
    mergedGrid.add(Sector01, new Point(0, 1));
    mergedGrid.add(Sector11, new Point(1, 1));
    mergedGrid.add(Sector21, new Point(2, 1));
    mergedGrid.add(Sector02, new Point(0, 2));
    mergedGrid.add(Sector12, new Point(1, 2));
    mergedGrid.add(Sector22, new Point(2, 2));

    //Barcode
    mergedGrid.add(BarcodeAgent.getBarcodeAgent(19), new Point(1, 0), Bearing.N);   
    mergedGrid.add(BarcodeAgent.getBarcodeAgent(13), new Point(0, -1), Bearing.W);

    return mergedGrid;
  }

  public static Grid createMergedGrid2() {

    Sector Sector_1_1 = createSector().setWall(Bearing.N).setWall(Bearing.W);
    Sector Sector_10 = createSector().setWall(Bearing.W);
    Sector Sector_11 = createSector().setWall(Bearing.W).setWall(Bearing.S).clearWall(Bearing.E);
    Sector Sector0_2 = createSector().setWall(Bearing.S);
    Sector Sector1_2 = createSector().setWall(Bearing.S);
    Sector Sector2_2 = createSector();
    Sector Sector0_1 = createSector().setWall(Bearing.N).setWall(Bearing.E);
    Sector Sector1_1 = createSector().setWall(Bearing.N).setWall(Bearing.W);
    Sector Sector2_1 = createSector().setNoWall(Bearing.S);
    Sector Sector00 = createSector().clearWall(Bearing.W);
    Sector Sector10 = createSector();
    Sector Sector20 = createSector().setNoWall(Bearing.N).setNoWall(Bearing.E).setWall(Bearing.S);
    Sector Sector01 = createSector().clearWall(Bearing.W).setWall(Bearing.E);
    Sector Sector11 = createSector().setWall(Bearing.W).setWall(Bearing.S);
    Sector Sector21 = createSector().setWall(Bearing.E).setWall(Bearing.N);
    Sector Sector02 = createSector().setWall(Bearing.W).setWall(Bearing.S);
    Sector Sector12 = createSector().setWall(Bearing.S).setWall(Bearing.N).setWall(Bearing.E);
    Sector Sector22 = createSector().setWall(Bearing.S).setWall(Bearing.E).setWall(Bearing.W);
    //ADD NEIGHBOURS
    LinkedGrid mergedGrid = new LinkedGrid();
    mergedGrid.add(Sector_1_1, new Point(-1, -1));
    mergedGrid.add(Sector_10, new Point(-1, 0));
    mergedGrid.add(Sector_11, new Point(-1, 1));
    mergedGrid.add(Sector0_2, new Point(0, -2));
    mergedGrid.add(Sector1_2, new Point(1, -2));
    mergedGrid.add(Sector2_2, new Point(2, -2));
    mergedGrid.add(Sector0_1, new Point(0, -1));
    mergedGrid.add(Sector1_1, new Point(1, -1));
    mergedGrid.add(Sector2_1, new Point(2, -1));
    mergedGrid.add(Sector00, new Point(0, 0));
    mergedGrid.add(Sector10, new Point(1, 0));
    mergedGrid.add(Sector20, new Point(2, 0));
    mergedGrid.add(Sector01, new Point(0, 1));
    mergedGrid.add(Sector11, new Point(1, 1));
    mergedGrid.add(Sector21, new Point(2, 1));
    mergedGrid.add(Sector02, new Point(0, 2));
    mergedGrid.add(Sector12, new Point(1, 2));
    mergedGrid.add(Sector22, new Point(2, 2));

    //Barcode
    mergedGrid.add(BarcodeAgent.getBarcodeAgent(19), new Point(1, 0), Bearing.N);
    mergedGrid.add(BarcodeAgent.getBarcodeAgent(13), new Point(0, -1), Bearing.W);
    return mergedGrid;
  }

  private static Sector createSector() {
    return new LinkedSector().setNoWall(Bearing.E).setNoWall(Bearing.S).setNoWall(Bearing.W).setNoWall(Bearing.N);
  }
}
 