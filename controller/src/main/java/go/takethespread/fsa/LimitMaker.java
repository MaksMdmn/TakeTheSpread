package go.takethespread.fsa;

import go.takethespread.Money;
import go.takethespread.Order;
import go.takethespread.managers.ExternalManager;

import java.util.HashMap;
import java.util.Map;

public class LimitMaker {

    private Order frontRunOrder;
    private ExternalManager externalManager;
    private TradeBlotter blotter;
    private Map<String, Integer> alreadyFilledMap;

    private Term lastTermVal;
    private Order.Deal lastDealVal;

    public LimitMaker(ExternalManager externalManager, TradeBlotter blotter) {
        this.externalManager = externalManager;
        this.blotter = blotter;
        this.alreadyFilledMap = new HashMap<>();
    }

    //return filled size only if order was rolled!!!!!!!!! NOT FILLED SIZE OF ACTIVE ORDER
    public void rollLimitOrder(int size, Term term, Order.Deal deal) {
        String tmpInstr = blotter.termToInstrument(term);
        Money tmpPrice = defineLimitPrice(term, deal);
        boolean isRollNecessary = isRollNecessary(size, term, deal);
        lastTermVal = term;
        lastDealVal = deal;

        if (deal == null || term == null || size <= 0) { //size, what should I do ????
            throw new IllegalArgumentException("illegal arguments, term and term and size are following: " + term + " " + deal + " " + size);
        }

        if (frontRunOrder == null) {
            frontRunOrder = placeAnOrder(tmpInstr, deal, tmpPrice, size);
        } else {
            if (isRollNecessary) {
                frontRunOrder = placeAnOrder(tmpInstr, deal, tmpPrice, defineDealSize(size, cancelOrderSize()));
            }
        }

    }

    public int trackFilledSize() {
        return defineRemainingSize();
    }

    public int cancelOrderSize() {
        frontRunOrder = externalManager.sendCancelOrder(frontRunOrder.getId());
        int result = defineRemainingSize();
        frontRunOrder = null;
        return result;
    }

    private Order placeAnOrder(String instr, Order.Deal deal, Money price, int size) {
        if (deal == Order.Deal.Buy) {
            return externalManager.sendLimitBuy(instr, price, size);
        }

        if (deal == Order.Deal.Sell) {
            return externalManager.sendLimitSell(instr, price, size);
        }

        throw new IllegalArgumentException("illegal arguments, deal is following: " + deal);
    }

    private boolean isRollNecessary(int size, Term term, Order.Deal deal) {
        if (frontRunOrder == null) {
            return false;
        }

        if (size < frontRunOrder.getSize()) {
            return true;
        }

        if (!defineLimitPrice(term, deal).equals(frontRunOrder.getPrice())) {
            return true;
        }

        if (term != blotter.instrumentToTerm(frontRunOrder.getInstrument())) {
            return true;
        }

        if (deal != frontRunOrder.getDeal()) {
            return true;
        }

        return false;
    }

    private Money defineLimitPrice(Term term, Order.Deal deal) {
        if (term == Term.NEAR) {
            if (deal == Order.Deal.Buy) {
                return blotter.getBid_n();
            }

            if (deal == Order.Deal.Sell) {
                return blotter.getAsk_n();
            }
        }

        if (term == Term.FAR) {
            if (deal == Order.Deal.Buy) {
                return blotter.getBid_f();
            }

            if (deal == Order.Deal.Sell) {
                return blotter.getAsk_f();
            }
        }

        throw new IllegalArgumentException("illegal arguments, term and deal are following: " + term + " " + deal);
    }

    private int defineDealSize(int size, int filled) {
        return size - filled;
    }

    private int defineRemainingSize() {
        String key = frontRunOrder.getId();
        int val = externalManager.getOrderFilled(key);
        int result;
        if (alreadyFilledMap.containsKey(key)) {
            if (alreadyFilledMap.get(key) == val) {
                result = 0;
            } else {
                result = val - alreadyFilledMap.get(key);
                alreadyFilledMap.put(key, val);
            }
        } else {
            alreadyFilledMap.put(key, val);
            result = val;
        }

        return result;
    }
}
