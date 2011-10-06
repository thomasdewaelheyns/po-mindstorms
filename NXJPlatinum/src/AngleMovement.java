
import lejos.nxt.Motor;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author MHGameWork
 */
public class AngleMovement implements IMovement{

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Hello");
        //AngleMovement a = new AngleMovement();
        //a.Move();
        //Thread.sleep(1000);
        
    }
    @Override
    public void Move() {
        Motor.B.setSpeed(120);
        Motor.B.forward();
    }
    
}
