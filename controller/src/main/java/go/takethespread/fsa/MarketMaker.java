package go.takethespread.fsa;

import go.takethespread.ClassNameUtil;
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

        String tmpInstr = blotter.termToInstrument(term);
        if (deal == Order.Deal.Buy) {
            externalManager.sendMarketBuy(tmpInstr, size);
        }

        if (deal == Order.Deal.Sell) {
            externalManager.sendMarketSell(tmpInstr, size);
        }

        if (blotter.getCurPhase() == TradeBlotter.Phase.ACCUMULATION) {
            logger.info("pause starting...");
            blotter.getSpreadCalculator().pause();
        } else {
            logger.info("pause is not necessary, cause phase is: " + blotter.getCurPhase());
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
