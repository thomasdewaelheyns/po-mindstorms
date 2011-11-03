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
      ui.update( 600, UIView.BROWN );
      try { Thread.sleep(2000); } catch(Exception e) { System.err.println(e); }

      ui.update(1000, UIView.WHITE, 8, UIView.GO_LEFT);
      try { Thread.sleep(2000); } catch(Exception e) { System.err.println(e); }

      ui.update( 500, UIView.BROWN, 4, UIView.GO_FORWARD);
      try { Thread.sleep(2000); } catch(Exception e) { System.err.println(e); }

      ui.update(   3, UIView.BLACK, 2, UIView.GO_RIGHT);
      try { Thread.sleep(2000); } catch(Exception e) { System.err.println(e); }
    }
  }

}
