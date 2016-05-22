package go.takethespread.fsa;

import go.takethespread.Money;
import go.takethespread.Order;
import go.takethespread.managers.ExternalManager;
import go.takethespread.managers.ConsoleManager;

public class MixedOrderMaker {
    private TradeBlotter blotter;
    private ExternalManager externalManager;
    private ConsoleManager consoleManager;
    private MarketOrderMaker mom;
    private Order frontRunningOrder;

    private Term afterLastChangeTerm;
    private Order.Deal afterLastChangeDeal;

    public MixedOrderMaker(TradeBlotter blotter, ExternalManager externalManager, ConsoleManager consoleManager, MarketOrderMaker mom) {
        this.blotter = blotter;
        this.externalManager = externalManager;
        this.consoleManager = consoleManager;
        this.mom = mom;
        this.frontRunningOrder = null;
        this.afterLastChangeTerm = Term.NEAR; //for example
        this.afterLastChangeDeal = Order.Deal.Buy; //for example
    }

    public void attemptToCatch(int orientedOn, Term term, Order.Deal deal) {
        trackingChanges(term, deal);

        if (orientedOn == 0) {
            return;
        }

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

    public void posEqualize() {
        if (frontRunningOrder != null) {
            int tempSize = cancellingExecution(frontRunningOrder);
            Term tempTerm = blotter.getOrderTerm(frontRunningOrder.getInstrument());
            Order.Deal tempDeal = frontRunningOrder.getDeal();

            askForHelp(tempSize, tempTerm, tempDeal);
        }
    }

    private void trackingChanges(Term term, Order.Deal deal) {
        boolean isNecessary = false;

        if (frontRunningOrder == null) {
            return; //MB HERE TROUBLE???
        }

        if (term != afterLastChangeTerm) {
            isNecessary = true;
            afterLastChangeTerm = term;
        }
        if (deal != afterLastChangeDeal) {
            isNecessary = true;
            afterLastChangeDeal = deal;
        }

        if (isNecessary) {
            frontRunningOrder = null;
            posEqualize();
        }
    }

    private boolean isCancelNecessary(Order actualOrder, Money newPrice, int newSize) {
        if (!actualOrder.getPrice().equals(newPrice)) return true;
        if (actualOrder.getSize() != newSize) return true;
        return false;
    }

    private void askForHelp(int size, Term reverseTerm, Order.Deal reverseDeal) {
        if (reverseTerm == Term.NEAR) {
            reverseTerm = Term.FAR;
        } else {
            reverseTerm = Term.NEAR;
        }

        if (reverseDeal == Order.Deal.Buy) {
            reverseDeal = Order.Deal.Sell;
        } else {
            reverseDeal = Order.Deal.Buy;
        }
        mom.hitTheMarket(size, reverseTerm, reverseDeal);
    }

    private Order orderRolling(Order order, int orientedSize) {
        if (order == null) throw new NullPointerException("One of param is null. Order: " + order.toString());

        Order answer = null;
        Money price = null;
        int size = 0;
        int filledSize = 0;
        Term term = blotter.getOrderTerm(order.getInstrument());

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

        if (isCancelNecessary(order, price, orientedSize)) {
            filledSize = cancellingExecution(order);
            askForHelp(filledSize, term, order.getDeal());

            size = calcDealSize(orientedSize, filledSize);
            if (order.getDeal() == Order.Deal.Buy) {
                answer = externalManager.sendLimitBuy(order.getInstrument(), price, size);
            } else {
                answer = externalManager.sendLimitSell(order.getInstrument(), price, size);
            }
        } else {
            filledSize = trackingExecution(order);
            askForHelp(filledSize, term, order.getDeal());
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

    private int calcDealSize(int oriented, int filled) {
        return oriented - filled;
    }

    private int trackingExecution(Order order) {
        return externalManager.getOrderFilled(order.getId());
    }

    private int cancellingExecution(Order order) {
        return externalManager.sendCancelOrder(order.getId()).getFilled();
    }
}
