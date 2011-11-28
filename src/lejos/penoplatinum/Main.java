package penoplatinum;

import lejos.nxt.Button;
import penoplatinum.simulator.SonarNavigator;

public class Main {

    public static void main(String[] args) throws Exception {
        RobotRunner.Run(new Runnable() {

            public void run() {
                try {
                    Utils.Log("Started!");
                    
                    AngieEventLoop loop = new AngieEventLoop();
                    loop.useNavigator(new SonarNavigator());

                    loop.runEventLoop();
                    
                    // SensorDemo.main(null);
                    //BarcodeDemo.main(null);

                    /*RobotBluetoothConnection conn = new RobotBluetoothConnection();
                    conn.initializeConnection();
                    LineFollowerFlorian abc = new LineFollowerFlorian(conn);
                    abc.ActionLineFollower();*/
//                    MuurVolgerTest.testPerpendicular();
                    //RobotBluetoothTest test = new RobotBluetoothTest();
                    //SonarTest test = new SonarTest();
                    //test.testBluetoothLogging();
                    //test.distanceTest(new UltrasonicSensor(SensorPort.S3));
                    //SonarTest.testGetDistanceDuration(new UltrasonicSensor(SensorPort.S3));
                    //test.calibrateSonar(new UltrasonicSensor(SensorPort.S3));
                    //test.orientSonarHead(new UltrasonicSensor(SensorPort.S3), Motor.A);
                } catch (Exception ex) {
                    Utils.Log("AAAAAAAh!");
                }
            }
        });

    }
}
