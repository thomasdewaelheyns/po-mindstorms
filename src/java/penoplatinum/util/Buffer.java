/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package penoplatinum.util;

/**
 *Buffer for the read lightValues
 */
public class Buffer {
    
    private int start = 0;
    private int startCheckpoint = -1;
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
        if(startCheckpoint == (start+1)%(maxElements+1)){
            throw new RuntimeException("Queue is full!");
        }
        queue[(start)% maxElements] = value;
        start = (start+1) % (maxElements+1);
    }


    public int get(int position) {
            return queue[(start+position-1) % maxElements];
    }
    
    public int getRaw(int position){
        return queue[(position-1)%maxElements];
    }
    
    public int getSize(){
        return this.maxElements;
    }
    
    public void setCheckPoint(){
        startCheckpoint = start;
    }
    
    //This guards for "DataOverflow", if no barcodes are found for a whole buffer.
    public void unsetCheckPoint(){
      startCheckpoint = -1;
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
    //REMOVE?
    BufferSubset getBufferSubset(){
        return new BufferSubset(this, startCheckpoint, start, maxElements);
    }
    
    public BufferSubset getBufferSubset(int beforeEnd){
        return new BufferSubset(this, startCheckpoint, (start+maxElements-beforeEnd)%maxElements, maxElements);
    }
    
    public void removeLast(){
      start = (start+maxElements-1)%maxElements;
      if(start==0){
       start = maxElements; 
      }
    }
}
