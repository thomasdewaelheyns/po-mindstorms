package PenoPlatinum;
import lejos.nxt.*;
import java.io.*;


public class MainThomas {

	public static void main(String [] args) {
		selectAngles(3);
	}

	static void selectAngles(int angles){
                LCD.clear();
		LCD.drawString("Select number of",0,0);
		LCD.drawString("angles:",0,1);
		if((angles<100)){LCD.drawString(""+angles,8,4);}
		else {LCD.drawString(""+angles,7,4);};
		while (true) {
		  if (Button.ENTER.isPressed()){
                      Utils.Sleep(500);
                      selectLength(angles,500);
                  }
		  if (Button.ESCAPE.isPressed()) {
                      Utils.Sleep(500);
                      System.exit(0);
                  }
		  if (Button.LEFT.isPressed()){ 
                      Utils.Sleep(500);
                      if((angles-1)>=3){selectAngles((angles-1));}
                      else { selectAngles(angles);};
                  };
		  if (Button.RIGHT.isPressed()){
                      Utils.Sleep(500);
                      selectAngles((angles+1));
                  }
		}
	}
	
	static void selectLength(int angles, int length){
                LCD.clear();
		LCD.drawString("Select the",0,0);
		LCD.drawString("length:",0,1);
		if((length<100)){LCD.drawString(""+length,8,4);}
		else {LCD.drawString(""+length,7,4);};
		while (true) {
		  if (Button.ENTER.isPressed()){
                      Utils.Sleep(500);
                      VeelhoekAction veelhoek = new VeelhoekAction();
                      veelhoek.veelhoekRotate((((double)length)/1000), angles);
		  };
		  if (Button.ESCAPE.isPressed()){
                      Utils.Sleep(500);
                      System.exit(0);
                  }
		  if (Button.LEFT.isPressed()){
                      Utils.Sleep(500);
                      if((length-10)>=10){selectLength(angles,(length-10));}
                      else { selectLength(angles,length);};
                  };
		  if (Button.RIGHT.isPressed()){
                      Utils.Sleep(500);
                      selectLength(angles,(length+10));
                  }
		}
	
	
	}


}