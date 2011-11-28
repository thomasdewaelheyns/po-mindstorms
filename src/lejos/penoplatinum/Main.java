package penoplatinum;

import penoplatinum.navigators.DriveForwardBackward;
import penoplatinum.navigators.TurnLeftRight;
import penoplatinum.simulator.LineFollowerNavigator;

public class Main {

    public static void main(String[] args) throws Exception {
        Runnable runnable = new Runnable() {

            public void run() {
                Utils.Log("Started!");

                AngieEventLoop loop = new AngieEventLoop();
                loop.useNavigator(new LineFollowerNavigator());

                loop.runEventLoop();
            }
        };


        //RobotRunner.Run(runnable);
        runnable.run();

    }
}
