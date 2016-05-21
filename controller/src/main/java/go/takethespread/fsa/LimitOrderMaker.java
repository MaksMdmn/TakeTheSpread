package go.takethespread.fsa;

import go.takethespread.Money;
import go.takethespread.Order;
import go.takethespread.managers.ExternalManager;
import go.takethespread.managers.ConsoleManager;

import java.util.HashMap;
import java.util.Map;

public class LimitOrderMaker {
    private TradeBlotter blotter;
    private ExternalManager externalManager;
    private ConsoleManager consoleManager;
    private Order frontRunningOrder;
    private int collectedSize;
    private Map<String, Integer> filledMap;

    private Term afterLastChangeTerm;
    private Order.Deal afterLastChangeDeal;

    public LimitOrderMaker(TradeBlotter blotter, ExternalManager externalManager, ConsoleManager consoleManager) {
        this.blotter = blotter;
        this.externalManager = externalManager;
        this.consoleManager = consoleManager;
        this.frontRunningOrder = null;
        this.collectedSize = 0;
        this.filledMap = new HashMap<>();

        this.afterLastChangeTerm = Term.NEAR; //for example
        this.afterLastChangeDeal = Order.Deal.Buy; //for example
    }


    public void tryingTo(int orientedOn, Term term,Order.Deal deal) {
        cancelAllIfNecessary(term, deal);

        if (frontRunningOrder == null) {
            switch (deal) {
                case Buy:
                    if (term == Term.NEAR) {
                        frontRunningOrder = externalManager.sendLimitBuy(blotter.getInstrument_n(), blotter.getBid_n(), orientedOn);
                    } else {
                        frontRunningOrder = externalManager.sendLimitBuy(blotter.getInstrument_f(), blotter.getBid_f(), orientedOn);
                    }
                    break;
                case Sell:
                    if (term == Term.NEAR) {
                        frontRunningOrder = externalManager.sendLimitSell(blotter.getInstrument_n(), blotter.getAsk_n(), orientedOn);
                    } else {
                        frontRunningOrder = externalManager.sendLimitSell(blotter.getInstrument_f(), blotter.getAsk_f(), orientedOn);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Incorrect deal type: " + deal);
            }
        } else {
            frontRunningOrder = orderRolling(frontRunningOrder, orientedOn);
        }
    }


    public int getCollectedSize() {
        return collectedSize;
    }

    public void resetFilledMap() {
        filledMap.clear();
    }

    public void resetFrontRun() {
        frontRunningOrder = null;
    }

    private void cancelAllIfNecessary(Term term, Order.Deal deal) {
        boolean isNecessary = false;

        if (term != afterLastChangeTerm) {
            isNecessary = true;
            afterLastChangeTerm = term;
        }
        if (deal != afterLastChangeDeal) {
            isNecessary = true;
            afterLastChangeDeal = deal;
        }

        if (isNecessary) externalManager.sendCancelOrders();
    }

    private Order orderRolling(Order order, int orientedSize) {
        if (order == null) throw new NullPointerException("One of param is null. Order: " + order.toString());

        Order answer = null;
        Money price = null;
        int size = 0;
        Term term;

        if (order.getInstrument().equals(blotter.getInstrument_n())) {
            term = Term.NEAR;
        } else if (order.getInstrument().equals(blotter.getInstrument_f())) {
            term = Term.FAR;
        } else {
            throw new IllegalArgumentException("incorrect instruments name(order's, near, far): " + order.getInstrument() + " " + blotter.getInstrument_n() + " " + blotter.getInstrument_f());
        }

        switch (order.getDeal()) {
            case Buy:
                if (term == Term.NEAR) {
                    price = calcDealPrice(Side.BID, blotter.getBid_n(), order.getPrice());
                } else {
                    price = calcDealPrice(Side.BID, blotter.getBid_f(), order.getPrice());
                }
                break;
            case Sell:
                if (term == Term.NEAR) {
                    price = calcDealPrice(Side.ASK, blotter.getAsk_n(), order.getPrice());
                } else {
                    price = calcDealPrice(Side.ASK, blotter.getAsk_f(), order.getPrice());
                }
                break;
            default:
                throw new IllegalArgumentException("Incorrect deal type: " + order.getDeal());
        }

        if (updateIfNecessary(order, price, orientedSize)) {
            Order tempOrder = externalManager.sendCancelOrder(order.getId());
            calcCollectedPos(tempOrder);
            size = calcDealSize(orientedSize, tempOrder.getFilled());
            if (order.getDeal() == Order.Deal.Buy) {
                answer = externalManager.sendLimitBuy(order.getInstrument(), price, size);
            } else {
                answer = externalManager.sendLimitSell(order.getInstrument(), price, size);
            }
        } else {
            answer = order;
        }

        return answer;
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

    private int calcDealSize(int oriented, int filled){
        return oriented - filled;
    }

    private int calcCollectedPos(Order order) {
        int result = 0;
        filledMap.put(order.getId(), order.getFilled());
        for (Map.Entry<String, Integer> pair : filledMap.entrySet()) {
            result += pair.getValue();
        }
        return result;
    }

    private boolean updateIfNecessary(Order actualOrder, Money newPrice, int newSize) {
        if (!actualOrder.getPrice().equals(newPrice)) return true;
        if (actualOrder.getSize() != newSize) return true;
        return false;
    }

}
