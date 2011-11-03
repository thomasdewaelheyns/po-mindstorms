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
      ui.updateLight(600, UIView.BROWN);
      ui.updateBarcode(UIView.NONE, UIView.NONE);
      ui.updateSonar( 0, 8000);
      try { Thread.sleep(750); } catch(Exception e) { System.err.println(e); }

      ui.updateLight(800, UIView.WHITE);
      ui.updateBarcode(8, UIView.GO_LEFT);
      ui.updateSonar(20, 800);
      try { Thread.sleep(750); } catch(Exception e) { System.err.println(e); }

      ui.updateLight(500, UIView.BROWN );
      ui.updateBarcode(4, UIView.GO_FORWARD);
      ui.updateSonar(45, 300);
      try { Thread.sleep(750); } catch(Exception e) { System.err.println(e); }

      ui.updateLight(200, UIView.BLACK);
      ui.updateBarcode(2, UIView.GO_RIGHT);
      ui.updateSonar(90, 200 );
      try { Thread.sleep(750); } catch(Exception e) { System.err.println(e); }
    }
  }
}
