package go.takethespread;


import java.io.Serializable;
import java.sql.Date;

public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    private Date date;
    private Deal deal;
    private Type type;
    private int size;
    private Money price;
    private int filled;

    public Order(){

    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Deal getDeal() {
        return deal;
    }

    public void setDeal(Deal deal) {
        this.deal = deal;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Money getPrice() {
        return price;
    }

    public void setPrice(Money price) {
        this.price = price;
    }

    public int getFilled() {
        return filled;
    }

    public void setFilled(int filled) {
        this.filled = filled;
    }

    public enum Deal{
        BUY,
        SELL
    }

    public enum Type{
        LIMIT,
        MARKET
    }
}


