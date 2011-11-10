
import lejos.nxt.*;
import penoplatinum.Utils;

/**
 * This is a test of remote reading of sensors from the PC
 * using the iCommand equivalent classes in pccomm.jar
 * 
 * @author Lawrie Griffiths
 *
 */
public class SensorTest {
    
    public static void main(String[] args) {
        
        String[] names = FileSystem.getFileNames();
        FileSystem.stopProgram();
        
        System.out.println(FileSystem.startProgram("Michiel.nxj"));
        
        Utils.Sleep(2000);
        
        for (int i = 0; i < names.length; i++) {
            System.out.println(names[i]);
        }
        //remoteSensor();
    }
    
    private static void remoteSensor() {
        LightSensor light = new LightSensor(SensorPort.S4);
        TouchSensor touch = new TouchSensor(SensorPort.S1);
        UltrasonicSensor sonic = new UltrasonicSensor(SensorPort.S3);
        
        while (true) {
            System.out.println("light = " + light.readValue());
            System.out.println("touch = " + touch.isPressed());
            System.out.println("distance = " + sonic.getDistance());
            try {
                Thread.sleep(10);
            } catch (InterruptedException ie) {
            }
        }
    }
}
