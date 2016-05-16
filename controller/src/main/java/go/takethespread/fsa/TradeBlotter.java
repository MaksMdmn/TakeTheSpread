package go.takethespread.fsa;

import go.takethespread.Money;
import go.takethespread.Order;
import go.takethespread.managers.ExternalManager;

import java.util.List;

public class TradeBlotter {
    private String instrument_n;
    private String instrument_f;
    private Money bid_n;
    private Money ask_n;
    private Money bid_f;
    private Money ask_f;
    private int bidVol_n;
    private int askVol_n;
    private int bidVol_f;
    private int askVol_f;
    private ExternalManager externalManager;
    private int position_n;
    private int position_f;
    private List<Order> orders;

    public TradeBlotter(String instrument_n, String instrument_f, ExternalManager externalManager){
        this.instrument_n = instrument_n;
        this.instrument_f = instrument_f;
        this.externalManager = externalManager;
    }

    public Money getBid_n() {
        return bid_n;
    }

    public Money getAsk_n() {
        return ask_n;
    }

    public Money getBid_f() {
        return bid_f;
    }

    public Money getAsk_f() {
        return ask_f;
    }

    public int getBidVol_n() {
        return bidVol_n;
    }

    public int getAskVol_n() {
        return askVol_n;
    }

    public int getBidVol_f() {
        return bidVol_f;
    }

    public int getAskVol_f() {
        return askVol_f;
    }

    public String getInstrument_n() {
        return instrument_n;
    }

    public String getInstrument_f() {
        return instrument_f;
    }

    public int getPosition_n() {
        return position_n;
    }

    public int getPosition_f() {
        return position_f;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void updateMainInfo(){
        externalManager.refreshData();
        bid_n = externalManager.getBBid(instrument_n);
        ask_n = externalManager.getBAsk(instrument_n);
        bid_f = externalManager.getBBid(instrument_f);
        ask_f = externalManager.getBAsk(instrument_f);
        bidVol_n = externalManager.getBBidVolume(instrument_n);
        askVol_n = externalManager.getBAskVolume(instrument_n);
        bidVol_f = externalManager.getBBidVolume(instrument_f);
        askVol_f = externalManager.getBAskVolume(instrument_f);
        position_n = externalManager.getPosition(instrument_n);
        position_f= externalManager.getPosition(instrument_f);
    }

    public void updateOrdersInfo(){
        orders = externalManager.getOrders();
    }

    public PositionState nearestPosState() {
        checkPositionsEquivalent();
        if (position_n > 0) return PositionState.LONG;
        if (position_n == 0) return PositionState.FLAT;
        if (position_n < 0) return PositionState.SHORT;
        throw new IllegalArgumentException("IT'S IMPOSSIBLE: " + position_n);
    }

    private void checkPositionsEquivalent(){
        // need to check pos correctly (can be delay\active orders etc)
    }
    protected enum PositionState {
        LONG,
        SHORT,
        FLAT
    }

    //here all market data and pos (like you are in terminal)
}
