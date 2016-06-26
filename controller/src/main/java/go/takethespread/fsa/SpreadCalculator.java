package go.takethespread.fsa;


import go.takethespread.ClassNameUtil;
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

    private Money lastSumCalc;
    private Money lastRemovingElem;

    private Money curSpread;
    private Money enteringSpread;

    private volatile boolean isPauseEnabled;

    private static final Logger logger = LogManager.getLogger(ClassNameUtil.getCurrentClassName());

    public SpreadCalculator(TradeBlotter blotter, TradeSystemInfo tradeSystemInfo) {
        this.blotter = blotter;
        this.tradeSystemInfo = tradeSystemInfo;
        this.marketData = new LinkedBlockingDeque<>();
        for (int i = 0; i < 100; i++) {
            marketData.add(Money.dollars(0.62d));
        }
        this.startTime = System.currentTimeMillis();
        this.lastSumCalc = Money.dollars(0d);
        this.lastRemovingElem = Money.dollars(0d);
        this.curSpread = Money.dollars(0d); // mb bad default??!!
        this.enteringSpread = Money.dollars(0d); // mb bad default??!!
        this.spreadCalcDuration = tradeSystemInfo.spreadCalc_time_sec * 1000L; //ms
        this.pauseDuration = tradeSystemInfo.inPos_time_sec * 1000L; //ms
        this.isPauseEnabled = false;

        logger.info("SC created");
    }

    public void makeCalculations() {
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
//            Money ask_lower;
//            Money bid_higher;
//            if (blotter.isNearLessThanFar()) {
//                ask_lower = blotter.getAsk_n();
//                bid_higher = blotter.getBid_f();
//            } else {
//                ask_lower = blotter.getAsk_f();
//                bid_higher = blotter.getBid_n();
//            }
//
//            Money spread = bid_higher.subtract(ask_lower);

//            System.out.println(ask_lower.getAmount() + " <-ask--bid-> " + bid_higher.getAmount() + " spread-->" + spread.getAmount() + " time--> " + new Date().toString());

            // market or best spread???
            Money spread = blotter.getBestSpread();
            if (spread.lessThan(Money.dollars(0))) {
//                throw new IllegalArgumentException("bid lower than ask, a/b: " + ask_lower + " " + bid_higher);
            }

            if (isEnoughData()) {
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

        if (!isEnoughData() && tradeSystemInfo.default_spread_use) {
            curSpread = tradeSystemInfo.default_spread;
            return;
        }

        // default value if in prop = false??
        if(!isEnoughData()){
            return;
        }

        if (isPauseEnabled) {
            //previous value and last element is excess
            return;
        }

        if (marketData == null) {
            throw new NullPointerException("marketData is null.");
        }

        if (marketData.isEmpty()) {
            throw new IllegalArgumentException("market data is empty, cannot calc that, man.");
        }

        if (lastSumCalc.equals(Money.dollars(0d))) {
            for (Money m : marketData) {
                lastSumCalc = lastSumCalc.add(m); //sum+=m
            }
            curSpread = lastSumCalc.multiply(1d / marketData.size()); // sum/marketData.size()
        } else {
            lastSumCalc = lastSumCalc.subtract(lastRemovingElem).add(marketData.peekLast());
            double denominator = marketData.size(); //it's already new size (+1)
            curSpread = lastSumCalc.multiply(1d / denominator);
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
