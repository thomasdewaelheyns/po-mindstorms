/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.demo;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import penoplatinum.movement.RotationMovement;

/**
 *
 * @author MHGameWork
 */
public class VeelhoekDemo {

    public static void main(String[] args) {
        VeelhoekDemo v = new VeelhoekDemo();
        v.runVeelhoekGUI();
    }
    private final RotationMovement movement;
    private int hoeken = 3;
    private int distance = 40;
    private int pos = 0;

    public VeelhoekDemo() {
        movement = new RotationMovement();
    }

    public void runVeelhoekGUI() {
        int[] p = new int[]{2, 5};
        while (true) {
            LCD.clear();
            LCD.drawString("Select number of", 0, 0);
            LCD.drawString("angles:", 0, 1);
            LCD.drawString("" + hoeken, 2, 2);
            LCD.drawString("Select distance", 0, 4);
            LCD.drawString("" + distance, 2, 5);
            LCD.drawString("*", 0, p[pos]);
            int button = Button.waitForPress();
            if (pos == 0) {
                switch (button) {
                    case Button.ID_LEFT:
                        hoeken = Math.max(3, hoeken - 1);
                        break;
                    case Button.ID_RIGHT:
                        hoeken = Math.min(20, hoeken + 1);
                        break;
                    case Button.ID_ENTER:
                        pos++;
                        break;
                    case Button.ID_ESCAPE:
                        return;
                }
            } else {
                switch (button) {
                    case Button.ID_LEFT:
                        distance = Math.max(10, distance - 1);
                        break;
                    case Button.ID_RIGHT:
                        distance = Math.min(1000, distance + 1);
                        break;
                    case Button.ID_ENTER:
                        driveVeelhoek(distance / 100.0, hoeken);
                        break;
                    case Button.ID_ESCAPE:
                        pos--;
                        break;
                }
            }
        }
    }

    public void driveVeelhoek(double edgeLength, int numberAngles) {
        double hoek = 360.0 / numberAngles;
        for (int i = 0; i < numberAngles; i++) {
            movement.driveDistance(edgeLength);
            movement.turnCCW(hoek);
        }
        Motor.B.stop();
        Motor.C.stop();
    }
}
