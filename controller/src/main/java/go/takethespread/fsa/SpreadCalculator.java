package go.takethespread.fsa;


import go.takethespread.util.ClassNameUtil;
import go.takethespread.Money;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class SpreadCalculator {
    private TradeBlotter blotter;
    private TradeSystemInfo tradeSystemInfo;

    private BlockingDeque<Money> marketData;
    private BlockingDeque<Money> marketDataContango;
    private BlockingDeque<Money> marketDataBackwardation;
    private BlockingDeque<Money> marketDataNearBid;
    private BlockingDeque<Money> marketDataNearAsk;

    private long startPauseTime;
    private long pauseDuration;

    private Money calcSpread;
    private Money enteringSpread;
    private Money guideValue;

    private volatile boolean isPauseEnabled;
    private boolean isEnoughData;

    private static final Logger logger = LogManager.getLogger(ClassNameUtil.getCurrentClassName());

    public SpreadCalculator(TradeBlotter blotter, TradeSystemInfo tradeSystemInfo) {
        this.blotter = blotter;
        this.tradeSystemInfo = tradeSystemInfo;
        this.marketData = new LinkedBlockingDeque<>();
        if (!tradeSystemInfo.limit_entering_mode) {
            this.marketDataContango = new LinkedBlockingDeque<>();
            this.marketDataBackwardation = new LinkedBlockingDeque<>();
        } else {
            this.marketDataNearBid = new LinkedBlockingDeque<>();
            this.marketDataNearAsk = new LinkedBlockingDeque<>();
        }
        this.calcSpread = Money.dollars(0d); // mb bad default??!!
        this.enteringSpread = Money.dollars(0d); // mb bad default??!!
        this.pauseDuration = tradeSystemInfo.inPos_time_sec * 1000L; //ms
        this.isPauseEnabled = false;
        this.isEnoughData = false;

        logger.info("SC created");
    }

    public void makeCalculations() {
        isEnoughData = isEnoughData();
        if (!tradeSystemInfo.limit_entering_mode) {
            collectCalcData();
            calcSpreads();
        } else {
            collectDataForLimitEntering();
            calcSpreadsForLimitEntering();
        }
        logger.info("calculations in SC completed");
    }

    public void pause() {
        startPauseTime = System.currentTimeMillis();
        isPauseEnabled = true;
        logger.info("pause started at: " + new Date(startPauseTime));
    }

    public boolean isPauseEnabled() {
        return isPauseEnabled;
    }

    public Money getCalcSpread() {
        return calcSpread;
    }

    public Money getEnteringSpread() {
        return enteringSpread;
    }

    public Money getGuideValue() {
        return guideValue;
    }

    protected synchronized TradeBlotter.Situation tryToClearCurSituation() {
        if (!tradeSystemInfo.limit_entering_mode) {
            TradeBlotter.Situation result;
            if (marketData.peekFirst().lessOrEqualThan(marketData.peekLast())) {
                result = TradeBlotter.Situation.CONTANGO;
            } else {
                result = TradeBlotter.Situation.BACKWARDATION;
            }

            return result;
        } else {
            return TradeBlotter.Situation.CONTANGO;
        }
    }

    protected void clearAnalysingData() {
        marketData.clear();
        if (!tradeSystemInfo.limit_entering_mode) {
            marketDataContango.clear();
            marketDataBackwardation.clear();
        } else {
            marketDataNearBid.clear();
            marketDataNearAsk.clear();
        }
    }

    private void collectCalcData() {
        if (!isPauseEnabled) {
            Money guidePrice = Money.absl(blotter.getBid_f().subtract(blotter.getBid_n()));

            Money spreadCon = blotter.getBestSpread(TradeBlotter.Situation.CONTANGO);
            Money spreadBack = blotter.getBestSpread(TradeBlotter.Situation.BACKWARDATION);

            if (isEnoughData) {
                marketDataContango.removeFirst();
                marketDataBackwardation.removeFirst();
                marketData.removeFirst();
                marketDataContango.addLast(spreadCon);
                marketDataBackwardation.addLast(spreadBack);
                marketData.addLast(guidePrice);
            } else {
                marketDataContango.addLast(spreadCon);
                marketDataBackwardation.addLast(spreadBack);
                marketData.addLast(guidePrice);
            }
        } else {
            checkPauseNecessity();
        }
    }

    private void calcSpreads() {
        TradeBlotter.Situation situation = tryToClearCurSituation();

        if (!isEnoughData) {
            if (tradeSystemInfo.default_spread_using) {
                calcSpread = tradeSystemInfo.default_spread;
                return;
            } else {
                calcSpread = blotter.getBestSpread(situation); //equal to market
                if (situation == TradeBlotter.Situation.CONTANGO) {
                    enteringSpread = calcSpread.add(tradeSystemInfo.entering_dev);
                } else if (situation == TradeBlotter.Situation.BACKWARDATION) {
                    enteringSpread = calcSpread.subtract(tradeSystemInfo.entering_dev);
                }
                return;
            }
        }

        if (isPauseEnabled) {
            //previous value and last element is excess
            logger.debug("pause now, should to be old spread value: " + calcSpread.getAmount());
            return;
        }

        if (marketData == null) {
            throw new NullPointerException("marketData is null.");
        }

        if (marketData.isEmpty()) {
            throw new IllegalArgumentException("market data is empty, cannot calc that, man.");
        }

        if (situation == TradeBlotter.Situation.CONTANGO) {
            calcSpread = marketDataContango.peekFirst();
            enteringSpread = calcSpread.add(tradeSystemInfo.entering_dev);
        } else if (situation == TradeBlotter.Situation.BACKWARDATION) {
            calcSpread = marketDataBackwardation.peekFirst();
            enteringSpread = calcSpread.subtract(tradeSystemInfo.entering_dev);
        }

        logger.debug("curSpr = " + calcSpread.getAmount() + " based on: " + situation);

    }

    private void collectDataForLimitEntering() {
        if (!isPauseEnabled) {
            Money bid = blotter.getBid_n();
            Money ask = blotter.getAsk_n();
            Money spr = blotter.getBestSpread();

            if (isEnoughData) {
                marketDataNearBid.removeFirst();
                marketDataNearAsk.removeFirst();
                marketDataNearBid.addLast(bid);
                marketDataNearAsk.addLast(ask);
                marketData.removeFirst();
                marketData.addLast(spr);
            } else {
                marketDataNearBid.addLast(bid);
                marketDataNearAsk.addLast(ask);
                marketData.addLast(spr);
            }
        } else {
            checkPauseNecessity();
        }
    }

    private void calcSpreadsForLimitEntering() {
        if (!isEnoughData) {
            if (tradeSystemInfo.default_spread_using) {
                return;
            } else {
                guideValue = blotter.getBid_n();
                calcSpread = blotter.getBestSpread();
                return;
            }
        }

        if (isPauseEnabled) {
            //previous value and last element is excess
            logger.debug("pause now, should to be old spread value: " + calcSpread.getAmount());
            return;
        }

        if (marketDataNearBid == null || marketDataNearAsk == null) {
            throw new NullPointerException("marketData is null.");
        }

        if (marketDataNearBid.isEmpty() || marketDataNearAsk.isEmpty()) {
            throw new IllegalArgumentException("market data is empty, cannot calc that, man.");
        }

        if (blotter.isNearLessThanFar()) {
            guideValue = marketDataNearBid.peekFirst();
        } else {
            guideValue = marketDataNearAsk.peekFirst();
        }
        calcSpread = marketData.peekFirst();
    }

    private boolean isEnoughData() {
        if (!tradeSystemInfo.limit_entering_mode) {
            return marketData.size() >= tradeSystemInfo.spread_ticks_ago;
        } else {
            return marketDataNearBid.size() >= tradeSystemInfo.spread_ticks_ago;
        }
    }

    private void checkPauseNecessity() {
        if (startPauseTime + pauseDuration < System.currentTimeMillis()
                || blotter.getPosition_n() == 0 && blotter.getPosition_f() == 0) {
            startPauseTime = 0L;
            isPauseEnabled = false;

            logger.debug("pause ended.");
        }
    }

}
