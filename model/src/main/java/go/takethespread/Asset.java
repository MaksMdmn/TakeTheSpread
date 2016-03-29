package go.takethespread;

import java.io.Serializable;

public class Asset implements Serializable{
    private static final long serialVersionUID = 2L;

    private String name;
    private String ticker;
    private String type;
    private Money priceStep;
    private Money priceValue;


    public Asset() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Money getPriceStep() {
        return priceStep;
    }

    public void setPriceStep(Money priceStep) {
        this.priceStep = priceStep;
    }

    public Money getPriceValue() {
        return priceValue;
    }

    public void setPriceValue(Money priceValue) {
        this.priceValue = priceValue;
    }
}
