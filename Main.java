
import lejos.nxt.*;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Main {

    static final Motor MotorLeft = Motor.B;
    static final Motor MotorRight = Motor.C;

    public static void main(String[] args) {
        Veelhoeken.mainVeelhoek(500,5);
        //veelhoek(3 , 1000);
        /*System.out.println("Bluetooth");
        BTConnection conn = Bluetooth.waitForConnection();
        DataInputStream str = conn.openDataInputStream();
        DataOutputStream stro = conn.openDataOutputStream();
        System.out.println("Connected");
        boolean cont = true;
        while (cont) {
            try {
                switch (str.readByte()) {
                    case 5:
                        stro.write(123);
                    case 4:
                        veelhoek(3, 1000);
                        break;
                    case 3:
                        cont = false;
                        break;
                    case 2:
                        int l = str.readByte() * 2;
                        MotorLeft.setSpeed(l);
                        MotorLeft.forward();
                        break;
                    case 1:
                        int r = str.readByte() * 2;
                        MotorRight.setSpeed(r);
                        MotorRight.forward();
                        break;
                    case 0:
                        byte ml = str.readByte();
                        byte mr = str.readByte();
                        Sound.playTone(ml * 10, mr * 10);
                        break;
                }
            } catch (Exception e) {
                conn = Bluetooth.waitForConnection();
                str = conn.openDataInputStream();
                stro = conn.openDataOutputStream();
            }
        */
    }

    static void veelhoek(int number, int time) {
        int angle = 180 * (number - 2) / number;
        for (int i = 0; i < number; i++) {
            forward(time);
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
            turn(angle);
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        }
    }

    static void forward(int time) {
        int speed = 720;
        MotorLeft.setSpeed(speed);
        MotorRight.setSpeed(speed);
        MotorLeft.forward();
        MotorRight.forward();
        try {
            Thread.sleep(time);
        } catch (Exception e) {
        }
    }

    static void turn(int angle) {
        int speed = 360;
        int sleep = 1500 * angle / 360;
        MotorLeft.setSpeed(speed);
        MotorRight.setSpeed(speed);
        MotorLeft.forward();
        MotorRight.backward();
        try {
            Thread.sleep(sleep);
        } catch (Exception e) {
        }
    }
}