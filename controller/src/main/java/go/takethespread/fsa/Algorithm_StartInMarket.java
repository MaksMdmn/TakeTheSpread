package go.takethespread.fsa;

import go.takethespread.managers.ExternalManager;

public class Algorithm_StartInMarket extends Algorithm {

    protected Algorithm_StartInMarket(TradeSystemInfo tradeSystemInfo, ExternalManager externalManager, TradeBlotter blotter) {
        super(tradeSystemInfo, externalManager, blotter);
    }

    @Override
    protected String addToLogDebug() {
        return "";
    }

    @Override
    protected Signal getEnterSignal() {
        boolean dataEnoughCheck = blotter.getSpreadCalculator().isEnoughData();
        boolean spreadCompareCheck = blotter.getBestSpread().lessOrEqualThan(blotter.getSpreadCalculator().getCalcSpread());
        if (dataEnoughCheck && spreadCompareCheck) {
            if (blotter.isNearLessThanFar()) {
                return Signal.M_L_SELL;
            } else {
                return Signal.M_L_BUY;
            }
        }
        return Signal.NOTHING;
    }

    @Override
    protected Signal getExitSignal() {
        if (blotter.isNearLessThanFar()) {
            return Signal.L_M_BUY;
        } else {
            return Signal.L_M_SELL;
        }
    }
}
