package penoplatinum.navigator;

public class Command {
    int navigator;
    double data;
    boolean block;
    
    public Command(int navigator, double data, boolean block){
        this.navigator = navigator;
        this.data = data;
        this.block = block;
    }

    public boolean isBlocked() {
        return block;
    }

    public double getData() {
        return data;
    }

    public int getNavigator() {
        return navigator;
    }
}
