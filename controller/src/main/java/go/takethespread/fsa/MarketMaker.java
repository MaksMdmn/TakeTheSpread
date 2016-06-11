package go.takethespread.fsa;

import go.takethespread.Order;
import go.takethespread.managers.ExternalManager;

public class MarketMaker {
    private TradeBlotter blotter;
    private ExternalManager externalManager;

    public MarketMaker(TradeBlotter blotter, ExternalManager externalManager) {
        this.blotter = blotter;
        this.externalManager = externalManager;
    }

    public static int choosePairDealSize(int size1, int size2) {
        return size1 > size2 ? size2 : size1;
    }

    public void hitOrderToMarket(int size, Term term, Order.Deal deal) {

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

        blotter.getSpreadCalculator().pause();
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
