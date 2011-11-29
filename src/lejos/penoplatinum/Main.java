package penoplatinum;

import penoplatinum.navigators.DriveForwardBackward;
import penoplatinum.navigators.TurnLeftRight;
import penoplatinum.navigators.LineFollowerNavigator;
import penoplatinum.navigators.SonarNavigator;

public class Main {

    public static void main(String[] args) throws Exception {
        Runnable runnable = new Runnable() {

            public void run() {
                Utils.Log("Started!");

                AngieEventLoop loop = new AngieEventLoop();
                loop.useNavigator(new SonarNavigator());

                loop.runEventLoop();
            }
        };


        //RobotRunner.Run(runnable);
        runnable.run();

    }
}
