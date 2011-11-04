/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.demo;

import penoplatinum.sensor.LineFollowerFlorian;

/**
 *
 * @author MHGameWork
 */
public class LijnvolgerDemo {
    public static void main(String[] args) {
        LineFollowerFlorian follower = new LineFollowerFlorian();
        follower.ActionLineFollower();
    }
}
