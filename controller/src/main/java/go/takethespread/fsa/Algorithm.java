package go.takethespread.fsa;

import go.takethespread.Money;
import go.takethespread.managers.ExternalManager;


public class Algorithm {
    private ExternalManager externalManager;
    private TradeBlotter blotter;
    private TradeSystemInfo tradeSystemInfo;

    protected Algorithm(TradeSystemInfo tradeSystemInfo, ExternalManager externalManager, TradeBlotter blotter) {
        this.tradeSystemInfo = tradeSystemInfo;
        this.externalManager = externalManager;
        this.blotter = blotter;
    }

    public void printMe(){
        System.out.println("algo: *phase is " + blotter.getCurPhase()
                + " spreadB=" + Money.absl(blotter.getBid_f().subtract(blotter.getBid_n())).getAmount()
                + " spreadA=" + Money.absl(blotter.getAsk_f().subtract(blotter.getAsk_n())).getAmount()
                + " spread calc is " + blotter.getSpreadCalculator().getCurSpread().getAmount());

    }

    public Signal getSignal() {
        TradeBlotter.Phase currentPhase = blotter.getCurPhase();

        if(currentPhase == TradeBlotter.Phase.ACCUMULATION){
            return getEnterSignal();
        }else if (currentPhase == TradeBlotter.Phase.DISTRIBUTION){
            return getExitSignal();
        } else{
            return Signal.NOTHING;
        }
    }

    private Signal getEnterSignal() {
        Money tempSpread;
        if (blotter.isNearLessThanFar()) {
            tempSpread = blotter.getBid_f().subtract(blotter.getAsk_n());
            if (tempSpread.greaterOrEqualThan(blotter.getSpreadCalculator().getEnteringSpread())) {
                return Signal.M_M_BUY;
            }

            tempSpread = blotter.getBid_f().subtract(blotter.getBid_n());
            if (tempSpread.greaterOrEqualThan(blotter.getSpreadCalculator().getEnteringSpread())) {
                return Signal.L_M_BUY;
            }

            tempSpread = blotter.getAsk_f().subtract(blotter.getAsk_n());
            if (tempSpread.greaterOrEqualThan(blotter.getSpreadCalculator().getEnteringSpread())) {
                return Signal.M_L_BUY;
            }

        } else {
            tempSpread = blotter.getBid_n().subtract(blotter.getAsk_f());
            if (tempSpread.greaterOrEqualThan(blotter.getSpreadCalculator().getEnteringSpread())) {
                return Signal.M_M_SELL;
            }

            tempSpread = blotter.getAsk_n().subtract(blotter.getAsk_f());
            if (tempSpread.greaterOrEqualThan(blotter.getSpreadCalculator().getEnteringSpread())) {
                return Signal.L_M_SELL;
            }

            tempSpread = blotter.getBid_n().subtract(blotter.getBid_f());
            if (tempSpread.greaterOrEqualThan(blotter.getSpreadCalculator().getEnteringSpread())) {
                return Signal.M_L_BUY;
            }
        }

        return Signal.NOTHING;
    }

    private Signal getExitSignal() {
        Money tempSpread;
        if (blotter.isNearLessThanFar()) {
            tempSpread = blotter.getAsk_f().subtract(blotter.getBid_n());
            if (tempSpread.lessOrEqualThan(blotter.getSpreadCalculator().getCurSpread())) {
                return Signal.M_M_SELL;
            }
            tempSpread = blotter.getAsk_f().subtract(blotter.getAsk_n());
            if (tempSpread.greaterOrEqualThan(blotter.getSpreadCalculator().getCurSpread())) {
                return Signal.L_M_SELL;
            }

            tempSpread = blotter.getBid_f().subtract(blotter.getBid_n());
            if (tempSpread.greaterOrEqualThan(blotter.getSpreadCalculator().getCurSpread())) {
                return Signal.M_L_SELL;
            }

        } else {
            tempSpread = blotter.getAsk_n().subtract(blotter.getBid_f());
            if (tempSpread.greaterOrEqualThan(blotter.getSpreadCalculator().getCurSpread())) {
                return Signal.M_M_BUY;
            }

            tempSpread = blotter.getBid_n().subtract(blotter.getBid_f());
            if (tempSpread.greaterOrEqualThan(blotter.getSpreadCalculator().getCurSpread())) {
                return Signal.L_M_BUY;
            }

            tempSpread = blotter.getAsk_n().subtract(blotter.getAsk_f());
            if (tempSpread.greaterOrEqualThan(blotter.getSpreadCalculator().getCurSpread())) {
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
