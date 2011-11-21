/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.navigator;

import penoplatinum.bluetooth.*;

/**
 *Buffer for the read lightValues
 */
public class BarcodeBuffer<T> {

    private int front = 0;
    private int[] queue;
    private int maxElements;

    public BarcodeBuffer(int maxElements) {
        queue = new int[maxElements];
        this.maxElements = maxElements;
    }

    public void insert(int value) {
        queue[(front)% maxElements] = value;
        front = (front +1) % maxElements;
    }


    public int get(int position) {
            return queue[(front+position) % maxElements];
    }
    
    public int getSize(){
        return this.maxElements;
    }
}
