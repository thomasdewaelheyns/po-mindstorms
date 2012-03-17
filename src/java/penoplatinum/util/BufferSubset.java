package penoplatinum.util;

public class BufferSubset {
    Buffer original;
    int start;
    int end;
    int origMaxSize;

    public BufferSubset(Buffer original, int start, int end, int maxLength) {
        this.original = original;
        this.start = start;
        this.end = end;
        this.origMaxSize = maxLength;
    }
    
    public int get(int position){
        return original.getRaw(start+(position%(end-start)));
    }
    
    public int size(){
        return (end-start+origMaxSize)%origMaxSize;
    }
    
}
