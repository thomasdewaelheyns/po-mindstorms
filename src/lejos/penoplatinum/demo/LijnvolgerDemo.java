/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.demo;

import penoplatinum.bluetooth.RobotBluetoothConnection;
import penoplatinum.sensor.LineFollowerFlorian;
import penoplatinum.sensor.WrappedLightSensor;

/**
 *
 * @author MHGameWork
 */
public class LijnvolgerDemo {
    public static void main(String[] args) {
        RobotBluetoothConnection conn = new RobotBluetoothConnection();
        LineFollowerFlorian follower = new LineFollowerFlorian(new WrappedLightSensor(conn));
        follower.ActionLineFollower();
    }
}
