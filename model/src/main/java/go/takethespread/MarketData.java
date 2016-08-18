package go.takethespread;


import java.io.Serializable;
import java.util.Date;

public class MarketData implements Serializable, Identified<Integer> {
    private static final long serialVersionUID = 3L;

    private Integer id = null;
    private Date date;
    private Term term;
    private Money bid;
    private Money ask;
    private Money last;
    private int bidSize;
    private int askSize;

    public MarketData() {

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

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }

    public Money getBid() {
        return bid;
    }

    public void setBid(Money bid) {
        this.bid = bid;
    }

    public Money getAsk() {
        return ask;
    }

    public void setAsk(Money ask) {
        this.ask = ask;
    }

    public Money getLast() {
        return last;
    }

    public void setLast(Money last) {
        this.last = last;
    }

    public int getBidSize() {
        return bidSize;
    }

    public void setBidSize(int bidSize) {
        this.bidSize = bidSize;
    }

    public int getAskSize() {
        return askSize;
    }

    public void setAskSize(int askSize) {
        this.askSize = askSize;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return "MarketData{" +
                "id=" + id +
                ", date=" + date +
                ", term=" + term +
                ", bid=" + bid +
                ", ask=" + ask +
                ", last=" + last +
                ", bidSize=" + bidSize +
                ", askSize=" + askSize +
                '}';
    }
}
