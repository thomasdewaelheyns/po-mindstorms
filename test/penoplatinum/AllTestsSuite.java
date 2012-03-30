/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import penoplatinum.grid.LinkedSectorTest;
import penoplatinum.util.BearingTest;
import penoplatinum.util.ColorTest;
import penoplatinum.util.PointTest;
import penoplatinum.util.RotationTest;
import penoplatinum.util.ScannerTest;
import penoplatinum.util.TransformationTRTTest;

/**
 *
 * @author MHGameWork
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({LinkedSectorTest.class, BearingTest.class, ColorTest.class, PointTest.class, RotationTest.class, ScannerTest.class, TransformationTRTTest.class})
public class AllTestsSuite {

  @BeforeClass
  public static void setUpClass() throws Exception {
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
  }

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }
}
