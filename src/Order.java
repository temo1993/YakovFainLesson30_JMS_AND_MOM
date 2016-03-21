import java.io.Serializable;

class Order implements Serializable{
    private int orderID;
    private String stockSymbol;
    private int quantity;
    private float price;
    
    Order(int id, String stockSymbol, int quantity, float price){
    	orderID=id;
    	this.stockSymbol=stockSymbol;
    	this.quantity=quantity;
    	this.price=price;
    }
    @Override
    public String toString(){
    	return "Stock ID: " + orderID + " ,Stock symbol: " + stockSymbol + ", quantity: "+ quantity +
    		", price: " + price;
    }
}
