package penoplatinum.agent;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author: Team Platinum
 */
public class MQTest {

  public MQTest() {
  }

  @BeforeClass
  public static void setUpClass() throws Exception {
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  @Test
  public void testMQRunner() throws IOException, InterruptedException {
    Thread t = new Thread(new Runnable() {

      @Override
      public void run() {
        try {
          MQRunner.main(new String[]{"MQTest"});
        } catch (IOException ex) {
          Logger.getLogger(MQTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
          Logger.getLogger(MQTest.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    });
    t.start();

    MQRunner.main(new String[]{"MQTest2"});
  }

  @Test
  public void testMQConnectLocal() throws IOException {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    factory.setPort(5672);
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
  }
}
