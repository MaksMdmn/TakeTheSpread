package go.takethespread.fsa;

import go.takethespread.Money;
import go.takethespread.managers.ExternalManager;

public class Algorithm_Classic extends Algorithm {

    protected Algorithm_Classic(TradeSystemInfo tradeSystemInfo, ExternalManager externalManager, TradeBlotter blotter) {
        super(tradeSystemInfo, externalManager, blotter);
    }

    @Override
    protected String addToLogDebug() {
        return "";
    }


    @Override
    protected Signal getEnterSignal() {
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

    @Override
    protected Signal getExitSignal() {
        Money tempSpread;
        if (blotter.isNearLessThanFar()) {
//            tempSpread = blotter.getAsk_f().subtract(blotter.getBid_n());
            tempSpread = blotter.getBid_f().subtract(blotter.getAsk_n());
            if (tempSpread.lessOrEqualThan(blotter.getSpreadCalculator().getCalcSpread())) {
                return Signal.M_M_SELL;
            }
//            tempSpread = blotter.getAsk_f().subtract(blotter.getAsk_n());
            tempSpread = blotter.getBid_f().subtract(blotter.getAsk_n());
            if (tempSpread.lessOrEqualThan(blotter.getSpreadCalculator().getCalcSpread())) {
                return Signal.L_M_SELL;
            }

//            tempSpread = blotter.getBid_f().subtract(blotter.getBid_n());
            tempSpread = blotter.getAsk_f().subtract(blotter.getAsk_n());
            if (tempSpread.lessOrEqualThan(blotter.getSpreadCalculator().getCalcSpread())) {
                return Signal.M_L_SELL;
            }

        } else {
//            tempSpread = blotter.getAsk_n().subtract(blotter.getBid_f());
            tempSpread = blotter.getBid_n().subtract(blotter.getAsk_f());
            if (tempSpread.lessOrEqualThan(blotter.getSpreadCalculator().getCalcSpread())) {
                return Signal.M_M_BUY;
            }

//            tempSpread = blotter.getBid_n().subtract(blotter.getBid_f());
            tempSpread = blotter.getAsk_n().subtract(blotter.getAsk_f());
            if (tempSpread.lessOrEqualThan(blotter.getSpreadCalculator().getCalcSpread())) {
                return Signal.L_M_BUY;
            }

//            tempSpread = blotter.getAsk_n().subtract(blotter.getAsk_f());
            tempSpread = blotter.getBid_n().subtract(blotter.getBid_f());
            if (tempSpread.lessOrEqualThan(blotter.getSpreadCalculator().getCalcSpread())) {
                return Signal.M_L_BUY;
            }
        }

        return Signal.NOTHING;
    }

}
