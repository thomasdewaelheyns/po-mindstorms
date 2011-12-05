package penoplatinum;

import java.io.PrintStream;
import penoplatinum.bluetooth.PacketTransporter;
import penoplatinum.bluetooth.RobotBluetoothConnection;
import penoplatinum.navigators.BehaviourNavigator;

public class Main {
    
    public static void main(String[] args) throws Exception {
        
        final AngieEventLoop angie = new AngieEventLoop();
        
        initializeAgent(angie);
        
        Runnable robot = new Runnable() {
            
            public void run() {
                Utils.Log("Started!");
                angie.useNavigator(new BehaviourNavigator());
                angie.runEventLoop();
            }
        };
        
        robot.run();
    }
    
    private static void initializeAgent(final AngieEventLoop angie) {
        RobotBluetoothConnection connection = new RobotBluetoothConnection();
        connection.initializeConnection();
//        Utils.EnableRemoteLogging(connection);
        final PacketTransporter transporter = new PacketTransporter(connection);
        connection.RegisterTransporter(transporter, 123);
        final PrintStream stream = new PrintStream(transporter.getSendStream());
        
        
        Runnable communication = new Runnable() {
            
            public void run() {
                try {
                    while (true) {
                        String state = angie.fetchState();
                        stream.println(state);
                        
                        transporter.SendPacket(123);
                        Utils.Sleep(30);
                    }
                } catch (Exception e) {
                    Utils.Log("Comm crashed!");
                }
            }
        };
        Thread t = new Thread(communication);
        t.start();
        
    }
}
