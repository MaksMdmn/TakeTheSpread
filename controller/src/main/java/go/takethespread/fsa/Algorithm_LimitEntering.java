package go.takethespread.fsa;

import go.takethespread.Money;
import go.takethespread.managers.ExternalManager;

public class Algorithm_LimitEntering extends Algorithm {

    protected Algorithm_LimitEntering(TradeSystemInfo tradeSystemInfo, ExternalManager externalManager, TradeBlotter blotter) {
        super(tradeSystemInfo, externalManager, blotter);
    }

    @Override
    protected Signal getEnterSignal() {
        if (blotter.isNearLessThanFar()) {
            return Signal.L_M_BUY;
        } else {
            return Signal.L_M_SELL;
        }
    }

    @Override
    protected Signal getExitSignal() {
        Money tempSpread;
        if(blotter.isNearLessThanFar()){
            tempSpread = blotter.getBid_f().subtract(blotter.getBid_n());
            if (tempSpread.lessOrEqualThan(blotter.getSpreadCalculator().getCalcSpread())) {
                return Signal.M_L_SELL;
            }
        }else{
            tempSpread = blotter.getAsk_n().subtract(blotter.getAsk_f());
            if (tempSpread.lessOrEqualThan(blotter.getSpreadCalculator().getCalcSpread())) {
                return Signal.M_L_BUY;
            }
        }

        return Signal.NOTHING;

    }
}
