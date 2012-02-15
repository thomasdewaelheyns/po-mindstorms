package penoplatinum;

import java.io.PrintStream;
import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.IRSeeker;
import penoplatinum.bluetooth.PacketTransporter;
import penoplatinum.bluetooth.RobotBluetoothConnection;
import penoplatinum.navigators.BehaviourNavigator;

public class Main {

    public static void main(String[] args) throws Exception {


        RobotBluetoothConnection conn = new RobotBluetoothConnection();
        conn.initializeConnection();

        Utils.EnableRemoteLogging(conn);

        IRSeeker seeker = new IRSeeker(SensorPort.S3);


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
            return;
        }

        m.rotateTo(startAngle, false);

    }

    private static boolean startMeasurement(IRSeeker seeker, int[] angles, Motor m, int startAngle) {
        seeker.setAddress(8);
        while (!Button.ESCAPE.isPressed()) {
            while (!Button.ENTER.isPressed()) {
                Utils.Sleep(500);
                 if (Button.ESCAPE.isPressed()) {
                    return true;
                }
            }
            for (int i = 0; i < angles.length; i++) {
                
                
                int angle = angles[i];
                m.rotateTo(startAngle + angle, false);
                int dir = seeker.getDirection();
                Utils.Log(angle + ", " + dir);
                if (Button.ESCAPE.isPressed()) {
                    return true;
                }
                Utils.Sleep(1000);
            }
        }
        return false;
    }
    static byte[] buf = new byte[1];
    static IRSeeker seeker = new IRSeeker(SensorPort.S3);

    private static int readDirection() {

        boolean isAC = true;

        int register = 0;
        if (isAC) {
            register = 0x49;
        } else {
            register = 0x42;
        }


        int ret = seeker.getData(register, buf, 1);
        if (ret != 0) {
            Utils.Log("Error");
        }
        return (0xFF & buf[0]);
    }

    private static void runRobotSemester1() {
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
