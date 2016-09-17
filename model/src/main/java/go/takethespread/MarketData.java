package go.takethespread;


import java.io.Serializable;
import java.util.Date;

public class MarketData implements Serializable, Identified<Integer> {
    private static final long serialVersionUID = 3L;

    private Integer id = null;
    private Date date;
    private Money bid_n;
    private int bidSize_n;
    private Money ask_n;
    private int askSize_n;
    private Money bid_f;
    private int bidSize_f;
    private Money ask_f;
    private int askSize_f;

    public MarketData() {

    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Money getBid_n() {
        return bid_n;
    }

    public void setBid_n(Money bid_n) {
        this.bid_n = bid_n;
    }

    public int getBidSize_n() {
        return bidSize_n;
    }

    public void setBidSize_n(int bidSize_n) {
        this.bidSize_n = bidSize_n;
    }

    public Money getAsk_n() {
        return ask_n;
    }

    public void setAsk_n(Money ask_n) {
        this.ask_n = ask_n;
    }

    public int getAskSize_n() {
        return askSize_n;
    }

    public void setAskSize_n(int askSize_n) {
        this.askSize_n = askSize_n;
    }

    public Money getBid_f() {
        return bid_f;
    }

    public void setBid_f(Money bid_f) {
        this.bid_f = bid_f;
    }

    public int getBidSize_f() {
        return bidSize_f;
    }

    public void setBidSize_f(int bidSize_f) {
        this.bidSize_f = bidSize_f;
    }

    public Money getAsk_f() {
        return ask_f;
    }

    public void setAsk_f(Money ask_f) {
        this.ask_f = ask_f;
    }

    public int getAskSize_f() {
        return askSize_f;
    }

    public void setAskSize_f(int askSize_f) {
        this.askSize_f = askSize_f;
    }

    @Override
    public String toString() {
        return "MarketData{" +
                "date=" + date +
                ", bid_n=" + bid_n.getAmount() +
                ", bidSize_n=" + bidSize_n +
                ", ask_n=" + ask_n.getAmount() +
                ", askSize_n=" + askSize_n +
                ", bid_f=" + bid_f.getAmount() +
                ", bidSize_f=" + bidSize_f +
                ", ask_f=" + ask_f.getAmount() +
                ", askSize_f=" + askSize_f +
                '}';
    }
}
