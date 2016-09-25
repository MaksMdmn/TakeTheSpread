package go.takethespread.fsa;

import go.takethespread.Term;
import go.takethespread.util.ClassNameUtil;
import go.takethespread.Order;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PositionWatcher {
    private TradeBlotter blotter;
    private MarketMaker marketMaker;
    private LimitMaker limitMaker;
    private LimitMaker additionalLimitMaker;
    private int prevPos_n;
    private int prevPos_f;
    private int curPos_n;
    private int curPos_f;

    private static final Logger logger = LogManager.getLogger(ClassNameUtil.getCurrentClassName());

    public PositionWatcher(TradeBlotter blotter, MarketMaker mm, LimitMaker lm) {
        this.blotter = blotter;
        this.marketMaker = mm;
        this.limitMaker = lm;
        this.additionalLimitMaker = null;
        this.prevPos_n = 0;
        this.prevPos_f = 0;
        this.curPos_n = 0;
        this.curPos_f = 0;
        logger.info("PW with 1 LM created");
    }

    public PositionWatcher(TradeBlotter blotter, MarketMaker mm, LimitMaker lm, LimitMaker additionalLimitMaker) {
        this.blotter = blotter;
        this.marketMaker = mm;
        this.limitMaker = lm;
        this.additionalLimitMaker = additionalLimitMaker;
        this.prevPos_n = 0;
        this.prevPos_f = 0;
        this.curPos_n = 0;
        this.curPos_f = 0;
        logger.info("PW with 2 LM (one additional) created");
    }

    public void equalizePositions() {
        logger.info("equalize START prev&cur values before CUR_UPDATE: " + prevPos_n + " " + prevPos_f + " <=prev  cur=> " + curPos_n + " " + curPos_f);
        updateCurValues();
        logger.info("equalize START prev&cur values after CUR_UPDATE: " + prevPos_n + " " + prevPos_f + " <=prev  cur=> " + curPos_n + " " + curPos_f);
        int diff_n = curPos_n - prevPos_n;
        int diff_f = curPos_f - prevPos_f;

        if (Math.abs(curPos_n) != Math.abs(curPos_f)) {

            if (diff_n == 0 && diff_f == 0) {
                throw new RuntimeException("fatal error in algorithm,  both pos are different, but both diffs = 0: " + diff_n + " " + diff_f);
            }

            if (diff_n != 0) {
                logger.debug("diff n: " + diff_n);
                fillTheDiff(diff_n, Term.FAR);
            } else {
                logger.debug("diff n: " + diff_f);
                fillTheDiff(diff_f, Term.NEAR);
            }
        }
        logger.info("equalize END prev&cur values before CUR_PREV_UPDATE: " + prevPos_n + " " + prevPos_f + " <=prev  cur=> " + curPos_n + " " + curPos_f);
        blotter.updatePositionData();
        updateCurValues();
        updatePrevValues();
        logger.info("equalize END prev&cur values after CUR_PREV_UPDATE: " + prevPos_n + " " + prevPos_f + " <=prev  cur=> " + curPos_n + " " + curPos_f);
    }

    public int defineMaxPossibleSize(int marketSize) {
        TradeBlotter.Phase phase = blotter.getCurPhase();
        logger.info("defineMaxPossibleSize marketSize=" + marketSize + " favor.size=" + blotter.getTradeSystemInfo().max_size + " phase: " + phase);
        int result;
        int absPos = Math.abs(blotter.getPosition_n());
        int calcSize = 0;
        if (phase == TradeBlotter.Phase.ACCUMULATION) {
            calcSize = blotter.getTradeSystemInfo().max_size - absPos;
        }

        if (phase == TradeBlotter.Phase.DISTRIBUTION) {
            calcSize = absPos;
        }
        result = marketSize < calcSize ? marketSize : calcSize;
        logger.debug("possible size: " + result);
        return result;
    }

    protected void updateEqualAndRelevantPos() {
        logger.info("update prev and cur values, cause pos are equal, but can be changed: " + prevPos_n + " " + prevPos_f + " <=prev  cur=> " + curPos_n + " " + curPos_f);
        updateCurValues();
        updatePrevValues();
        logger.info("pos updated: " + prevPos_n + " " + prevPos_f + " <=prev  cur=> " + curPos_n + " " + curPos_f);
    }

    public boolean isPosEqual() {
        updateCurValues();
        int pos_n = Math.abs(curPos_n);
        int pos_f = Math.abs(curPos_f);
        return pos_n == pos_f;
    }

    private void fillTheDiff(int diff, Term fillTerm) {
        Order.Deal deal;

        if (diff > 0) {
            deal = Order.Deal.Sell;
        } else if (diff < 0) {
            deal = Order.Deal.Buy;
            logger.info("equalizing: " + fillTerm + " " + Math.abs(diff) + " " + Order.Deal.Buy);
        } else {
            logger.info("equalizing size is zero: " + diff);
            return; //exception here?
        }
        marketMaker.hitOrderToMarket(Math.abs(diff), fillTerm, deal);

        limitMaker.cancelOrderAndGetFilled();
        if (additionalLimitMaker != null) {
            additionalLimitMaker.cancelOrderAndGetFilled();
        }

        logger.info("equalizing: " + fillTerm + " " + Math.abs(diff) + " " + deal);
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
