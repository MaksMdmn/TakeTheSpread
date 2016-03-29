package go.takethespread;

import java.io.Serializable;
import java.sql.Date;

public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    private Date actDate;
    private Date endDate;
    private String assetTicker;
    private Money enterPrice;
    private Money exitPrice;
    private Money volume;
    private int size;
    private Money result;

    public Order() {

    }

    public Date getActDate() {
        return actDate;
    }

    public void setActDate(Date actDate) {
        this.actDate = actDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getAssetTicker() {
        return assetTicker;
    }

    public void setAssetTicker(String assetTicker) {
        this.assetTicker = assetTicker;
    }

    public Money getEnterPrice() {
        return enterPrice;
    }

    public void setEnterPrice(Money enterPrice) {
        this.enterPrice = enterPrice;
    }

    public Money getExitPrice() {
        return exitPrice;
    }

    public void setExitPrice(Money exitPrice) {
        this.exitPrice = exitPrice;
    }

    public Money getVolume() {
        return volume;
    }

    public void setVolume(Money volume) {
        this.volume = volume;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Money getResult() {
        return result;
    }

    public void setResult(Money result) {
        this.result = result;
    }
}
