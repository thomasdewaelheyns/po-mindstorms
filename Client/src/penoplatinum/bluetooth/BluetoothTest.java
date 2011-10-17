package penoplatinum.bluetooth;

public class BluetoothTest {

    BluetoothCommunicatorPC bt = new BluetoothCommunicatorPC();

    public void testInitializeConnection() {
        bt.initializeConnection();
        System.out.println("Success!");
    }
}
