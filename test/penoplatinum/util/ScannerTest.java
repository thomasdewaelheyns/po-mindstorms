package penoplatinum.util;

/**
 * ScannerTest
 * 
 * Tests Scanner class
 * 
 * @author: Team Platinum
 */

import junit.framework.*; 


public class ScannerTest extends TestCase {

  public ScannerTest(String name) { 
    super(name);
  }

  public void testHasNext() {
    Scanner scanner = this.createScanner("test1 test2 test3");
    
    assertTrue(scanner.hasNext());
    scanner.next();
    assertTrue(scanner.hasNext());
    scanner.next();
    assertTrue(scanner.hasNext());
    scanner.next();
    assertFalse(scanner.hasNext());
  }
  
  public void testNextWithSpaces() {
    Scanner scanner = this.createScanner("test1 test2 test3");
    
    assertEquals( "test1", scanner.next() );
    assertEquals( "test2", scanner.next() );
    assertEquals( "test3", scanner.next() );
  }

  public void testNextWithColons() {
    Scanner scanner = this.createScanner("test1,test2,test3");
    
    assertEquals( "test1", scanner.next() );
    assertEquals( "test2", scanner.next() );
    assertEquals( "test3", scanner.next() );
  }

  public void testNextWithMixedSpacesAndColons() {
    Scanner scanner = this.createScanner("test1 test2,test3");
    
    assertEquals( "test1", scanner.next() );
    assertEquals( "test2", scanner.next() );
    assertEquals( "test3", scanner.next() );
  }

  public void testNextInt() {
    Scanner scanner = this.createScanner("1 2,3");
    
    assertEquals( 1, scanner.nextInt() );
    assertEquals( 2, scanner.nextInt() );
    assertEquals( 3, scanner.nextInt() );
  }
  
  private Scanner createScanner(String string) {
    return new Scanner(string);
  }
}
