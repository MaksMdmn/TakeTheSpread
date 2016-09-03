package go.takethespread.util;

import go.takethespread.Order;
import go.takethespread.Term;
import go.takethespread.fsa.TradeSystemInfo;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class JsonOrderData {
    private String date;
    private char term;
    private char deal;
    private char type;
    private double price;
    private int size;
    private double priceFilled;
    private int sizeFilled;
    private String status;


    public JsonOrderData() {

    }

    public JsonOrderData(Order order) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM HH:mm:ss", Locale.ENGLISH);
        this.date = sdf.format(order.getDate());
        this.term = instrumentToTerm(order.getInstrument());
        this.deal = order.getDeal().name().charAt(0);
        this.type = order.getType().name().charAt(0);
        this.price = order.getPrice().getAmount();
        this.size = order.getSize();
        this.priceFilled = order.getPriceFilled().getAmount();
        this.sizeFilled = order.getFilled();
        this.status = order.getState().name();
    }

    private char instrumentToTerm(String instrument) {
        if (instrument.equals(TradeSystemInfo.getInstance().instrument_n)) {
            return Term.NEAR.name().toUpperCase().charAt(0);
        }

        if (instrument.equals(TradeSystemInfo.getInstance().instrument_f)) {
            return Term.FAR.name().toUpperCase().charAt(0);
        }

        throw new IllegalArgumentException("Incorrect instrument: " + instrument);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public char getTerm() {
        return term;
    }

    public void setTerm(char term) {
        this.term = term;
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
