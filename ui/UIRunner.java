/**
 * UIRunner
 * 
 * Author: Team Platinum
 */

class UIRunner {

  public static void main(String[] args) {
    UIView ui = new SwingUIView();
    // simulate event loop
    while(true) {
      ui.update( 600, 0, UIView.BROWN, UIView.GO_FORWARD);
      try { Thread.sleep(2000); } catch(Exception e) { System.err.println(e); }

      ui.update(1000, 4, UIView.WHITE, UIView.GO_LEFT);
      try { Thread.sleep(2000); } catch(Exception e) { System.err.println(e); }

      ui.update( 500, 0, UIView.BROWN, UIView.GO_FORWARD);
      try { Thread.sleep(2000); } catch(Exception e) { System.err.println(e); }

      ui.update(   3, 4, UIView.BLACK, UIView.GO_RIGHT);
      try { Thread.sleep(2000); } catch(Exception e) { System.err.println(e); }
    }
  }

}
