package PenoPlatinum;


import lejos.nxt.*;

public class TestOmwenteling {

    public static void main(String[] Args) {
        int hoek = 90;
        double lengte_as = 11;
        double lengte_wiel = 17.5;

        double omtrek = 2 * Math.PI * (lengte_as / 2);
        double afTeLeggen = hoek * (omtrek / 360);
        int aantalOmwentelingen = (int) ((afTeLeggen * 360) / lengte_wiel);

        Motor.B.setSpeed(100);
        Motor.C.setSpeed(100);
        Motor.B.rotate(aantalOmwentelingen, true);
        Motor.C.rotate(-aantalOmwentelingen, true);
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
        }
    }
}
