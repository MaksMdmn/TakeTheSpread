package go.takethespread.fsa;

import go.takethespread.ClassNameUtil;
import go.takethespread.Money;
import go.takethespread.Order;
import go.takethespread.managers.ExternalManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class LimitMaker {

    private Order frontRunOrder;
    private ExternalManager externalManager;
    private TradeBlotter blotter;
    private Map<String, Integer> alreadyFilledMap;
    private boolean isRollNecessary;

    private static final Logger logger = LogManager.getLogger(ClassNameUtil.getCurrentClassName());

    private Term prevTerm;
    private Order.Deal prevDeal;

    public LimitMaker(ExternalManager externalManager, TradeBlotter blotter) {
        this.externalManager = externalManager;
        this.blotter = blotter;
        this.alreadyFilledMap = new HashMap<>();
        this.prevTerm = null; //for example, may wonder
        this.prevDeal = null; //for example, may wonder

        logger.info("LM created");
    }

    public void rollLimitOrder(int size, Term term, Order.Deal deal) {
        if (isPrevCancellingDangerous(term, deal)) {
            //leave, because it already has changed the position
            return;
        }

        String tmpInstr = blotter.termToInstrument(term);
        Money tmpPrice = defineLimitPrice(term, deal);
        isRollNecessary = isRollNecessary(size, term, deal);

        logger.debug("is roll necessary: " + isRollNecessary);

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

        logger.debug("rolling order: " + frontRunOrder);
    }

    public boolean isOrderPlaced() {
        return frontRunOrder != null;
    }

    public int trackFilledSize() {
        return defineRemainingSize();
    }

    public int cancelOrderSize() {
        frontRunOrder = externalManager.sendCancelOrder(frontRunOrder.getId());
        logger.debug("order cancelled: " + frontRunOrder);
        int result = defineRemainingSize();
        logger.debug("cancelled order size: " + frontRunOrder.getFilled() + " comparing with cancelOrderSize() result: " + result);
        frontRunOrder = null;
        return result;
    }

    private boolean isPrevCancellingDangerous(Term term, Order.Deal deal) {
        if (prevTerm == null && prevDeal == null
                || frontRunOrder == null) {
            prevTerm = term;
            prevDeal = deal;
            return false;

        } else if (term != prevTerm || deal != prevDeal) {
            int cancelSize = cancelOrderSize();

            logger.debug("cancelling dangerous: size is " + cancelSize);
            if (cancelSize > 0) {
                logger.debug("cancelling dangerous: prevTerm/prevDeal " + prevTerm + " " + prevDeal);
            }

            prevTerm = null;
            prevDeal = null;
            return true;
        }

        return false;
    }

    private Order placeAnOrder(String instr, Order.Deal deal, Money price, int size) {
        logger.info("place order by following args: " + instr + " " + deal + " " + price.getAmount() + " " + size);
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
