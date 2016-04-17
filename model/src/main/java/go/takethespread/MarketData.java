package go.takethespread;

import java.io.Serializable;
import java.sql.Date;

public class MarketData implements Serializable {
    private static final long serialVersionUID = 3L;

    private Date date;
    private Money bid;
    private Money ask;
    private Money last;
    private int bidSize;
    private int askSize;

    public MarketData() {

    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
}
