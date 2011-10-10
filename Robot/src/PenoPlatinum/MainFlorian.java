/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package PenoPlatinum;
import PenoPlatinum.SpeedBasedMovement;

/**
 * deze klasse dient voorlopig als testklasse om SpeeddBasedMovement uit te testen...
 * @author Florian
 */
public class MainFlorian {
    	public static void main(String [] args) {
		
	}
        static void testForwardDriving(){
        SpeedBasedMovement.forward(0.50f, 2);
        SpeedBasedMovement.wait(3000);
        SpeedBasedMovement.forward(0.20f, 5);
        SpeedBasedMovement.wait(3000);
        SpeedBasedMovement.forward(0.10f, 10);
        SpeedBasedMovement.wait(3000);
        SpeedBasedMovement.forward(0.05f, 20);
        SpeedBasedMovement.wait(3000);
        SpeedBasedMovement.forward(0.025f, 40);
        SpeedBasedMovement.wait(3000);
        SpeedBasedMovement.forward(0.01f, 100);
        SpeedBasedMovement.wait(3000);
        }
        static void testRotation(){
        SpeedBasedMovement.turn(0.10f, 180, true);
        SpeedBasedMovement.wait(3000);
        SpeedBasedMovement.turn(0.05f, 180, true);
        SpeedBasedMovement.wait(3000);
        SpeedBasedMovement.turn(0.025f, 180, true);
        SpeedBasedMovement.wait(3000);
        SpeedBasedMovement.turn(0.01f, 180, true);
        SpeedBasedMovement.wait(3000);
        SpeedBasedMovement.turn(0.10f, 180, false);
        SpeedBasedMovement.wait(3000);
        SpeedBasedMovement.turn(0.05f, 180, false);
        SpeedBasedMovement.wait(3000);
        SpeedBasedMovement.turn(0.025f, 180, false);
        SpeedBasedMovement.wait(3000);
        SpeedBasedMovement.turn(0.01f, 180, false);
        SpeedBasedMovement.wait(3000);
        }
        static void testVeelhoek(){
        SpeedBasedMovement.veelhoek(3, 2000);
                SpeedBasedMovement.wait(3000);

        SpeedBasedMovement.veelhoek(4, 2000);
                SpeedBasedMovement.wait(3000);

        SpeedBasedMovement.veelhoek(5, 2000);
                SpeedBasedMovement.wait(3000);

        SpeedBasedMovement.veelhoek(6, 2000);
                SpeedBasedMovement.wait(3000);

        }
        static void testMoveStraight(){
            SpeedBasedMovement mov = new SpeedBasedMovement();
            mov.MoveStraight(1000);
            mov.wait(500);
            mov.MoveStraight(500);
        }
        static void testTurnOnSpotCCW(){
           SpeedBasedMovement mov = new SpeedBasedMovement();
           mov.TurnOnSpotCCW(360);
           mov.wait(500);
           mov.TurnOnSpotCCW(180);
        }
        
}
