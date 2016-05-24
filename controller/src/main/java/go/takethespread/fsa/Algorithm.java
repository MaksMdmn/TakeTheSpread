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
    }

    public Signal getSignal() {
        blotter.updateMainInfo();
        currentPhase = defineCurrentPhase();

        if(currentPhase == Phase.ACCUMULATION){
            return getEnterSignal();
        }else if (currentPhase == Phase.DISTRIBUTION){
            return getExitSignal();
        } else{
            return Signal.NOTHING;
        }
    }

    public Phase getCurrentPhase(){
        return currentPhase;
    }

    public Money getBestSpread(){
        if(isBidBetOrEqThanAsk()){
            return Money.absl(blotter.getBid_n().subtract(blotter.getBid_f()));
        }else{
            return Money.absl(blotter.getAsk_n().subtract(blotter.getAsk_f()));
        }
    }

    private Phase defineCurrentPhase(){
        Money bestSpread = getBestSpread();
        if(bestSpread.greaterOrEqualThan(tradeSystemInfo.entering_spread)
                && blotter.getPosition_n() < tradeSystemInfo.favorable_size){
            return Phase.ACCUMULATION;
            //getPos i think may have delay ----- keep in mind
        }else if(blotter.getPosition_n() != 0){
            return Phase.DISTRIBUTION;
        } else{
            return Phase.OFF_SEASON;
        }
    }
    private Signal getEnterSignal() {
        Money tempSpread;
        if (isNearLessThanFar()) {
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

    private Signal getExitSignal() {
        Money tempSpread;
        if (isNearLessThanFar()) {
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

    private boolean isNearLessThanFar(){
        if (isBidBetOrEqThanAsk()) {
            return blotter.getBid_n().lessOrEqualThan(blotter.getBid_f());
        } else {
            return blotter.getAsk_n().lessOrEqualThan(blotter.getAsk_f());
        }
    }

    private boolean isBidBetOrEqThanAsk(){
        Money byBid = Money.absl(blotter.getBid_f().subtract(blotter.getBid_n()));
        Money byAsk = Money.absl(blotter.getAsk_f().subtract(blotter.getAsk_n()));
        return byBid.greaterOrEqualThan(byAsk);
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
        DISTRIBUTION,
        OFF_SEASON
    }

}
