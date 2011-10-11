import lejos.nxt.*;

public class Main {

    public static void main(String[] args){
        selectVeelhoek();
    }

    static int hoeken=3;
    static int distance=200;
    static int pos=0;

    public static void selectVeelhoek(){
        int[] p=new int[]{2,5};
        while(true){
            LCD.clear();
            LCD.drawString("Select number of",0,0);
            LCD.drawString("angles:",0,1);
            LCD.drawString(""+hoeken,2,2);
            LCD.drawString("Select distance",0,4);
            LCD.drawString(""+distance, 2, 5);
            LCD.drawString("*", 0, p[pos]);
            int button = Button.waitForPress();
            if(pos==0){
                switch(button){
                    case Button.ID_LEFT:
                        hoeken = Math.max(3,hoeken-1);
                        break;
                    case Button.ID_RIGHT:
                        hoeken = Math.min(20, hoeken+1);
                        break;
                    case Button.ID_ENTER:
                        pos++;
                        break;
                    case Button.ID_ESCAPE:
                        return;
                }
            } else {
                switch(button){
                    case Button.ID_LEFT:
                        distance = Math.max(10, distance-1);
                        break;
                    case Button.ID_RIGHT:
                        distance = Math.min(1000, distance+1);
                        break;
                    case Button.ID_ENTER:
                        Veelhoek.veelhoekRotate(distance/100.0, hoeken);
                        break;
                    case Button.ID_ESCAPE:
                        pos--;
                        break;
                }
            }
        }
    }

}
