package go.takethespread.fsa;

public class PositionWatcher {
    private TradeBlotter blotter;
    private MarketMaker marketMaker;

    public PositionWatcher(TradeBlotter blotter, MarketMaker marketMaker) {
        this.blotter = blotter;
        this.marketMaker = marketMaker;
    }

    public int defineMaxSize() {
        return 0;
    }

    public boolean isPosEquals() {
        return true;
    }

    public void posEqualize(){

    }
}
