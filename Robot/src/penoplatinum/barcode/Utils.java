package penoplatinum.barcode;







public class Utils {
    public static void Sleep(long milliseconds)
    {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ex) {
            System.out.println("InterruptException");
        }
    }
}
