import lejos.nxt.*;

public class Veelhoeken {
    public static final int WIELOMTREK=176; //mm
    public static final int WIELAFSTAND=110;//mm
    public static Motor MOTORA=Motor.A;
    public static Motor MOTORB=Motor.B;
    public static void mainVeelhoek(int l, int h){
        int lengte=l;
        int aantalHoeken=h;
        int rotatiesRijden=berekenRijden(lengte);
        int rotatiesHoeken=berekenHoeken(aantalHoeken);
        System.out.println(rotatiesRijden);System.out.println(rotatiesRijden);
        System.out.println(rotatiesHoeken);
        //rijden(rotatiesRijden);
        hoek(rotatiesHoeken);
        for(int i=0;i<aantalHoeken-1;i++){
            //hoek(rotatiesHoeken);
            //rijden(rotatiesRijden);
        }
    }

    public static void rijden(int dist){
        MOTORA.rotate(dist, true);
        MOTORB.rotate(dist, false);
    }
    public static void hoek(int angle){
        MOTORA.rotate(angle/2, true);
        MOTORB.rotate(-angle/2, false);
    }

    public static int berekenRijden(int lengte){
        return lengte*360/WIELOMTREK;
    }

    public static int berekenHoeken(int hoeken){
        double hoek=(hoeken-2)*180.0/hoeken;
        double rad=hoek/360*2*Math.PI;
        double moveLength=rad*WIELAFSTAND;
        return (int) (moveLength*360/WIELOMTREK);
    }






}
