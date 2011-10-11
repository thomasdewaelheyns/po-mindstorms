package PenoPlatinum;

import lejos.nxt.*;

public class MainRuben {
    public static void main(String[] args){
        /*int[] hoeken=new int[]{3,4,5,8};
        double[] afstand=new double[]{0.2, 0.5, 1.0};
        for(int i=0;i<hoeken.length;i++){
            for(int j=0;j<afstand.length;j++){
                System.out.println(hoeken[i]+" "+afstand[j]);
                VeelhoekAction.veelhoekRotate(afstand[j], hoeken[i]);
                Button.waitForPress();
            }
        }/**/
        double[] afstanden=new double[]{0.2, 0.2, 0.5, 0.5, 1.0, 1.0};
        RubenMovement r=new RubenMovement();
        for(int i=0;i<afstanden.length;i++){
            Button.waitForPress();
            r.MoveStraight(afstanden[i]);
        }
    }
}
