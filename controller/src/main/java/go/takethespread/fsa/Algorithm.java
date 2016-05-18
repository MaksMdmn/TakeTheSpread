package go.takethespread.fsa;

import go.takethespread.Money;
import go.takethespread.managers.ExternalManager;


public class Algorithm {
    private ExternalManager externalManager;
    private Money enterSpread;
    private Money exitSpread;
    private TradeBlotter blotter;
    private Money currentSpread;

    protected Algorithm(Money enterSpread, Money exitSpread, ExternalManager externalManager, TradeBlotter blotter) {
        this.enterSpread = enterSpread;
        this.exitSpread = exitSpread;
        this.externalManager = externalManager;
        this.blotter = blotter;

    }

    public Signal getSignal() {
        TradeBlotter.PositionState position = blotter.nearestPosState();
        blotter.updateMainInfo();
        if (position == TradeBlotter.PositionState.FLAT) {
            return getEnterSignal(blotter.getBid_n(), blotter.getBid_f());
        } else {
            return getExitSignal(position, blotter.getBid_n(), blotter.getBid_f());
        }
    }

    private Signal getEnterSignal(Money comparePrc_n, Money comparePrc_f) {
        Money tempSpread;
        if (comparePrc_n.lessOrEqualThan(comparePrc_f)) {
            //check market
            tempSpread = blotter.getBid_f().subtract(blotter.getAsk_n());
            if (tempSpread.greaterOrEqualThan(enterSpread)) {
                return Signal.M_M_BUY;
            }
            //check n lim  f mar

            tempSpread = blotter.getBid_f().subtract(blotter.getBid_n());
            if (tempSpread.greaterOrEqualThan(enterSpread)) {
                return Signal.L_M_BUY;
            }

            //check n mar   f lim
            tempSpread = blotter.getAsk_f().subtract(blotter.getAsk_n());
            if (tempSpread.greaterOrEqualThan(enterSpread)) {
                return Signal.M_L_BUY;
            }

        } else {
            tempSpread = blotter.getBid_n().subtract(blotter.getAsk_f());
            if (tempSpread.greaterOrEqualThan(enterSpread)) {
                return Signal.M_M_SELL;
            }
            //check n lim  f mar

            tempSpread = blotter.getAsk_n().subtract(blotter.getAsk_f());
            if (tempSpread.greaterOrEqualThan(enterSpread)) {
                return Signal.L_M_SELL;
            }

            //check n mar   f lim
            tempSpread = blotter.getBid_n().subtract(blotter.getBid_f());
            if (tempSpread.greaterOrEqualThan(enterSpread)) {
                return Signal.M_L_BUY;
            }
        }

        return Signal.NOTHING;
    }

    private Signal getExitSignal(TradeBlotter.PositionState position, Money comparePrc_n, Money comparePrc_f) {
        Money tempSpread;
        if (comparePrc_n.lessOrEqualThan(comparePrc_f)) {
            //check market
            tempSpread = blotter.getAsk_f().subtract(blotter.getBid_n());
            if (tempSpread.lessOrEqualThan(exitSpread)) {
                return Signal.M_M_SELL;
            }
            //check n lim  f mar
            tempSpread = blotter.getAsk_f().subtract(blotter.getAsk_n());
            if (tempSpread.greaterOrEqualThan(enterSpread)) {
                return Signal.L_M_SELL;
            }

            //check n mar   f lim
            tempSpread = blotter.getBid_f().subtract(blotter.getBid_n());
            if (tempSpread.greaterOrEqualThan(enterSpread)) {
                return Signal.M_L_SELL;
            }

        } else {
            tempSpread = blotter.getAsk_n().subtract(blotter.getBid_f());
            if (tempSpread.greaterOrEqualThan(enterSpread)) {
                return Signal.M_M_BUY;
            }
            //check n lim  f mar

            tempSpread = blotter.getBid_n().subtract(blotter.getBid_f());
            if (tempSpread.greaterOrEqualThan(enterSpread)) {
                return Signal.L_M_BUY;
            }

            //check n mar   f lim
            tempSpread = blotter.getAsk_n().subtract(blotter.getAsk_f());
            if (tempSpread.greaterOrEqualThan(enterSpread)) {
                return Signal.M_L_BUY;
            }
        }

        return Signal.NOTHING;
    }

    protected enum Signal {
        M_M_BUY,
        L_M_BUY,
        M_L_BUY,
        M_M_SELL,
        L_M_SELL,
        M_L_SELL,
        NOTHING
    }


}
