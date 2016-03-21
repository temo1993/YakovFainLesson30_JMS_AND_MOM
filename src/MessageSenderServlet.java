import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/MessageSenderServlet")
public class MessageSenderServlet extends HttpServlet {

    @Resource(lookup ="java:comp/DefaultJMSConnectionFactory")  // JNDI name
            ConnectionFactory factory;

    @Resource(lookup = "OutgoingTradeOrders")  // JNDI name
            Destination ordersQueue;


    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{

        try(JMSContext context = factory.createContext("admin","admin")){

            JMSProducer producer = context.createProducer();

            // Send msg to buy 200 shares of IBM at market price
            producer.send(ordersQueue,"IBM 200 Mkt");
            PrintWriter out = response.getWriter();
            out.print("<html><p>Message sent!</p></html>");
            System.out.println("Placed an order to purchase 200 shares of IBM to OutgoingTradeOrders");
        }
    }
}
