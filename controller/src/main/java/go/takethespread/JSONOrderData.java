package go.takethespread;

import java.text.SimpleDateFormat;

public class JSONOrderData {
    private String date;
    private String instrument;
    private char deal;
    private char type;
    private double price;
    private int size;
    private double priceFilled;
    private int sizeFilled;
    private String status;


    public JSONOrderData() {

    }

    public JSONOrderData(Order order) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
        this.date = sdf.format(order.getDate());
        this.instrument = order.getInstrument();
        this.deal = order.getDeal().name().charAt(0);
        this.type = order.getType().name().charAt(0);
        this.price = order.getPrice().getAmount();
        this.size = order.getSize();
        this.priceFilled = order.getPriceFilled().getAmount();
        this.sizeFilled = order.getFilled();
        this.status = order.getState().name();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public char getDeal() {
        return deal;
    }

    public void setDeal(char deal) {
        this.deal = deal;
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public double getPriceFilled() {
        return priceFilled;
    }

    public void setPriceFilled(double priceFilled) {
        this.priceFilled = priceFilled;
    }

    public int getSizeFilled() {
        return sizeFilled;
    }

    public void setSizeFilled(int sizeFilled) {
        this.sizeFilled = sizeFilled;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
