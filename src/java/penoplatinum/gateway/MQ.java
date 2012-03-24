package penoplatinum.gateway;

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

import penoplatinum.util.Utils;

public class MQ implements Queue {
  private String  channelName = "default";
  private Channel channel;
  private MessageReceiver receiver;

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
        public void handleDelivery(String consumerTag,
                                   Envelope envelope,
                                   AMQP.BasicProperties properties,
                                   byte[] body) throws IOException
        {
          handleIncomingMessage(new String(body));
        }
      });
    this.channelName = name;
    return this;
  }

  // public method to send a message. the implementation adds the internal
  // identification of the sender.
  public MQ send(String message) {
    try {
      this.channel.basicPublish(this.channelName, "", null, message.getBytes());
    } catch (Exception e) {
      System.err.println("MQ Error: " + e.toString());
    }
    return this;
  }

  public MQ subscribe(MessageReceiver receiver) {
    this.receiver = receiver;
    return this;
  }

  protected void handleIncomingMessage(String message) {
    if( this.receiver != null ) {
      this.receiver.receive(message);
    } else {
      System.err.println("No MessageReceiver, can't handle incoming MQ msg.");
    }
  }
}
