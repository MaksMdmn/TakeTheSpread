package go.takethespread;


import java.io.Serializable;

public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    private String ntId;
    private String account;
    private String instrument;
    private Money price;
    private int size;
    private OrderAction action;
    private OrderCommand command;
    private OrderType type;
    private OrderStatus status;
    private OrderTIF tif;


    public Order() {

    }

    public String getNtId() {
        return ntId;
    }

    public void setNtId(String ntId) {
        this.ntId = ntId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public Money getPrice() {
        return price;
    }

    public void setPrice(Money price) {
        this.price = price;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public OrderAction getAction() {
        return action;
    }

    public void setAction(OrderAction action) {
        this.action = action;
    }

    public OrderCommand getCommand() {
        return command;
    }

    public void setCommand(OrderCommand command) {
        this.command = command;
    }

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public OrderTIF getTif() {
        return tif;
    }

    public void setTif(OrderTIF tif) {
        this.tif = tif;
    }


    @Override
    public String toString() {
        return "Order{" +
                "ntId='" + ntId + '\'' +
                ", account='" + account + '\'' +
                ", instrument='" + instrument + '\'' +
                ", price=" + price +
                ", size=" + size +
                ", action=" + action +
                ", command=" + command +
                ", type=" + type +
                ", status=" + status +
                ", tif=" + tif +
                '}';
    }
}


