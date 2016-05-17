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
    private Order frontRunningOrder_n;
    private Order frontRunningOrder_f;


    public LimitOrderMaker(TradeBlotter blotter, ExternalManager externalManager, InfoManager infoManager) {
        this.blotter = blotter;
        this.externalManager = externalManager;
        this.infoManager = infoManager;
        favorableSize = Integer.valueOf(infoManager.getActualProperties().getProperty("favorable_size"));
    }

    public void doNearBuyFarSell() {

        if (frontRunningOrder_n == null) {
            frontRunningOrder_n = externalManager.sendLimitBuy(blotter.getInstrument_n(), blotter.getBid_n(), favorableSize);
        } else {
            frontRunningOrder_n = externalManager.getOrder(frontRunningOrder_n.getId());
            String instr = blotter.getInstrument_n();
            Money price = calcDealPrice(BlotterPrice.BID, blotter.getBid_n(), frontRunningOrder_n.getPrice());
            int size = calcDealSize(favorableSize, frontRunningOrder_n.getFilled(), blotter.getBidVol_n());

            if(calcUpdateNecessity(frontRunningOrder_n, price, size)){

            }

        }

        if (frontRunningOrder_f == null) {
            frontRunningOrder_f = externalManager.sendLimitSell(blotter.getInstrument_f(), blotter.getBid_f(), favorableSize);
        } else {
            frontRunningOrder_f = externalManager.getOrder(frontRunningOrder_f.getId());
        }


    }


    public void doFarBuyNearSell() {
    }

    public void doCancelling() {
        externalManager.sendCancelOrders();
        frontRunningOrder_n = null;
        frontRunningOrder_f = null;
    }

    private int calcDealSize(int favorableSize, int filled, int blotterSize) {
        int favor = favorableSize - filled;
        return favor <= blotterSize ? favor : blotterSize;
    }

    private Money calcDealPrice(BlotterPrice type, Money currentPrice, Money orderPrice) {
        Money answer;
        switch (type) {
            case BID:
                if (currentPrice.greaterThan(orderPrice)) {
                    answer = currentPrice;
                } else {
                    answer = orderPrice;
                }
                break;
            case ASK:
                if (currentPrice.lessThan(orderPrice)) {
                    answer = currentPrice;
                } else {
                    answer = orderPrice;
                }
                break;
            default:
                throw new IllegalArgumentException("price type is null: " + type);
        }

        return answer;
    }

    private boolean calcUpdateNecessity(Order actualOrder, Money newPrice, int newSize){
        if(!actualOrder.getPrice().equals(newPrice)) return true;
        if(actualOrder.getSize()!=newSize) return true;
        return false;
    }

}
