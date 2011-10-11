/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package PenoPlatinum;

/**
 *
 * @author MHGameWork
 */
public class Utils {
    public static void Sleep(long milliseconds)
    {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ex) {
        }
    }
    
    
}
