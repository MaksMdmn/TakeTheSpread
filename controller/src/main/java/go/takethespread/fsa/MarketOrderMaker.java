package go.takethespread.fsa;

import go.takethespread.Order;
import go.takethespread.managers.ExternalManager;
import go.takethespread.managers.ConsoleManager;

public class MarketOrderMaker {

    private TradeBlotter blotter;
    private ExternalManager externalManager;
    private ConsoleManager consoleManager;
    private TradeSystemInfo tradeSystemInfo;
    private int favorableSize;
    private int maxPossibleSize;
    private Algorithm.Phase currentPhase;


    public MarketOrderMaker(TradeBlotter blotter, ExternalManager externalManager,
                            ConsoleManager consoleManager, TradeSystemInfo tradeSystemInfo) {
        this.blotter = blotter;
        this.externalManager = externalManager;
        this.consoleManager = consoleManager;
        this.tradeSystemInfo = tradeSystemInfo;
        this.favorableSize = tradeSystemInfo.favorable_size;
        this.maxPossibleSize = favorableSize;
        this.currentPhase = Algorithm.Phase.ACCUMULATION; //default
    }

    public static int sizeForPairDeal(int nearSize, int farSize) {
        return nearSize <= farSize ? nearSize : farSize;
    }

    public void youShouldKnow(Algorithm.Phase phase) {
        this.currentPhase = phase;
    }

    public int hitTheMarket(int strongSize, Term term, Order.Deal deal) {
        System.out.println("SEND ONE: " + strongSize + " " + term + " " + deal);
        if (deal == null || term == null)
            throw new IllegalArgumentException("deal or term is null: " + deal + " " + term);

        if (strongSize == 0) {
            return 0;
        }

        String tempIntsr = null;
        Order tempOrder = null;

        if (term == Term.NEAR) tempIntsr = blotter.getInstrument_n();
        if (term == Term.FAR) tempIntsr = blotter.getInstrument_f();

        if (deal == Order.Deal.Buy) tempOrder = externalManager.sendMarketBuy(tempIntsr, strongSize);
        if (deal == Order.Deal.Sell) tempOrder = externalManager.sendMarketSell(tempIntsr, strongSize);

        return tempOrder.getFilled();
    }

    public int getOrientedSize(Term term, Side side) {
        if (side == null || term == null)
            throw new IllegalArgumentException("side or term is null: " + side + " " + term);

        int result;
        switch (term) {
            case NEAR:
                switch (side) {
                    case BID:
                        result = blotter.getBidVol_n();
                        break;
                    case ASK:
                        result = blotter.getAskVol_n();
                        break;
                    default:
                        result = 0;
                        break;
                }
                break;
            case FAR:
                switch (side) {
                    case BID:
                        result = blotter.getBidVol_f();
                        break;
                    case ASK:
                        result = blotter.getAskVol_f();
                        break;
                    default:
                        result = 0;
                        break;
                }
                break;
            default:
                result = 0;
                break;
        }

        if (currentPhase == Algorithm.Phase.ACCUMULATION) {
            return maxPossibleSize > result ? result : maxPossibleSize;
        } else if (currentPhase == Algorithm.Phase.DISTRIBUTION) {
            return (favorableSize - 1) > result ? result : (favorableSize - maxPossibleSize);
        } else {
            return 0;
        }
    }

    public void updateMaxSize(int diff) {
        if (currentPhase == Algorithm.Phase.ACCUMULATION) {
            maxPossibleSize -= diff;
        } else {
            maxPossibleSize += diff;
        }

        System.out.println("diff = " + diff + " after that maxSize= " + maxPossibleSize );

    }
}
