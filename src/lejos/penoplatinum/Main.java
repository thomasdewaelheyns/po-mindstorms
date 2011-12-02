package penoplatinum;

import penoplatinum.navigators.BehaviourNavigator;
import penoplatinum.navigators.SonarNavigator;
import penoplatinum.navigators.TurnVerySmall;

public class Main {

    public static void main(String[] args) throws Exception {

        final AngieEventLoop angie = new AngieEventLoop();

        BluetoothConnection connection = new BluetoothConnection();
        connection.initializeConnection();
        final PacketTransporter transporter = new PacketTransporter(connection);
        connection.RegisterTransporter(t, 123);
        final PrintStream stream = new PrintStream(transporter.getSendStream());

        Runnable communication = new Runnable() {
          public void run() {
            stream.println(angie.getState());
            transpoter.SendPacket(123);            
          }
        }
        communication.run();

        Runnable robot = new Runnable() {
          public void run() {
            Utils.Log("Started!");
            angie.useNavigator(new TurnVerySmall());
            angie.runEventLoop();
          }
        };
        robot.run();
    }
}
