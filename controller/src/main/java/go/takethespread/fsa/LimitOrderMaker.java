package go.takethespread.fsa;

import go.takethespread.Money;
import go.takethespread.Order;
import go.takethespread.managers.ExternalManager;
import go.takethespread.managers.InfoManager;

public class LimitOrderMaker extends OrderMaker {
    private TradeBlotter blotter;
    private ExternalManager externalManager;
    private InfoManager infoManager;
    private int favorableSize;
    private Order frontOrd_n;
    private Order frontOrd_f;


    public LimitOrderMaker(TradeBlotter blotter, ExternalManager externalManager, InfoManager infoManager) {
        this.blotter = blotter;
        this.externalManager = externalManager;
        this.infoManager = infoManager;
        favorableSize = Integer.valueOf(infoManager.getActualProperties().getProperty("favorable_size"));
    }

    public void doNearBuyFarSell() {
    }


    public void doFarBuyNearSell() {
    }

    public void doCancelling(){

    }

    private void buying(Order order, Term term) {
        String instr = term == Term.NEAR
                ? blotter.getInstrument_n()
                : blotter.getInstrument_f();
        ;
        Money price = term == Term.NEAR
                ? blotter.getBid_n()
                : blotter.getBid_f();
        ;
        int size = term == Term.NEAR
                ? possibleDealSize(favorableSize, blotter.getBidVol_n())
                : possibleDealSize(favorableSize, blotter.getBidVol_f());
        ;
        if (order != null) {
            externalManager.sendCancelOrder(order.getId());
        }

        externalManager.sendLimitBuy(instr, price, size);
    }

    private void selling(Order order, Term term) {

    }

    private int possibleDealSize(int favorableSize, int blotterSize) {
        return favorableSize <= blotterSize ? favorableSize : blotterSize;
    }
}
