package go.takethespread.fsa;

import go.takethespread.Side;
import go.takethespread.Term;
import go.takethespread.util.ClassNameUtil;
import go.takethespread.Order;
import go.takethespread.managers.ExternalManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MarketMaker {
    private TradeBlotter blotter;
    private ExternalManager externalManager;
    private static final Logger logger = LogManager.getLogger(ClassNameUtil.getCurrentClassName());

    public MarketMaker(ExternalManager externalManager, TradeBlotter blotter) {
        this.externalManager = externalManager;
        this.blotter = blotter;
        logger.info("MM created");
    }

    public static int choosePairDealSize(int size1, int size2) {
        return size1 > size2 ? size2 : size1;
    }

    public void hitOrderToMarket(int size, Term term, Order.Deal deal) {

        logger.debug("hit order by following args: " + term + " " + deal + " " + size);
        if (deal == null || term == null || size <= 0) { //size, what should I do ????
            throw new IllegalArgumentException("illegal arguments, term and term and size are following: " + term + " " + deal + " " + size);
        }

        Order tempOrder = null;
        String tmpInstr = blotter.termToInstrument(term);
        if (deal == Order.Deal.Buy) {
            tempOrder = externalManager.sendMarketBuy(tmpInstr, size);
        }

        if (deal == Order.Deal.Sell) {
            tempOrder = externalManager.sendMarketSell(tmpInstr, size);
        }

        logger.debug("MARKET ORDER (ONE) :" + tempOrder);

        if (blotter.getCurPhase() == TradeBlotter.Phase.ACCUMULATION) {
            logger.info("pause starting...");
            blotter.getSpreadCalculator().pause();
        } else {
            logger.info("pause is not necessary, cause phase is: " + blotter.getCurPhase());
            blotter.getSpreadCalculator().clearAnalysingData();
            logger.info("removed old market data, cause position was closed.");
        }

        blotter.updateOrdersData(); // new, can happen something D:
    }

    public void hitPairOrdersToMarket(int buySize, Term buyTerm, int sellSize, Term sellTerm) {

        logger.debug("PAIR DEAL: BUY " + buySize + " " + buyTerm + " SELL " + sellSize + " " + sellTerm);
        if (buyTerm == null || sellTerm == null || buySize <= 0 || sellSize <= 0) { //size, what should I do ????
            throw new IllegalArgumentException("illegal arguments, term and term and size are following: " + buySize + " " + buyTerm + " " + sellSize + " " + sellTerm);
        }

        Order[] tempOrdersArr = externalManager.sendPairMarketBuySell(blotter.termToInstrument(buyTerm), buySize, blotter.termToInstrument(sellTerm), sellSize);
        logger.debug("!ORDERS SENT!");
        logger.debug("MARKET ORDER (PAIR), ORDER_N: " + tempOrdersArr[0] + " ORDER_F: " + tempOrdersArr[1]);

        if (blotter.getCurPhase() == TradeBlotter.Phase.ACCUMULATION) {
            logger.info("pause starting...");
            blotter.getSpreadCalculator().pause();
        } else {
            logger.info("pause is not necessary, cause phase is: " + blotter.getCurPhase());
            blotter.getSpreadCalculator().clearAnalysingData();
            logger.info("removed old market data, cause position was closed.");
        }

        blotter.updateOrdersData(); // new, can happen something D:
    }

    public int defineMaxMarketSize(Term term, Side side) {
        if (term == Term.NEAR) {
            if (side == Side.BID) {
                return blotter.getBidVol_n();
            }

            if (side == Side.ASK) {
                return blotter.getAskVol_n();
            }
        }

        if (term == Term.FAR) {
            if (side == Side.BID) {
                return blotter.getBidVol_f();
            }

            if (side == Side.ASK) {
                return blotter.getAskVol_f();
            }
        }

        throw new IllegalArgumentException("illegal arguments, term and side are following: " + term + " " + side);
    }
}
