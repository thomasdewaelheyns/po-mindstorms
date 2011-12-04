package penoplatinum;

import java.io.PrintStream;
import lejos.util.Delay;
import penoplatinum.bluetooth.PacketTransporter;
import penoplatinum.bluetooth.RobotBluetoothConnection;
import penoplatinum.navigators.BehaviourNavigator;
import penoplatinum.navigators.TurnVerySmall;

public class Main {

    public static void main(String[] args) throws Exception {

        final AngieEventLoop angie = new AngieEventLoop();

        RobotBluetoothConnection connection = new RobotBluetoothConnection();
        connection.initializeConnection();
        final PacketTransporter transporter = new PacketTransporter(connection);
        connection.RegisterTransporter(transporter, 123);
        final PrintStream stream = new PrintStream(transporter.getSendStream());


        Runnable communication = new Runnable() {

            public void run() {
                while (true) {
                    stream.println(angie.getState());
                    
                    transporter.SendPacket(123);
                    Delay.msDelay(40);
                }
            }
        };
        Thread t = new Thread(communication);
        t.start();

        Runnable robot = new Runnable() {

            public void run() {
                Utils.Log("Started!");
                angie.useNavigator(new BehaviourNavigator());
                angie.runEventLoop();
            }
        };
        robot.run();
    }
}
