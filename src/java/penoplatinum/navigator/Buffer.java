/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.navigator;

/**
 *Buffer for the read lightValues
 */
public class Buffer {
    
    private int start = 0;
    private int startCheckpoint = 0;
    private int[] queue;
    private int maxElements;
    //private int defaultFiller = 60;

    public Buffer(int maxElements) {
        queue = new int[maxElements];
        this.maxElements = maxElements;
        /*for(int i = 0; i<maxElements; i++){
            //insert(defaultFiller);
            queue[i] = defaultFiller;
        }/**/ 
    }

    public void insert(int value) {
        if(startCheckpoint == (start+1)%maxElements){
            System.out.println("DataOverflow");
        }
        queue[(start)% maxElements] = value;
        start = (start +1) % maxElements;
    }


    public int get(int position) {
            return queue[(start+position) % maxElements];
    }
    
    public int getRaw(int position){
        return queue[position%maxElements];
    }
    
    public int getSize(){
        return this.maxElements;
    }
    
    public void setCheckPoint(){
        startCheckpoint = start;
    }
    
    public int getCheckPoint(){
        return startCheckpoint;
    }
    
    public int getEndPoint(){
        return start;
    }
    
    public int getCheckpointSize(){
        return (start-startCheckpoint+maxElements)%maxElements;
    }
    
    BufferSubset getBufferSubset(){
        return new BufferSubset(this, startCheckpoint, start, maxElements);
    }
}
