package penoplatinum.agent;

/**
 * MQ
 * 
 * Class wrapping the connection to and handling messages to and from a
 * RabbitMQ message queue server.
 *
 * A low-level internal protocol is implemented adding a "<sender>:" prefix
 * to each message to allow tracking of the originating sender.
 *
 * The setup uses 1 queue to which all parties can send messages. Think of a
 * IRC channel or general purpose chat-room. Own messages are not passed to
 * the incoming message handling callback method.
 *
 * Author: Team Platinum
 */

import java.io.IOException;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP;

abstract public class MQ {

  // configurable properties
  private String  server      = "localhost";
  private String  me          = "default";
  private String  channelName = "default";
  
  // the actual channel
  private Channel channel;

  // sets my name, identifying my messages
  public MQ setMyName(String name) {
    this.me = name;
    return this;
  }

  // connects to a server, setting up the technical communication channel
  public MQ connectToMQServer(String name) throws java.io.IOException {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(name);
    Connection connection = factory.newConnection();
    this.channel = connection.createChannel();
    return this;
  }

  // connects to a queue
  public MQ follow(String name) throws java.io.IOException,
                                       java.lang.InterruptedException
  {
    this.channel.exchangeDeclare(name, "fanout");
    String queueName = this.channel.queueDeclare().getQueue();
    this.channel.queueBind(queueName, name, "");
    this.channel.basicConsume(queueName, true,
      new DefaultConsumer(this.channel) {
        @Override
        public void handleDelivery( String consumerTag,
                                    Envelope envelope,
                                    AMQP.BasicProperties properties,
                                    byte[] body ) throws IOException
        {
          String[] parts = new String(body).split(":");
          String sender  = parts[0];
          String message = parts[1];
          if( ! me.equals(sender) ) {
            handleIncomingMessage(sender, message);
          }
        }
      });

    this.channelName = name;
    return this;
  }

  // public method to send a message. the implementation adds the internal
  // identification of the sender.
  public MQ sendMessage(String message) throws java.io.IOException {
    message = this.me + ":" + message;
    this.channel.basicPublish(this.channelName, "", null, message.getBytes());
    return this;
  }

  // this class is abstract to allow users to provide a callback-style
  // method to handle the incoming messages at their level
  abstract void handleIncomingMessage(String sender, String message);
}
