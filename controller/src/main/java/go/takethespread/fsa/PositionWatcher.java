package go.takethespread.fsa;

import go.takethespread.ClassNameUtil;
import go.takethespread.Order;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PositionWatcher {
    private TradeBlotter blotter;
    private MarketMaker marketMaker;
    private int prevPos_n;
    private int prevPos_f;
    private int curPos_n;
    private int curPos_f;

    private static final Logger logger = LogManager.getLogger(ClassNameUtil.getCurrentClassName());

    public PositionWatcher(TradeBlotter blotter, MarketMaker marketMaker) {
        this.blotter = blotter;
        this.marketMaker = marketMaker;
        this.prevPos_n = 0;
        this.prevPos_f = 0;
        this.curPos_n = 0;
        this.curPos_f = 0;
        logger.info("PW created");
    }

    public void equalizePositions() {
        logger.info("cur values before update: " + curPos_n + " " + curPos_f);
        updateCurValues();
        logger.info("cur values after update: " + curPos_n + " " + curPos_f);
        int diff_n = curPos_n - prevPos_n;
        int diff_f = curPos_f - prevPos_f;

        if (Math.abs(curPos_n) != Math.abs(curPos_f)) {

            if (diff_n == 0 && diff_f == 0) {
                throw new RuntimeException("fatal error in algorithm,  both pos are different, but both diffs = 0: " + diff_n + " " + diff_f);
            }

            if (diff_n != 0) {
                fillTheDiff(diff_n, Term.FAR);
            } else {
                fillTheDiff(diff_f, Term.NEAR);
            }
        }
        logger.info("prev values before update: " + prevPos_n + " " + prevPos_f);
        updatePrevValues();
        logger.info("prev values after update: " + prevPos_n + " " + prevPos_f);
    }

    public int defineMaxPossibleSize(int marketSize) {
        TradeBlotter.Phase phase = blotter.getCurPhase();
        logger.info("defineMaxPossibleSize marketSize=" + marketSize + " favor.size=" + blotter.getTradeSystemInfo().favorable_size + " phase: " + phase);
        int result;
        int absPos = Math.abs(blotter.getPosition_n());
        int calcSize = 0;
        if (phase == TradeBlotter.Phase.ACCUMULATION) {
            calcSize = blotter.getTradeSystemInfo().favorable_size - absPos;
        }

        if (phase == TradeBlotter.Phase.DISTRIBUTION) {
            calcSize = absPos;
        }
        result = marketSize < calcSize ? marketSize : calcSize;
        logger.debug("possible size: " + result);
        return result;
    }

    public boolean isPosEqual() {
        int pos_n = Math.abs(blotter.getPosition_n());
        int pos_f = Math.abs(blotter.getPosition_f());
        return pos_n == pos_f;
    }

    private void fillTheDiff(int diff, Term fillTerm) {
        logger.info("equalizing: " + fillTerm + " " + diff);
        if (diff > 0) {
            marketMaker.hitOrderToMarket(Math.abs(diff), fillTerm, Order.Deal.Sell);
        } else if (diff < 0) {
            marketMaker.hitOrderToMarket(Math.abs(diff), fillTerm, Order.Deal.Buy);
        }
    }

    private void updatePrevValues() {
        prevPos_n = curPos_n;
        prevPos_f = curPos_f;
    }

    private void updateCurValues() {
        curPos_n = blotter.getPosition_n();
        curPos_f = blotter.getPosition_f();
    }
}
