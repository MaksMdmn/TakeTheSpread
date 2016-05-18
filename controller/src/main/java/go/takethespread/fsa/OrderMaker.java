package go.takethespread.fsa;

import go.takethespread.Money;
import go.takethespread.Order;
import go.takethespread.managers.ExternalManager;
import go.takethespread.managers.InfoManager;


public class OrderMaker {
    private TradeBlotter blotter;
    private ExternalManager externalManager;
    private InfoManager infoManager;
    private int favorableSize;
    private Order frontRunningOrder_n;
    private Order frontRunningOrder_f;


    public OrderMaker(TradeBlotter blotter, ExternalManager externalManager, InfoManager infoManager) {
        this.blotter = blotter;
        this.externalManager = externalManager;
        this.infoManager = infoManager;
        this.favorableSize = Integer.valueOf(infoManager.getActualProperties().getProperty("favorable_size"));
    }

    public void doArbitrage(Term leg1, Mode type1, Term leg2, Mode type2) {

    }


    public void doNearBuyFarSell() {
        if (frontRunningOrder_n == null) {
            frontRunningOrder_n = externalManager.sendLimitBuy(blotter.getInstrument_n(), blotter.getBid_n(), calcOrderMaxSize(Term.NEAR));
        } else {
            frontRunningOrder_n = orderRolling(externalManager.getOrder(frontRunningOrder_n.getId()), Term.NEAR);
        }

        if (frontRunningOrder_f == null) {
            frontRunningOrder_f = externalManager.sendLimitSell(blotter.getInstrument_f(), blotter.getAsk_f(), calcOrderMaxSize(Term.FAR));
        } else {
            frontRunningOrder_f = orderRolling(externalManager.getOrder(frontRunningOrder_f.getId()), Term.FAR);
        }
    }


    public void doNearSellFarBuy() {
        int orderMaxSize_n = Math.abs(favorableSize - blotter.getPosition_n());
        int orderMaxSize_f = Math.abs(favorableSize - blotter.getPosition_f());

        if (frontRunningOrder_n == null) {
            frontRunningOrder_n = externalManager.sendLimitSell(blotter.getInstrument_n(), blotter.getAsk_n(), calcOrderMaxSize(Term.NEAR));
        } else {
            frontRunningOrder_n = orderRolling(externalManager.getOrder(frontRunningOrder_n.getId()), Term.NEAR);
        }

        if (frontRunningOrder_f == null) {
            frontRunningOrder_f = externalManager.sendLimitBuy(blotter.getInstrument_f(), blotter.getBid_f(), calcOrderMaxSize(Term.FAR));
        } else {
            frontRunningOrder_f = orderRolling(externalManager.getOrder(frontRunningOrder_f.getId()), Term.FAR);
        }
    }

    public void doCancelling() {
        externalManager.sendCancelOrders();
        frontRunningOrder_n = null;
        frontRunningOrder_f = null;
    }

    private Order orderRolling(Order order, Term term) {
        if (term == null || order == null) {
            throw new NullPointerException("One of param is null. Order: " + order.toString() + " and term: " + term);
        }

        Order answer = null;
        Money price = null;
        int size = 0;

        switch (order.getDeal()) {
            case Buy:
                if (term == Term.NEAR) {
                    price = calcDealPrice(Side.BID, blotter.getBid_n(), order.getPrice());
                    size = calcDealSize(favorableSize, order.getFilled(), blotter.getBidVol_n());
                } else {
                    price = calcDealPrice(Side.BID, blotter.getBid_f(), order.getPrice());
                    size = calcDealSize(favorableSize, order.getFilled(), blotter.getBidVol_f());
                }
                break;
            case Sell:
                if (term == Term.NEAR) {
                    price = calcDealPrice(Side.ASK, blotter.getAsk_n(), order.getPrice());
                    size = calcDealSize(favorableSize, order.getFilled(), blotter.getAskVol_n());
                } else {
                    price = calcDealPrice(Side.ASK, blotter.getAsk_f(), order.getPrice());
                    size = calcDealSize(favorableSize, order.getFilled(), blotter.getAskVol_f());
                }
                break;
            default:
                throw new IllegalArgumentException("Incorrect deal type: " + order.getDeal());
        }

        if (calcUpdateNecessity(order, price, size)) {
            answer = externalManager.sendChangeOrder(order.getId(), price, size);
        } else {
            answer = order;
        }

        return answer;
    }


    private int calcOrderMaxSize(Term term, int filled, int blotterSize) {
        int possibleMaxSize;
        int answer;
        switch (term) {
            case NEAR:
                possibleMaxSize = favorableSize - Math.abs(blotter.getPosition_n()) - filled;
                answer = possibleMaxSize <= blotterSize ? possibleMaxSize : blotterSize;
                break;
            case FAR:
                possibleMaxSize = favorableSize - Math.abs(blotter.getPosition_f());
                break;
            default:
                throw new IllegalArgumentException("term is null: " + term);
        }

        return term == Term.NEAR
                ? favorableSize - Math.abs(blotter.getPosition_n())
                : favorableSize - Math.abs(blotter.getPosition_f());
    }

    private int calcDealSize(int favorSize, int filled, int blotterSize) {
        int favor = favorSize - filled;
        return favor <= blotterSize ? favor : blotterSize;
    }

    private Money calcDealPrice(Side type, Money currentPrice, Money orderPrice) {
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

    private boolean calcUpdateNecessity(Order actualOrder, Money newPrice, int newSize) {
        if (!actualOrder.getPrice().equals(newPrice)) return true;
        if (actualOrder.getSize() != newSize) return true;
        return false;
    }

}
