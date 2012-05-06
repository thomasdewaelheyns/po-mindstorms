package penoplatinum.simulator.mini;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import penoplatinum.barcode.BarcodeTranslator;
import penoplatinum.grid.agent.Agent;

import penoplatinum.grid.Sector;
import penoplatinum.simulator.RobotAPI;
import penoplatinum.simulator.Bearing;


import penoplatinum.simulator.Model;

public class MiniSimulationRobotAPI implements RobotAPI {

  private Agent proxy;

  public MiniSimulationRobotAPI(Agent proxy) {
    this.proxy = proxy;
  }

  public boolean hasActorAtNeighbour(int bearing) {
    Sector neighbour = this.proxy.getSector().getNeighbour(bearing);
    if (neighbour == null) {
      return false;
    }
    if (neighbour.getAgent() == null) {
      return false;
    }

    return true;
  }

  public boolean move(double distance) {
    if (!this.proxy.canMoveForward()) {
      return false;
    }
    this.proxy.moveForward();
    return true;
  }

  public void turn(int angle) {
    int bearing = this.proxy.getBearing();
    switch (angle) {
      case -90:
        this.proxy.turnLeft();
        break;
      case 90:
        this.proxy.turnRight();
        break;
    }
  }

  public void stop() {
    // nothing to do ;-)
  }

  public int[] getSensorValues() {
    return new int[Model.SENSORVALUES_NUM];
  }

  public void setSpeed(int motor, int speed) {
    // nothing to do
  }

  public void beep() {
    System.out.println("BEEP");
  }

  // NEW
  public void sweep(int[] bearings) {
    // nothing to do... hard-coded
  }

  public boolean sweepInProgress() {
    // we have everything immediately
    return false;
  }

  public List<Integer> getSweepResult() {
    int left, front, right;

    int bearing = this.proxy.getBearing();
    Boolean wall, agent = false;
    wall = this.proxy.getSector().hasWall(Bearing.leftFrom(bearing));
    wall |= hasActorAtNeighbour(Bearing.leftFrom(bearing));
    if (wall == null) {
      throw new RuntimeException("proxy can not determine wall to left");
    }
    // WARNING left and right are reversed here, this could simply be a bug
    //  in the normal robot implementation

    right = wall ? 20 : 60;

    wall = this.proxy.getSector().hasWall(bearing);
    wall |= hasActorAtNeighbour(bearing);
    if (wall == null) {
      throw new RuntimeException("proxy can not determine wall in front");
    }
    front = wall ? 20 : 60;

    wall = this.proxy.getSector().hasWall(Bearing.rightFrom(bearing));
    wall |= hasActorAtNeighbour(Bearing.rightFrom(bearing));
    if (wall == null) {
      throw new RuntimeException("proxy can not determine wall to right");
    }
    left = wall ? 20 : 60;

    return new ArrayList<Integer>(Arrays.asList(left, front, right));
  }

  public void setReferenceAngle(float reference) {
  }

  public float getAngle(float reference) {
    return 0.f;
  }
  boolean isSweeping = false;

  public void setSweeping(boolean b) {
    isSweeping = b;
  }

  public boolean isSweeping() {
    return isSweeping;
  }

  public Agent getProxy() {
    return proxy;
  }

  public int readBarcode() {
    int code = proxy.getSector().getTagCode();
    if (code == -1) {
      return code;
    }
    if (proxy.getSector().getTagBearing() != proxy.getBearing()) {
      // maybe safety check whether the tagbearing is opposite?
      code = BarcodeTranslator.reverse(code, 6);
    }
    return code;
  }
}
