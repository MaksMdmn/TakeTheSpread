package go.takethespread.fsa;


import go.takethespread.util.ClassNameUtil;
import go.takethespread.Money;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class SpreadCalculator {
    private TradeBlotter blotter;
    private TradeSystemInfo tradeSystemInfo;

    private BlockingDeque<Money> marketData;

    private long startTime;
    private long updateTime;
    private long spreadCalcDuration;
    private long startPauseTime;
    private long pauseDuration;

    private Money lastCalcSum;
    private int lastCalcSize;
    private Money lastRemovingElem;

    private Money curSpread;
    private Money enteringSpread;

    private volatile boolean isPauseEnabled;
    private boolean isEnoughData;

    private static final Logger logger = LogManager.getLogger(ClassNameUtil.getCurrentClassName());

    public SpreadCalculator(TradeBlotter blotter, TradeSystemInfo tradeSystemInfo) {
        this.blotter = blotter;
        this.tradeSystemInfo = tradeSystemInfo;
        this.marketData = new LinkedBlockingDeque<>();
        this.startTime = System.currentTimeMillis();
        this.lastCalcSum = Money.dollars(0d);
        this.lastCalcSize = 0;
        this.lastRemovingElem = Money.dollars(0d);
        this.curSpread = Money.dollars(0d); // mb bad default??!!
        this.enteringSpread = Money.dollars(0d); // mb bad default??!!
        this.spreadCalcDuration = tradeSystemInfo.spreadCalc_time_sec * 1000L; //ms
        this.pauseDuration = tradeSystemInfo.inPos_time_sec * 1000L; //ms
        this.isPauseEnabled = false;
        this.isEnoughData = false;

        logger.info("SC created");
    }


    public synchronized void testAddPhonyData() {
        for (int i = 0; i < 50; i++) {
            marketData.add(Money.dollars(0.5d));
        }
    }

    public void makeCalculations() {
        isEnoughData = isEnoughData();
        collectCalcData();
        calcCurSpread();
        calcEnteringSpread();
        logger.info("calculations in SC completed");
//        System.out.println("cur: " + curSpread.getAmount() + " ent " + enteringSpread.getAmount() + " enough? " + isEnoughData());
    }

    public void pause() {
        startPauseTime = System.currentTimeMillis();
        isPauseEnabled = true;
        logger.info("pause started at: " + startPauseTime);
    }

    public boolean isPauseEnabled() {
        return isPauseEnabled;
    }

    public Money getCurSpread() {
        return curSpread;
    }

    public Money getEnteringSpread() {
        return enteringSpread;
    }

    private void collectCalcData() {
        if (!isPauseEnabled) {
            Money spread;

            //the idea is: to get lower spread (for increasing chance to enter to pos), while we have zero pos. And get higher spread to increase chance leave from market when we already have pos.
            if (blotter.getPosition_n() == 0 && blotter.getPosition_f() == 0) {
                spread = blotter.getBestSpread(false);
            } else {
                spread = blotter.getBestSpread(true);
            }

            if (spread.lessThan(Money.dollars(0))) {
//                throw new IllegalArgumentException("bid lower than ask, a/b: " + ask_lower + " " + bid_higher);
            }

            if (isEnoughData) {
                lastRemovingElem = marketData.pollFirst();
                marketData.addLast(spread);
            } else {
                marketData.addLast(spread);
            }
            updateTime = System.currentTimeMillis();
        } else {
            checkPauseNecessity();
        }
    }

    private void calcCurSpread() {

        if (!isEnoughData && tradeSystemInfo.default_spread_use) {
            curSpread = tradeSystemInfo.default_spread;
            return;
        }

        // default value if in prop = false??
        if (!isEnoughData) {
            return;
        }

        if (isPauseEnabled) {
            //previous value and last element is excess
            logger.debug("pause now, should to be old spread value: " + curSpread + " and old size: " + marketData.size());
            return;
        }

        if (marketData == null) {
            throw new NullPointerException("marketData is null.");
        }

        if (marketData.isEmpty()) {
            throw new IllegalArgumentException("market data is empty, cannot calc that, man.");
        }

        if (lastCalcSum.equals(Money.dollars(0d)) || marketData.size() != lastCalcSize) {
            logger.debug("first calc or queue size was changed: both sizes is " + marketData.size() + " " + lastCalcSize + " lastCalcSum: " + lastCalcSum.getAmount());
            lastCalcSum = Money.dollars(0d);
            for (Money m : marketData) {
                lastCalcSum = lastCalcSum.add(m); //sum+=m
            }
            lastCalcSize = marketData.size();
            curSpread = lastCalcSum.multiply(1d / lastCalcSize); // sum/marketData.size()

        } else {
            lastCalcSum = lastCalcSum.subtract(lastRemovingElem).add(marketData.peekLast());
            curSpread = lastCalcSum.multiply(1d / lastCalcSize);
        }
    }

    private void calcEnteringSpread() {
        if (curSpread == null) {
            throw new NullPointerException("curSpread is null.");
        }

        enteringSpread = curSpread.add(tradeSystemInfo.entering_dev);

    }

    private boolean isEnoughData() {
        return (updateTime - startTime) > spreadCalcDuration
                && marketData.size() > tradeSystemInfo.min_spreadCalc_period;
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
