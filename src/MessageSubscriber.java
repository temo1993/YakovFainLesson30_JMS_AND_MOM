import com.sun.messaging.ConnectionConfiguration;
import com.sun.messaging.ConnectionFactory;

import javax.jms.*;

public class MessageSubscriber implements MessageListener{
    private MessageSubscriber(){
        ConnectionFactory factory = new ConnectionFactory();
/***
        JMS API has a concept of a messaging session in which you can specify
        either acknowledgments modes or request transaction support to give the applications
        control over message removals from queues or topics.
        There are three acknowledgments modes:
            AUTO_ACKNOWLEDGE mode sends the acknowledgment back as soon as the
        method onMessage() is successfully finished. This is a default acknowledgment
        mode.
            CLIENT_ACKNOWLEDGE mode requires explicit acknowledgment by calling the
        method acknowledge() from the message receiver’s code.
            DUP_OK_ACKNOWLEDGE mode is used in case the server fails; the same message
        may be delivered more than once. In some use cases it’s acceptable—for example,
                receiving a price quote twice does not hurt.*/
        try(JMSContext context = factory.createContext("admin","admin",JMSContext.AUTO_ACKNOWLEDGE)){
            factory.setProperty(ConnectionConfiguration.imqAddressList, "mq://127.0.0.1:7676,mq://127.0.0.1:7676");

            Destination priceQuoteTopic = context.createTopic("PriceQuoteTopic");
            JMSConsumer consumer;

/****  Creating non-durable consumer(subscriber) , which means message will delivered to only them who is active at
      given time, and message will deleted from mom after all messages are received to active subscribers*/


//            Destination priceQuoteTopic =
//                    context.createTopic("PriceQuoteTopic");
//            consumer = context.createConsumer(priceQuoteTopic);



/**** Creating durable consumer , which means message will delivered to all subscribers whenever they
      are active on offline, and messages will be saved until every subscriber will receive them*/


            /*** Setting client id for using durability*/
//            context.setClientID("client123");
//            consumer = context.createDurableConsumer((Topic)priceQuoteTopic,
//                    "FraudPreventionUnit");


/**** For scalability reasons, the same subscription can be shared by multiple standalone
            consumers working in parallel (for example, running on different JVMs). In this case, a
            shared consumer has to be created (it can be durable or non-durable). For example, you
            can create a shared durable subscriber, as shown*/

            context.setClientID("client123");
            consumer = context.createSharedDurableConsumer(
                    (Topic)priceQuoteTopic,"FraudPreventionUnit");

/*** At any time an application can unsubscribe from a topic by calling the method
            unsubscribe() on the JMSContext object, for example:*/
//            context.unsubscribe("FraudPreventionUnit");

            consumer.setMessageListener(this);

            System.out.println("Listening to the PriceQuoteTopic...");

            // Keep the program running - wait for messages
            Thread.sleep(100000);
/***
            As an alternative to using acknowledgments, you can request transaction support for
            message consumers. Imagine if a received message contains the data that must be saved in
            a database and forwarded to a Web Service as one unit of work, so unless both operations
            are successful the entire transaction must be rolled back. You may need transaction
            support on the JMS producer side, too. For example, if you need to send two messages as
            one logical unit of work—either both messages were successfully sent or rolled back the
            transaction. The following code snippet shows how to create JMSContext and
            a JMSProducer object that sends two messages in different queues as one transaction.*/

/*            try(JMSContext context = factory.createContext("admin","admin",
                    JMSContext.TRANSACTED)){
                JMSProducer producer = context.createProducer();
                Destination queue1 = context.createQueue("Queue1");
                Destination queue2 = context.createQueue("Queue2");
                producer.send(queue1,"Msg1");
                producer.send(queue2,"Msg2");
                context.commit(); // commit the JMS transaction
            } catch (JMSException e){
                context.rollback(); // rollback the JMS transaction
                System.out.println("Error: " + e.getMessage());
            }*/

/***          If both sends went through fine, the Session object ( encapsulated inside JMSContext)
            issues a commit. If the exception is thrown, no messages are placed in any of the queues.*/

        } catch (InterruptedException e){
            System.out.println("Error: " + e.getMessage());
        }
        catch (JMSException jmsE){
            System.out.println("JMS Error: " + jmsE.getMessage());
        }
    }

    public void onMessage(Message msg){

        try{
            System.out.println("Got the text message from the PriceQuoteTopic: " +
                    msg.getBody(String.class));

            System.out.println("\n === Here's what toString() on the message prints \n" + msg);

        } catch (JMSException e){
            System.err.println("JMSException: " + e.toString());
        }
    }

    public static void main(String[] args) {

        new MessageSubscriber();
    }
}
