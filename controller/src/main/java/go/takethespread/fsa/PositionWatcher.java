package go.takethespread.fsa;

import go.takethespread.Order;

public class PositionWatcher {
    private TradeBlotter blotter;
    private MarketMaker marketMaker;
    private int prevPos_n;
    private int prevPos_f;
    private int curPos_n;
    private int curPos_f;

    public PositionWatcher(TradeBlotter blotter, MarketMaker marketMaker) {
        this.blotter = blotter;
        this.marketMaker = marketMaker;
        this.prevPos_n = 0;
        this.prevPos_f = 0;
        this.curPos_n = 0;
        this.curPos_f = 0;
    }

    public void equalizePositions() {
        updateCurValues();
        int diff_n = curPos_n - prevPos_n;
        int diff_f = curPos_f - prevPos_f;

        if (curPos_n != curPos_f) {

            if (diff_n == 0 && diff_f == 0) {
                throw new RuntimeException("fatal error in algorithm,  both pos are different, but both diffs = 0: " + diff_n + " " + diff_f);
            }

            if (diff_n != 0) {
                fillTheDiff(diff_n, Term.FAR);
            } else {
                fillTheDiff(diff_f, Term.NEAR);
            }
        }
        updatePrevValues();
    }

    public int defineMaxPossibleSize(int marketSize) {
        TradeBlotter.Phase phase = blotter.getCurPhase();
        int absPos = Math.abs(blotter.getPosition_n());
        int calcSize = 0;
        if (phase == TradeBlotter.Phase.ACCUMULATION) {
            calcSize = blotter.getTradeSystemInfo().favorable_size - absPos;
        }

        if (phase == TradeBlotter.Phase.DISTRIBUTION) {
            calcSize = absPos;
        }

        return marketSize < calcSize ? marketSize : calcSize;
    }

    public boolean isPosEqual() {
        int pos_n = Math.abs(blotter.getPosition_n());
        int pos_f = Math.abs(blotter.getPosition_f());
        return pos_n == pos_f;
    }

    private void fillTheDiff(int diff, Term fillTerm) {
        if (diff > 0) {
            marketMaker.hitOrderToMarket(Math.abs(diff), fillTerm, Order.Deal.Sell);
        } else if (diff < 0) {
            marketMaker.hitOrderToMarket(Math.abs(diff), fillTerm, Order.Deal.Buy);
        }
    }

    private void updatePrevValues() {
        prevPos_n = curPos_n;
        prevPos_f = curPos_f;
    }

    private void updateCurValues() {
        curPos_n = blotter.getPosition_n();
        curPos_f = blotter.getPosition_f();
    }
}
