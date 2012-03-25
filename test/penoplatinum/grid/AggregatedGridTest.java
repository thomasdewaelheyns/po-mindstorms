/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.grid;

import junit.framework.Assert;
import org.junit.Test;
import penoplatinum.model.processor.MergeGridModelProcessor;
import penoplatinum.util.TransformationTRT;

/**
 * 
 * 
 * @author MHGameWork
 */
public class AggregatedGridTest {

  public void testBarcodeMap(int codeLocal, int bearingLocal, int codeRemote, int bearingRemote) {
    SimpleGrid local = (SimpleGrid) GridFactory.load("..\\..\\src\\java\\penoplatinum\\simulator\\mini\\testGRTS-local.map");
    SimpleGrid remote = (SimpleGrid) GridFactory.load("..\\..\\src\\java\\penoplatinum\\simulator\\mini\\testGRTS-remote.map");
    SimpleGrid merged = (SimpleGrid) GridFactory.load("..\\..\\src\\java\\penoplatinum\\simulator\\mini\\testGRTS-merged.map");

    
    local.getSector(1, 0).setTagCode(codeLocal);
    local.getSector(1, 0).setTagBearing(bearingLocal);
    
    remote.getSector(0, 0).setTagCode(codeRemote);
    remote.getSector(0, 0).setTagBearing(bearingRemote);
    
    merged.getSector(1, 0).setTagCode(codeLocal);
    merged.getSector(1, 0).setTagBearing(bearingLocal);
    
    AggregatedGrid grid1 = new AggregatedGrid();

    SimpleGrid.copyGridTo(grid1, local, TransformationTRT.Identity);
    SimpleGrid.copyGridTo(grid1.getGhostGrid("ghost"), remote, TransformationTRT.Identity);

    
    
    Assert.assertTrue(grid1.attemptMapBarcode(grid1.getSector(1, 0),grid1.getGhostGrid("ghost").getSector(0, 0),"ghost"));


//    local.displayOn(new SwingGridView());
//    remote.displayOn(new SwingGridView());
    merged.displayOn(new SwingGridView());
    grid1.displayOn(new SwingGridView());

    Assert.assertTrue(merged.areSectorsEqual(grid1));
  }

  @Test
  public void testMapBarcodesTranslated() {
    int tX1 = 3;
    int tY1 = 5;
//    int tX2 = -3;
//    int tY2 = -2;

    int barcodeX = 2;
    int barcodeY = 3;

    SimpleGrid original = (SimpleGrid) GridFactory.load("..\\..\\src\\java\\penoplatinum\\simulator\\mini\\corridor.map");
    original.getSector(barcodeX, barcodeY).setTagCode(42);

    AggregatedGrid grid1 = new AggregatedGrid();

    grid1.importGrid(original, TransformationTRT.Identity);
    SimpleGrid.copyGridTo(grid1.getGhostGrid("ghost"), original, new TransformationTRT().setTransformation(tX1, tY1, 0, 0, 0));


    grid1.setGhostRelativeTransformation("ghost", new TransformationTRT().setTransformation(-tX1, -tY1, 0, 0, 0));


    original.displayOn(new SwingGridView());
    grid1.displayOn(new SwingGridView());

    Assert.assertTrue(original.areSectorsEqual(grid1));


  }

  @Test
  public void testMapBarcodesRotated() {
    int tX1 = 3;
    int tY1 = 5;
//    int tX2 = -3;
//    int tY2 = -2;

    int barcodeX = 2;
    int barcodeY = 3;

    SimpleGrid original = (SimpleGrid) GridFactory.load("..\\..\\src\\java\\penoplatinum\\simulator\\mini\\corridor.map");
    original.getSector(barcodeX, barcodeY).setTagCode(42);

    AggregatedGrid grid1 = new AggregatedGrid();

    SimpleGrid.copyGridTo(grid1, original, TransformationTRT.Identity);
    SimpleGrid.copyGridTo(grid1.getGhostGrid("ghost"), original, new TransformationTRT().setTransformation(0, 0, 3, 0, 0));


    grid1.setGhostRelativeTransformation("ghost", new TransformationTRT().setTransformation(0, 0, 1, 0, 0));


    original.displayOn(new SwingGridView());
    grid1.displayOn(new SwingGridView());
    ((AggregatedSubGrid) grid1.getGhostGrid("ghost")).getDecoratedGrid().displayOn(new SwingGridView());

    Grid test = ((AggregatedSubGrid) grid1.getGhostGrid("ghost")).getDecoratedGrid();

    Assert.assertTrue(original.areSectorsEqual(grid1));


  }

  @Test
  public void testSetGhostRelativeTransformationSimple() {

    SimpleGrid local = (SimpleGrid) GridFactory.load("..\\..\\src\\java\\penoplatinum\\simulator\\mini\\testGRTS-local.map");
    SimpleGrid remote = (SimpleGrid) GridFactory.load("..\\..\\src\\java\\penoplatinum\\simulator\\mini\\testGRTS-remote.map");
    SimpleGrid merged = (SimpleGrid) GridFactory.load("..\\..\\src\\java\\penoplatinum\\simulator\\mini\\testGRTS-merged.map");

    AggregatedGrid grid1 = new AggregatedGrid();

    SimpleGrid.copyGridTo(grid1, local, TransformationTRT.Identity);
    SimpleGrid.copyGridTo(grid1.getGhostGrid("ghost"), remote, TransformationTRT.Identity);


    grid1.setGhostRelativeTransformation("ghost", new TransformationTRT().setTransformation(0, 0, 3, 1, 0));


//    local.displayOn(new SwingGridView());
//    remote.displayOn(new SwingGridView());
    merged.displayOn(new SwingGridView());
    grid1.displayOn(new SwingGridView());

    Assert.assertTrue(merged.areSectorsEqual(grid1));



  }

  @Test
  public void testMapBarcodeSimple() {

    SimpleGrid local = (SimpleGrid) GridFactory.load("..\\..\\src\\java\\penoplatinum\\simulator\\mini\\testGRTS-local.map");
    SimpleGrid remote = (SimpleGrid) GridFactory.load("..\\..\\src\\java\\penoplatinum\\simulator\\mini\\testGRTS-remote.map");
    SimpleGrid merged = (SimpleGrid) GridFactory.load("..\\..\\src\\java\\penoplatinum\\simulator\\mini\\testGRTS-merged.map");

    AggregatedGrid grid1 = new AggregatedGrid();

    SimpleGrid.copyGridTo(grid1, local, TransformationTRT.Identity);
    SimpleGrid.copyGridTo(grid1.getGhostGrid("ghost"), remote, TransformationTRT.Identity);

    
    
    Assert.assertTrue(grid1.attemptMapBarcode(grid1.getSector(1, 0),grid1.getGhostGrid("ghost").getSector(0, 0),"ghost"));


//    local.displayOn(new SwingGridView());
//    remote.displayOn(new SwingGridView());
    merged.displayOn(new SwingGridView());
    grid1.displayOn(new SwingGridView());

    Assert.assertTrue(merged.areSectorsEqual(grid1));



  }
  
  @Test
  public void testMapBarcodeInverse() {
    testBarcodeMap(19, 0, 19, 1);
    testBarcodeMap(19, 0, 50, 3);
    testBarcodeMap(50, 3, 50, 0);
    testBarcodeMap(50, 3, 19, 2);



  }
}
