package go.takethespread.NT;


import go.takethespread.Money;

import java.io.Serializable;

public class NTOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String account;
    private String instrument;
    private Money price;
    private int size;
    private NTAction action;
    private NTCommand command;
    private NTOrderType type;
    private NTOrderStatus status;
    private NTtif tif;


    public NTOrder() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public NTAction getAction() {
        return action;
    }

    public void setAction(NTAction action) {
        this.action = action;
    }

    public NTCommand getCommand() {
        return command;
    }

    public void setCommand(NTCommand command) {
        this.command = command;
    }

    public NTOrderType getType() {
        return type;
    }

    public void setType(NTOrderType type) {
        this.type = type;
    }

    public NTOrderStatus getStatus() {
        return status;
    }

    public void setStatus(NTOrderStatus status) {
        this.status = status;
    }

    public NTtif getTif() {
        return tif;
    }

    public void setTif(NTtif tif) {
        this.tif = tif;
    }
}


