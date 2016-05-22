package go.takethespread.fsa;

import go.takethespread.Money;
import go.takethespread.managers.ExternalManager;


public class Algorithm {
    private ExternalManager externalManager;
    private TradeBlotter blotter;
    private TradeSystemInfo tradeSystemInfo;
    private Phase currentPhase;

    protected Algorithm(TradeSystemInfo tradeSystemInfo, ExternalManager externalManager, TradeBlotter blotter) {
        this.tradeSystemInfo = tradeSystemInfo;
        this.externalManager = externalManager;
        this.blotter = blotter;
        this.currentPhase = Phase.ACCUMULATION;
    }

    public Signal getSignal() {
        blotter.updateMainInfo();
        Money compare1;
        Money compare2;
        if (spreadChoosing() == Side.BID) {
            compare1 = blotter.getBid_n();
            compare2 = blotter.getBid_f();
        } else {
            compare1 = blotter.getAsk_n();
            compare2 = blotter.getAsk_f();
        }

        Signal enterSignal = getEnterSignal(compare1, compare2);
        Signal exitSignal = getExitSignal(compare1, compare2);

        if (enterSignal == Signal.NOTHING) {
            currentPhase = Phase.DISTRIBUTION;
            return exitSignal;
        } else {
            currentPhase = Phase.ACCUMULATION;
            return enterSignal;
        }
    }

    public Phase getCurrentPhase(){
        return currentPhase;
    }

    private Signal getEnterSignal(Money comparePrc_n, Money comparePrc_f) {
        Money tempSpread;
        if (comparePrc_n.lessOrEqualThan(comparePrc_f)) {
            //check market
            tempSpread = blotter.getBid_f().subtract(blotter.getAsk_n());
            if (tempSpread.greaterOrEqualThan(tradeSystemInfo.entering_spread)) {
                return Signal.M_M_BUY;
            }
            //check n lim  f mar

            tempSpread = blotter.getBid_f().subtract(blotter.getBid_n());
            if (tempSpread.greaterOrEqualThan(tradeSystemInfo.entering_spread)) {
                return Signal.L_M_BUY;
            }

            //check n mar   f lim
            tempSpread = blotter.getAsk_f().subtract(blotter.getAsk_n());
            if (tempSpread.greaterOrEqualThan(tradeSystemInfo.entering_spread)) {
                return Signal.M_L_BUY;
            }

        } else {
            tempSpread = blotter.getBid_n().subtract(blotter.getAsk_f());
            if (tempSpread.greaterOrEqualThan(tradeSystemInfo.entering_spread)) {
                return Signal.M_M_SELL;
            }
            //check n lim  f mar

            tempSpread = blotter.getAsk_n().subtract(blotter.getAsk_f());
            if (tempSpread.greaterOrEqualThan(tradeSystemInfo.entering_spread)) {
                return Signal.L_M_SELL;
            }

            //check n mar   f lim
            tempSpread = blotter.getBid_n().subtract(blotter.getBid_f());
            if (tempSpread.greaterOrEqualThan(tradeSystemInfo.entering_spread)) {
                return Signal.M_L_BUY;
            }
        }

        return Signal.NOTHING;
    }

    private Signal getExitSignal(Money comparePrc_n, Money comparePrc_f) {
        Money tempSpread;
        if (comparePrc_n.lessOrEqualThan(comparePrc_f)) {
            //check market
            tempSpread = blotter.getAsk_f().subtract(blotter.getBid_n());
            if (tempSpread.lessOrEqualThan(tradeSystemInfo.leaving_spread)) {
                return Signal.M_M_SELL;
            }
            //check n lim  f mar
            tempSpread = blotter.getAsk_f().subtract(blotter.getAsk_n());
            if (tempSpread.greaterOrEqualThan(tradeSystemInfo.leaving_spread)) {
                return Signal.L_M_SELL;
            }

            //check n mar   f lim
            tempSpread = blotter.getBid_f().subtract(blotter.getBid_n());
            if (tempSpread.greaterOrEqualThan(tradeSystemInfo.leaving_spread)) {
                return Signal.M_L_SELL;
            }

        } else {
            tempSpread = blotter.getAsk_n().subtract(blotter.getBid_f());
            if (tempSpread.greaterOrEqualThan(tradeSystemInfo.leaving_spread)) {
                return Signal.M_M_BUY;
            }
            //check n lim  f mar

            tempSpread = blotter.getBid_n().subtract(blotter.getBid_f());
            if (tempSpread.greaterOrEqualThan(tradeSystemInfo.leaving_spread)) {
                return Signal.L_M_BUY;
            }

            //check n mar   f lim
            tempSpread = blotter.getAsk_n().subtract(blotter.getAsk_f());
            if (tempSpread.greaterOrEqualThan(tradeSystemInfo.leaving_spread)) {
                return Signal.M_L_BUY;
            }
        }

        return Signal.NOTHING;
    }

    private Side spreadChoosing() {
        Money byBid = Money.absl(blotter.getBid_f().subtract(blotter.getBid_n()));
        Money byAsk = Money.absl(blotter.getAsk_f().subtract(blotter.getAsk_n()));

        if (byBid.greaterOrEqualThan(byAsk)) {
            return Side.BID;
        } else {
            return Side.ASK;
        }
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

    protected enum Phase {
        ACCUMULATION,
        DISTRIBUTION
    }

}
