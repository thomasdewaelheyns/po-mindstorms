package penoplatinum.movement;


/**
 * TODO: put in shared project
 * @author MHGameWork
 */
public class Utils {
    public static void Sleep(long milliseconds)
    {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ex) {
            System.out.println("InterruptException");
        }
    }
    
    public static void Log(String message)
    {
        System.out.println(message);
    }
    
}
