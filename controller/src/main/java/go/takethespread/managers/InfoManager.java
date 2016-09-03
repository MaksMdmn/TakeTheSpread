package go.takethespread.managers;

import go.takethespread.Money;
import go.takethespread.Order;
import go.takethespread.Side;
import go.takethespread.Term;
import go.takethespread.fsa.TradeBlotter;
import go.takethespread.fsa.TradeSystemInfo;

import java.util.List;

public class InfoManager {
    private static InfoManager instance;

    private TradeBlotter blotter;

    private InfoManager() {

    }

    public static InfoManager getInstance() {
        if (instance == null) {
            instance = new InfoManager();
        }
        return instance;
    }

    public void setBlotter(TradeBlotter blotter) {
        this.blotter = blotter;
    }

    public Money getPrice(Term term, Side side) {
        Money answer = null;
        if (term == Term.NEAR) {
            if (side == Side.BID) {
                answer = blotter.getBid_n();
            }
            if (side == Side.ASK) {
                answer = blotter.getAsk_n();
            }
        }

        if (term == Term.FAR) {
            if (side == Side.BID) {
                answer = blotter.getBid_f();
            }
            if (side == Side.ASK) {
                answer = blotter.getAsk_f();
            }
        }

        return answer;

    }

    public int getPosition(Term term) {
        if (term == Term.NEAR) {
            return blotter.getPosition_n();
        }

        if (term == Term.FAR) {
            return blotter.getPosition_n();
        }

        throw new IllegalArgumentException("term is null has illegal value: " + term);
    }

    public Money getCurrentSpread() {
        Money answer = null;
        answer = blotter.getSpreadCalculator().getCurSpread();
        return answer;
    }

    public Money getBestSpread() {
        Money answer = null;
        answer = blotter.getBestSpread();
        return answer;
    }

    public Money getEnteringSpread() {
        Money answer = null;
        answer = blotter.getSpreadCalculator().getEnteringSpread();
        return answer;
    }

    public Money getCash() {
        Money answer = null;
        answer = blotter.getCash();
        return answer;
    }

    public Money getBuyingPwr() {
        Money answer = null;
        answer = blotter.getBuypow();
        return answer;
    }

    public Money getPnL() {
        Money answer = null;
        answer = blotter.getPnl();
        return answer;
    }

    public List<Order> getAllOrders() {
        return blotter.getOrders();
    }

    public TradeSystemInfo getProps() {
        return blotter.getTradeSystemInfo();
    }

    public boolean isBlotterActive() {
        return blotter != null;
    }

    public TradeBlotter getBlotter() {
        return blotter;
    }
}
