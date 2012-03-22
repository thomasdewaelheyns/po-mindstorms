package penoplatinum.gateway;

/**
 * 
 * 
 * @author Team Platinum
 */

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import penoplatinum.Config;
import penoplatinum.util.Utils;
import penoplatinum.bluetooth.CallbackPacketTransporter;
import penoplatinum.bluetooth.IConnection;
import penoplatinum.bluetooth.IPacketHandler;

public class MQMessageDispatcher {
  private CallbackPacketTransporter transporter;
  private final IConnection connection;
  private MQ mq;

  public MQMessageDispatcher(IConnection connection) {
    this.connection = connection;
  }

  public void startMQDispatcher() {
    this.mq = new MQ() {
      @Override
      protected void handleIncomingMessage(String message) {
        try {
          transporter.getSendStream().write(message.getBytes());
          transporter.SendPacket(GatewayConfig.MQRelayPacket);
        } catch (IOException ex) {
          Logger.getLogger(MQMessageDispatcher.class.getName())
            .log(Level.SEVERE, null, ex);
        }
      }
    };
    try {
      mq.connectToMQServer(Config.MQ_SERVER).follow(Config.GHOST_CHANNEL);
      // TODO: remove hard coded data
    } catch (IOException ex) {
      Utils.Log("Kaput!");
    } catch (InterruptedException ex) {
      Utils.Log("Kaput!");
    }

    this.transporter = new CallbackPacketTransporter(connection, 
                                                     new IPacketHandler() {
      @Override
      public void receive(int packetID, byte[] dgram) {
        String s = new String(dgram);
        try {
          mq.sendMessage(s);
        } catch (IOException ex) {
          Logger.getLogger(MQMessageDispatcher.class.getName())
            .log(Level.SEVERE, null, ex);
        }
      }
    });
    connection.RegisterTransporter(transporter, GatewayConfig.MQRelayPacket);
  }
}
