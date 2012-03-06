package penoplatinum;

import java.io.PrintStream;
import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import penoplatinum.IRSeekerV2.Mode;
import penoplatinum.bluetooth.PacketTransporter;
import penoplatinum.bluetooth.RobotBluetoothConnection;
import penoplatinum.ghost.GhostRobot;
import penoplatinum.navigators.BehaviourNavigator;
import penoplatinum.pacman.GhostNavigator;
import penoplatinum.simulator.NavigatorRobot;

public class Main {

    public static void main(String[] args) throws Exception {
        
        
        GhostRobot robot = new GhostRobot("Michiel");
        //robot.useNavigator(new LeftFollowingGhostNavigator(robot.getGhostModel()));
        robot.useNavigator(new GhostNavigator().setModel(robot.getGhostModel()));
        
        
        final AngieEventLoop angie = new AngieEventLoop(robot);
        
        RobotBluetoothConnection conn = new RobotBluetoothConnection();
        conn.initializeConnection();
        Utils.EnableRemoteLogging(conn);
        
//        initializeAgent(angie);
        Runnable runnable = new Runnable() {
            public void run() {
                Utils.Log("Started!");
                angie.runEventLoop();
            }
        };

        runnable.run();
    }

    private static boolean IRTestRuben() {
        RobotBluetoothConnection conn = new RobotBluetoothConnection();
        conn.initializeConnection();
        Utils.EnableRemoteLogging(conn);
        IRSeekerV2 seeker = new IRSeekerV2(SensorPort.S3, Mode.AC);
        Motor m = Motor.A;
        int startAngle = m.getTachoCount();
        int range = 120;
        int step = 15;
        int curr = -range;
        int[] angles = new int[(range * 2) / step + 1];
        for (int i = 0; i < angles.length; i++) {
            angles[i] = curr;
            curr += step;
        }
        System.out.println(angles[0]);
        if (startMeasurement(seeker, angles, m, startAngle)) {
            return true;
        }
        m.rotateTo(startAngle, false);
        return false;
    }

    private static boolean startMeasurement(IRSeekerV2 seeker, int[] angles, Motor m, int startAngle) {
        int count = 0;
        while (!Button.ESCAPE.isPressed()) {
            for (int i = 0; i < angles.length; i++) {
                m.rotateTo(startAngle + angles[i], false);
                int dir = seeker.getDirection();
                String str = count + "," + dir;
                for (int j = 1; j < 6; j++) {
                    str += "," + seeker.getSensorValue(j);
                }
                Utils.Log(str);
                count++;
                Utils.Sleep(1000);
            }
        }
        return false;
    }
    static byte[] buf = new byte[1];

    private static void runRobotSemester1() {
        
        NavigatorRobot nav = new NavigatorRobot();
        nav.useNavigator(new BehaviourNavigator());
        
        final AngieEventLoop angie = new AngieEventLoop(nav);

        initializeAgent(angie);

        Runnable robot = new Runnable() {

            public void run() {
                Utils.Log("Started!");
                angie.runEventLoop();
            }
        };

        robot.run();
    }

    private static void initializeAgent(final AngieEventLoop angie) {
     
        RobotBluetoothConnection connection = new RobotBluetoothConnection();
        connection.initializeConnection();
        Utils.EnableRemoteLogging(connection);
     
           if (0 == 0) {
            return;
        }
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