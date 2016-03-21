import com.sun.messaging.ConnectionConfiguration;
import com.sun.messaging.ConnectionFactory;

import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;

public class MessagePublisher {
    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        try(JMSContext context = factory.createContext("admin","admin")){
            factory.setProperty(ConnectionConfiguration.imqAddressList,"mq://127.0.0.1:7676,mq://127.0.0.1:7676");
            Destination priceQuoteTopic = context.createTopic("PriceQuoteTopic");
            JMSProducer publisher = context.createProducer();

            // Send msg to PriceQuoteTopic topic
            publisher.send(priceQuoteTopic,"Hello From Subscriber MESsAGE");
            System.out.println("Message sent");
        } catch (JMSException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
