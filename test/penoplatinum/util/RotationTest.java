package penoplatinum.util;

/**
 * UtilTest
 * 
 * Tests Rotation enumeration
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 


public class RotationTest extends TestCase {

  public RotationTest(String name) { 
    super(name);
  }

  public void testRotationAdditionsToRotationNONE() {
    Rotation rotation = Rotation.NONE;
    
    assertEquals("L90 rotation on Rotation.NONE doesn't result in L90.",
                 Rotation.L90, rotation.add(Rotation.L90) );
    assertEquals("L180 rotation on Rotation.NONE doesn't result in L180.",
                 Rotation.L180, rotation.add(Rotation.L180) );
    assertEquals("L270 rotation on Rotation.NONE doesn't result in L270.",
                 Rotation.L270, rotation.add(Rotation.L270) );
    assertEquals("L360 rotation on Rotation.NONE doesn't result in NONE.",
                 Rotation.NONE, rotation.add(Rotation.L360) );

    assertEquals("R90 rotation on Rotation.NONE doesn't result in R90.",
                 Rotation.R90, rotation.add(Rotation.R90) );
    assertEquals("R180 rotation on Rotation.NONE doesn't result in L180.",
                 Rotation.R180, rotation.add(Rotation.R180) );
    assertEquals("R270 rotation on Rotation.NONE doesn't result in L270.",
                 Rotation.R270, rotation.add(Rotation.R270) );
    assertEquals("R360 rotation on Rotation.NONE doesn't result in NONE.",
                 Rotation.NONE, rotation.add(Rotation.R360) );
  }

  public void testRotationAdditionsToRotationL90() {
    Rotation rotation = Rotation.L90;
    
    assertEquals("L90 rotation on Rotation.L90 doesn't result in L180.",
                 Rotation.L180, rotation.add(Rotation.L90) );
    assertEquals("L180 rotation on Rotation.L90 doesn't result in L270.",
                 Rotation.L270, rotation.add(Rotation.L180) );
    assertEquals("L270 rotation on Rotation.L90 doesn't result in NONE.",
                 Rotation.NONE, rotation.add(Rotation.L270) );
    assertEquals("L360 rotation on Rotation.L90 doesn't result in L90.",
                 Rotation.L90, rotation.add(Rotation.L360) );

    assertEquals("R90 rotation on Rotation.L90 doesn't result in NONE.",
                 Rotation.NONE, rotation.add(Rotation.R90) );
    assertEquals("R180 rotation on Rotation.L90 doesn't result in R90.",
                 Rotation.R90, rotation.add(Rotation.R180) );
    assertEquals("L270 rotation on Rotation.L90 doesn't result in L180.",
                 Rotation.R180, rotation.add(Rotation.R270) );
    assertEquals("R360 rotation on Rotation.L90 doesn't result in L90.",
                 Rotation.L90, rotation.add(Rotation.R360) );
  }

  public void testRotationAdditionsToRotationL180() {
    Rotation rotation = Rotation.L180;
    
    assertEquals("L90 rotation on Rotation.L180 doesn't result in L270.",
                 Rotation.L270, rotation.add(Rotation.L90) );
    assertEquals("L180 rotation on Rotation.L180 doesn't result in NONE.",
                 Rotation.NONE, rotation.add(Rotation.L180) );
    assertEquals("L270 rotation on Rotation.L180 doesn't result in L90.",
                 Rotation.L90, rotation.add(Rotation.L270) );
    assertEquals("L360 rotation on Rotation.L180 doesn't result in L180.",
                 Rotation.L180, rotation.add(Rotation.L360) );

    assertEquals("R90 rotation on Rotation.L180 doesn't result in L90.",
                 Rotation.L90, rotation.add(Rotation.R90) );
    assertEquals("R180 rotation on Rotation.L180 doesn't result in NONE",
                 Rotation.NONE, rotation.add(Rotation.R180) );
    assertEquals("R270 rotation on Rotation.L180 doesn't result in R90.",
                 Rotation.R90, rotation.add(Rotation.R270) );
    assertEquals("R360 rotation on Rotation.L180 doesn't result in L180.",
                 Rotation.L180, rotation.add(Rotation.R360) );
  }

  public void testRotationAdditionsToRotationL270() {
    Rotation rotation = Rotation.L270;
    
    assertEquals("L90 rotation on Rotation.L270 doesn't result in NONE.",
                 Rotation.NONE, rotation.add(Rotation.L90) );
    assertEquals("L180 rotation on Rotation.L270 doesn't result in L90.",
                 Rotation.L90, rotation.add(Rotation.L180) );
    assertEquals("L270 rotation on Rotation.L270 doesn't result in L180.",
                 Rotation.L180, rotation.add(Rotation.L270) );
    assertEquals("L360 rotation on Rotation.L270 doesn't result in L270.",
                 Rotation.L270, rotation.add(Rotation.L360) );

    assertEquals("R90 rotation on Rotation.L270 doesn't result in L180.",
                 Rotation.L180, rotation.add(Rotation.R90) );
    assertEquals("R180 rotation on Rotation.L270 doesn't result in L90",
                 Rotation.L90, rotation.add(Rotation.R180) );
    assertEquals("R270 rotation on Rotation.L270 doesn't result in NONE.",
                 Rotation.NONE, rotation.add(Rotation.R270) );
    assertEquals("R360 rotation on Rotation.L270 doesn't result in L270.",
                 Rotation.L270, rotation.add(Rotation.R360) );
  }
  
  public void testRotationAdditionsToRotationL360() {
    Rotation rotation = Rotation.L360;
    
    assertEquals("L90 rotation on Rotation.L360 doesn't result in L90.",
                 Rotation.L90, rotation.add(Rotation.L90) );
    assertEquals("L180 rotation on Rotation.L360 doesn't result in L180.",
                 Rotation.L180, rotation.add(Rotation.L180) );
    assertEquals("L270 rotation on Rotation.L360 doesn't result in L270.",
                 Rotation.L270, rotation.add(Rotation.L270) );
    assertEquals("L360 rotation on Rotation.L360 doesn't result in NONE.",
                 Rotation.NONE, rotation.add(Rotation.L360) );

    assertEquals("R90 rotation on Rotation.L360 doesn't result in R90.",
                 Rotation.R90, rotation.add(Rotation.R90) );
    assertEquals("R180 rotation on Rotation.L360 doesn't result in L180.",
                 Rotation.R180, rotation.add(Rotation.R180) );
    assertEquals("R270 rotation on Rotation.L360 doesn't result in L270.",
                 Rotation.R270, rotation.add(Rotation.R270) );
    assertEquals("R360 rotation on Rotation.L360 doesn't result in NONE.",
                 Rotation.NONE, rotation.add(Rotation.R360) );
  }

  // R
  
  public void testRotationAdditionsToRotationR90() {
    Rotation rotation = Rotation.R90;
    
    assertEquals("L90 rotation on Rotation.R90 doesn't result in NONE.",
                 Rotation.NONE, rotation.add(Rotation.L90) );
    assertEquals("L180 rotation on Rotation.R90 doesn't result in L90.",
                 Rotation.L90, rotation.add(Rotation.L180) );
    assertEquals("L270 rotation on Rotation.R90 doesn't result in L180.",
                 Rotation.L180, rotation.add(Rotation.L270) );
    assertEquals("L360 rotation on Rotation.R90 doesn't result in R90.",
                 Rotation.R90, rotation.add(Rotation.L360) );

    assertEquals("R90 rotation on Rotation.R90 doesn't result in R180.",
                 Rotation.R180, rotation.add(Rotation.R90) );
    assertEquals("R180 rotation on Rotation.R90 doesn't result in R270.",
                 Rotation.R270, rotation.add(Rotation.R180) );
    assertEquals("L270 rotation on Rotation.R90 doesn't result in NONE.",
                 Rotation.NONE, rotation.add(Rotation.R270) );
    assertEquals("R360 rotation on Rotation.R90 doesn't result in R90.",
                 Rotation.R90, rotation.add(Rotation.R360) );
  }

  public void testRotationAdditionsToRotationR180() {
    Rotation rotation = Rotation.R180;
    
    assertEquals("L90 rotation on Rotation.R180 doesn't result in R90.",
                 Rotation.R90, rotation.add(Rotation.L90) );
    assertEquals("L180 rotation on Rotation.R180 doesn't result in NONE.",
                 Rotation.NONE, rotation.add(Rotation.L180) );
    assertEquals("L270 rotation on Rotation.R180 doesn't result in L90.",
                 Rotation.L90, rotation.add(Rotation.L270) );
    assertEquals("L360 rotation on Rotation.R180 doesn't result in R180.",
                 Rotation.R180, rotation.add(Rotation.L360) );

    assertEquals("R90 rotation on Rotation.R180 doesn't result in R270.",
                 Rotation.R270, rotation.add(Rotation.R90) );
    assertEquals("R180 rotation on Rotation.R180 doesn't result in NONE",
                 Rotation.NONE, rotation.add(Rotation.R180) );
    assertEquals("R270 rotation on Rotation.R180 doesn't result in R90.",
                 Rotation.R90, rotation.add(Rotation.R270) );
    assertEquals("R360 rotation on Rotation.R180 doesn't result in R180.",
                 Rotation.R180, rotation.add(Rotation.R360) );
  }

  public void testRotationAdditionsToRotationR270() {
    Rotation rotation = Rotation.R270;
    
    assertEquals("L90 rotation on Rotation.R270 doesn't result in R180.",
                 Rotation.R180, rotation.add(Rotation.L90) );
    assertEquals("L180 rotation on Rotation.R270 doesn't result in R90.",
                 Rotation.R90, rotation.add(Rotation.L180) );
    assertEquals("L270 rotation on Rotation.R270 doesn't result in NONE.",
                 Rotation.NONE, rotation.add(Rotation.L270) );
    assertEquals("L360 rotation on Rotation.R270 doesn't result in R270.",
                 Rotation.R270, rotation.add(Rotation.L360) );

    assertEquals("R90 rotation on Rotation.R270 doesn't result in NONE.",
                 Rotation.NONE, rotation.add(Rotation.R90) );
    assertEquals("R180 rotation on Rotation.R270 doesn't result in R90",
                 Rotation.R90, rotation.add(Rotation.R180) );
    assertEquals("R270 rotation on Rotation.R270 doesn't result in R180.",
                 Rotation.R180, rotation.add(Rotation.R270) );
    assertEquals("R360 rotation on Rotation.R270 doesn't result in R270.",
                 Rotation.R270, rotation.add(Rotation.R360) );
  }
  
  public void testRotationAdditionsToRotationR360() {
    Rotation rotation = Rotation.R360;
    
    assertEquals("L90 rotation on Rotation.R360 doesn't result in L90.",
                 Rotation.L90, rotation.add(Rotation.L90) );
    assertEquals("L180 rotation on Rotation.R360 doesn't result in L180.",
                 Rotation.L180, rotation.add(Rotation.L180) );
    assertEquals("L270 rotation on Rotation.R360 doesn't result in L270.",
                 Rotation.L270, rotation.add(Rotation.L270) );
    assertEquals("L360 rotation on Rotation.R360 doesn't result in NONE.",
                 Rotation.NONE, rotation.add(Rotation.L360) );

    assertEquals("R90 rotation on Rotation.R360 doesn't result in R90.",
                 Rotation.R90, rotation.add(Rotation.R90) );
    assertEquals("R180 rotation on Rotation.R360 doesn't result in L180.",
                 Rotation.R180, rotation.add(Rotation.R180) );
    assertEquals("R270 rotation on Rotation.R360 doesn't result in L270.",
                 Rotation.R270, rotation.add(Rotation.R270) );
    assertEquals("R360 rotation on Rotation.R360 doesn't result in NONE.",
                 Rotation.NONE, rotation.add(Rotation.R360) );
  }

}
