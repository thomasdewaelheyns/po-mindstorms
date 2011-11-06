/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.demo;

import penoplatinum.veelhoek.Veelhoek;
import penoplatinum.movement.RotationMovement;

/**
 *
 * @author MHGameWork
 */
public class VeelhoekDemo {

    public static void main(String[] args) {
        Veelhoek v = new Veelhoek(new RotationMovement());
        v.run();
    }
}
