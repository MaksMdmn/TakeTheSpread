package go.takethespread.fsa;

import go.takethespread.managers.ExternalManager;
import go.takethespread.util.ClassNameUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Algorithm {
    protected ExternalManager externalManager;
    protected TradeBlotter blotter;
    protected TradeSystemInfo tradeSystemInfo;
    private static final Logger logger = LogManager.getLogger(ClassNameUtil.getCurrentClassName());

    protected Algorithm(TradeSystemInfo tradeSystemInfo, ExternalManager externalManager, TradeBlotter blotter) {
        logger.info("Creation of Algorithm object");
        this.tradeSystemInfo = tradeSystemInfo;
        this.externalManager = externalManager;
        this.blotter = blotter;
        logger.info("Algorithm created");
    }

    public Signal getSignal(){
        logger.debug("Signal definition, market spread: " +
                " spreadCURRENT=" + blotter.getBestSpread().getAmount()
                + " spreadCALC=" + blotter.getSpreadCalculator().getCalcSpread().getAmount()
                + " spreadENTER=" + blotter.getSpreadCalculator().getEnteringSpread().getAmount()
        );

        TradeBlotter.Phase currentPhase = blotter.getCurPhase();

        logger.debug("phase is: " + currentPhase);

        if (currentPhase == TradeBlotter.Phase.ACCUMULATION) {
            return getEnterSignal();
        } else if (currentPhase == TradeBlotter.Phase.DISTRIBUTION) {
            return getExitSignal();
        } else {
            return Algorithm.Signal.NOTHING;
        }
    }

    protected abstract Signal getEnterSignal();

    protected abstract Signal getExitSignal();

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
