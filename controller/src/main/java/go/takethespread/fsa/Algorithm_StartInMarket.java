package go.takethespread.fsa;

import go.takethespread.Money;
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
        boolean spreadCheck = blotter.getBestSpread().lessOrEqualThan(
                blotter.getSpreadCalculator()
                        .getCalcSpread()
                        .subtract( Money.dollars(0.01d)));
        boolean internalSpreadsCheck = (blotter.getAsk_n().subtract(blotter.getBid_n())).equals( Money.dollars(0.01d)) && (blotter.getAsk_f().subtract(blotter.getBid_f()).equals( Money.dollars(0.01d)));

        if (dataEnoughCheck && spreadCheck && internalSpreadsCheck) {
            if (blotter.isNearLessThanFar()) {

                return Signal.L_M_SELL; //nearest future has more liquidity, so limit order will execute faster.
            } else {
                return Signal.L_M_BUY;
            }
        }
        return Signal.NOTHING;
    }

    @Override
    protected Signal getExitSignal() {
        if (blotter.isNearLessThanFar()) {
            return Signal.L_L_BUY;
        } else {
            return Signal.L_L_SELL;
        }
    }
}
