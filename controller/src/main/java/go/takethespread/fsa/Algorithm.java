package go.takethespread.fsa;

import go.takethespread.util.ClassNameUtil;
import go.takethespread.Money;
import go.takethespread.managers.ExternalManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Algorithm {
    private ExternalManager externalManager;
    private TradeBlotter blotter;
    private TradeSystemInfo tradeSystemInfo;
    private static final Logger logger = LogManager.getLogger(ClassNameUtil.getCurrentClassName());

    protected Algorithm(TradeSystemInfo tradeSystemInfo, ExternalManager externalManager, TradeBlotter blotter) {
        logger.info("Creation of Algorithm object");
        this.tradeSystemInfo = tradeSystemInfo;
        this.externalManager = externalManager;
        this.blotter = blotter;
        logger.info("Algorithm created");
    }

    public Signal getSignal() {
        logger.debug("Signal definition, market spreads: " +
                " spreadBID=" + Money.absl(blotter.getBid_f().subtract(blotter.getBid_n())).getAmount()
                + " spreadASK=" + Money.absl(blotter.getAsk_f().subtract(blotter.getAsk_n())).getAmount()
                + " spreadCALC=" + blotter.getSpreadCalculator().getCurSpread().getAmount());

        TradeBlotter.Phase currentPhase = blotter.getCurPhase();

        logger.debug("phase is: " + currentPhase);

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
