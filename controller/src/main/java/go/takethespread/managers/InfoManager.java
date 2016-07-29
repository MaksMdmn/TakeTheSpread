package go.takethespread.managers;

import go.takethespread.Money;
import go.takethespread.Order;
import go.takethespread.fsa.Side;
import go.takethespread.fsa.Term;
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
        } else {
            if (  instance.blotter == null) {
                throw new RuntimeException("class cannot be used - please, initialize: " +
                        ", blotter=" + instance.blotter);
            }
        }
        return instance;
    }

    public void setBlotter(TradeBlotter blotter) {
        this.blotter = blotter;
    }

    public Money getPrice(Term term, Side side) {
        if (term == Term.NEAR) {
            if (side == Side.BID) {
                return blotter.getBid_n();
            }
            if (side == Side.ASK) {
                return  blotter.getAsk_n();
            }
        }

        if (term == Term.FAR) {
            if (side == Side.BID) {
                return blotter.getBid_f();
            }
            if (side == Side.ASK) {
                return blotter.getAsk_f();
            }
        }

        throw new IllegalArgumentException("term or side is null or has illegal value: " + term + " " + side);
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

    public Money getCurentSpread(){
        return blotter.getSpreadCalculator().getCurSpread();
    }

    public Money getBestSpread(){
        return blotter.getBestSpread();
    }

    public Money getEnteringSpread(){
        return blotter.getSpreadCalculator().getEnteringSpread();
    }

    public Money getCash(){
        return blotter.getCash();
    }

    public Money getBuyingPwr(){
        return blotter.getBuypow();
    }

    public Money getPnL(){
        return blotter.getPnl();
    }

    public List<Order> getAllOrders(){
        return blotter.getOrders();
    }

    public TradeSystemInfo getProps(){
        return blotter.getTradeSystemInfo();
    }

}
