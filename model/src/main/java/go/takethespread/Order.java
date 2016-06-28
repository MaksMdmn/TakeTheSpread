package go.takethespread;


import java.io.Serializable;
import java.util.Date;

public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String instrument;
    private Date date;
    private Deal deal;
    private Type type;
    private State state;
    private int size;
    private Money price;
    private int filled;
    private Money priceFilled;

    public Order() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
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

    public Money getPriceFilled() {
        return priceFilled;
    }

    public void setPriceFilled(Money priceFilled) {
        this.priceFilled = priceFilled;
    }

    public enum Deal {
        Buy,
        Sell
    }

    public enum Type {
        Limit,
        Market
    }

    public enum State {
        Accepted,
        Cancelled,
        Filled,
        PartFilled,
        PendingCancel,
        PendingChange,
        PendingSubmit,
        Rejected,
        Working,
        Unknown,
        Initialized
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", instrument='" + instrument + '\'' +
                ", date=" + date +
                ", deal=" + deal +
                ", type=" + type +
                ", state=" + state +
                ", size=" + size +
                ", price=" + price.getAmount() +
                ", filled=" + filled +
                ", priceFilled=" + priceFilled.getAmount() +
                '}';
    }
}


