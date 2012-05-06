/**
 * GatewayRunner
 * 
 * Processes CLI parameters, constructs an Gateway and starts it.
 *
 * Author: Team Platinum
 */

import penoplatinum.gateway.Gateway;
import penoplatinum.gateway.MQ;
import penoplatinum.gateway.BluetoothConnection;


public class GatewayRunner extends BaseRunner {
  // de default GatewayRunner has a Bluetooth and a real MQ connection.
  private BluetoothConnection connection;

  // the actual Gateway
  private Gateway gateway;


  private GatewayRunner(String[] args) {
    super(args, "rmjs");
    this.setupBluetooth();
    this.setupGateway();
  }
  
  private void setupBluetooth() {
    System.out.println("--- Setting up Bluetooth");
    try {
      this.connection = new BluetoothConnection();
    } catch( Exception e ) {
      System.err.println( "FATAL: Could not initialize to bluetooth." );
      System.err.println( "       Is Bluetooth stack started?" );
      System.exit(1);
    }
    this.connection.setName(this.robotName);
  }
  
  private void setupGateway() {
    System.out.println("--- Setting up Gateway...");
    this.gateway = new Gateway().useConnection(this.connection)
                                .useQueue(this.queue)
                                .useSecret(this.secret);
  }
  
  private void start() {
    System.out.println("--- Starting Gateway...");
    this.gateway.start();
  }
  
  public static void main(String[] args) {
    new GatewayRunner(args).start();
  }
}
