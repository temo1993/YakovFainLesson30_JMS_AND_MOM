
import javax.jms.*;

import com.sun.messaging.ConnectionFactory;
import com.sun.messaging.ConnectionConfiguration;


public class DirectMessageSender{
    public static void main(String[] args){

        ConnectionFactory factory;

        factory = new com.sun.messaging.ConnectionFactory();

        try(JMSContext context = factory.createContext("admin","admin")){

            factory.setProperty(ConnectionConfiguration.imqAddressList, "mq://127.0.0.1:7676,mq://127.0.0.1:7676");
            Destination ordersQueue = context.createQueue("TradingOrdersQueue");
/***        If you have to share a queue with some other applications or developers from your team,
             use message selectors (also known as filters) to avoid “stealing” somebody else’s
             messages. For example, in the message consumer application you can opt for receiving
             messages that have a property symbol with the value IBM:
             //        String selector = "symbol=IBM";
             //        Context.createConsumer(ordersQueue, selector);
             In this case the queue listener dequeues only those messages that have a String property
             symbol with the value IBM. Accordingly, the message producers have to set this property
             on the message object:
             //        TextMessage outMsg = context.createTextMessage();
             //        outMsg.setText("IBM 200 Mkt");
             //        outMsg.setStringProperty("symbol", "IBM");
             //        Destination ordersQueue=context.createQueue("TradingOrdersQueue");
             //        JMSProducer producer = context.createProducer();
             //        producer.send(ordersQueue, outMsg);
             Remember that message selectors slow down the process of retrieval. Messages stay in a
             queue until the listener with the matching selector picks them up. Selectors really help if
             your team has a limited number of queues and everyone needs to receive messages
             without interfering with the others. But if someone starts the queue listener without
             selectors, it just drains the queue.*/
            JMSProducer producer = context.createProducer();

            // Send msg to buy 200 shares of IBM at market price
            producer.send(ordersQueue,"IBM 200 Mkt");

            System.out.println("Placed an order to purchase 200 shares of IBM to TradingOrdersQueue");




        } catch (JMSException e){
            System.out.println("Error: " + e.getMessage());
        }
    }
}
