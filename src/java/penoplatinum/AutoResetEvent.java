/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum;

/**
 *
 * Taken from http://stackoverflow.com/questions/1091973/javas-equivalent-to-nets-autoresetevent
 */
class AutoResetEvent {

    private final Object monitor = new Object();
    private volatile boolean open = false;

    public AutoResetEvent(boolean open) {
        this.open = open;
    }

    public void waitOne() throws InterruptedException {
        synchronized (monitor) {
            while (open == false) {
                monitor.wait();
            }
            open = false; // close for other
        }

    }

    public void set() {
        synchronized (monitor) {
            open = true;
            monitor.notify(); // open one 
        }
    }

    public void reset() {//close stop
        open = false;
    }
}
