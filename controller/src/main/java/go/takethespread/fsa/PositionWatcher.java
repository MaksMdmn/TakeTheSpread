package go.takethespread.fsa;

import go.takethespread.Order;

public class PositionWatcher {
    private TradeBlotter blotter;
    private MarketMaker marketMaker;

    public PositionWatcher(TradeBlotter blotter, MarketMaker marketMaker) {
        this.blotter = blotter;
        this.marketMaker = marketMaker;
    }

    public int defineMaxSize(int marketSize) {
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

    public void cancelFilledEqualize(int filled, Term term, Order.Deal deal) {

    }

    public void limitFilledEqualize() {

    }

    public void checkPosEqualize() {

    }


}
