package go.takethespread.fsa;


import go.takethespread.util.ClassNameUtil;
import go.takethespread.Money;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class SpreadCalculator {

    private static Money SERIOUS_DEVIATION_FOR_BKW = Money.dollars(0.03d);
    private static double PERCENT_OF_EXCESS = 0.2d;

    private TradeBlotter blotter;
    private TradeSystemInfo tradeSystemInfo;

    private BlockingDeque<Money> marketData;
    private BlockingDeque<Money> marketDataContango;
    private BlockingDeque<Money> marketDataBackwardation;
    private BlockingDeque<Money> marketDataNearBid;
    private BlockingDeque<Money> marketDataNearAsk;
    private Money prevBid;
    private Money prevAsk;

    private long startPauseTime;
    private long pauseDuration;

    private Money calcSpread;
    private Money enteringSpread;
    private Money guideValue;

    private boolean isEnoughData;
    private volatile boolean isPauseEnabled;

    private static final Logger logger = LogManager.getLogger(ClassNameUtil.getCurrentClassName());

    public SpreadCalculator(TradeBlotter blotter, TradeSystemInfo tradeSystemInfo) {
        this.blotter = blotter;
        this.tradeSystemInfo = tradeSystemInfo;
        this.marketData = new LinkedBlockingDeque<>();

        switch (tradeSystemInfo.current_tactics) {
            case 0:
                this.marketDataContango = new LinkedBlockingDeque<>();
                this.marketDataBackwardation = new LinkedBlockingDeque<>();
                break;
            case 1:
                this.marketDataNearBid = new LinkedBlockingDeque<>();
                this.marketDataNearAsk = new LinkedBlockingDeque<>();
                break;
            case 2:
                this.marketDataNearBid = new LinkedBlockingDeque<>();
                this.marketDataNearAsk = new LinkedBlockingDeque<>();
                break;
            default:
                break;
        }
        this.calcSpread = Money.dollars(0d); // mb bad default??!!
        this.enteringSpread = Money.dollars(0d); // mb bad default??!!
        this.pauseDuration = tradeSystemInfo.inPos_time_sec * 1000L; //ms
        this.isPauseEnabled = false;
        this.isEnoughData = false;

        logger.info("SC created");
    }

    public void makeCalculations() {
        isEnoughData = checkEnoughData();
        switch (tradeSystemInfo.current_tactics) {
            case 0:
                collectCalcData();
                calcSpreads();
                break;
            case 1:
                collectDataForLimitEntering();
                calcSpreadsForLimitEntering();
                break;
            case 2:
                collectDataForLimitEntering();
                calcSpreadsForLimitEntering();
                break;
            default:
                break;
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

    public boolean isEnoughData() {
        return isEnoughData;
    }

    protected synchronized TradeBlotter.Situation tryToClearCurSituation() {
        switch (tradeSystemInfo.current_tactics) {
            case 0:
                TradeBlotter.Situation result;
                if (marketData.peekFirst().lessOrEqualThan(marketData.peekLast())) {
                    result = TradeBlotter.Situation.CONTANGO;
                } else {
                    result = TradeBlotter.Situation.BACKWARDATION;
                }

                return result;
            case 1:
                return TradeBlotter.Situation.CONTANGO;
            case 2:
                return TradeBlotter.Situation.CONTANGO;
            default:
                break;
        }

        return TradeBlotter.Situation.CONTANGO; //compiler deception
    }

    protected void clearAnalysingData() {
        marketData.clear();
        switch (tradeSystemInfo.current_tactics) {
            case 0:
                marketDataContango.clear();
                marketDataBackwardation.clear();
                break;
            case 1:
                marketDataNearBid.clear();
                marketDataNearAsk.clear();
                break;
            case 2:
                marketDataNearBid.clear();
                marketDataNearAsk.clear();
                break;
            default:
                break;
        }
    }

    protected Money necessityOfWorstExitLimitPrice(Money favorablePrice, Money currentLimitPrice){
        if (blotter.isNearLessThanFar()) {
            if(favorablePrice.lessOrEqualThan(currentLimitPrice)){
                return favorablePrice;
            }
            if(prevBid.greaterThan(marketDataNearBid.peekLast())){
                return currentLimitPrice;
            }else{
                return favorablePrice;
            }
        } else {
            if(favorablePrice.greaterOrEqualThan(currentLimitPrice)){
                return favorablePrice;
            }
            if(prevAsk.lessThan(marketDataNearAsk.peekLast())){
                return currentLimitPrice;
            }else{
                return favorablePrice;
            }
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
                prevBid = marketDataNearBid.peekLast();
                prevAsk = marketDataNearAsk.peekLast(); //both previous values - after adding element (code below) we cannot see them - havnt get(i) method in queue
                marketDataNearBid.removeFirst();
                marketDataNearAsk.removeFirst();
                marketDataNearBid.addLast(bid);
                marketDataNearAsk.addLast(ask);
                marketData.removeFirst();
                marketData.addLast(spr);
            } else {
                prevBid = bid;
                prevAsk = ask;
                marketDataNearBid.addLast(bid);
                marketDataNearAsk.addLast(ask);
                marketData.addLast(spr);
            }

            bkwFilter();
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

    private boolean checkEnoughData() {
        switch (tradeSystemInfo.current_tactics) {
            case 0:
                return marketData.size() >= tradeSystemInfo.spread_ticks_ago;
            case 1:
                return marketDataNearBid.size() >= tradeSystemInfo.spread_ticks_ago;
            case 2:
                return marketDataNearBid.size() >= tradeSystemInfo.spread_ticks_ago;
            default:
                break;
        }
        return false; //compiler deception
    }

    private void checkPauseNecessity() {
        if (startPauseTime + pauseDuration < System.currentTimeMillis()
                || blotter.getPosition_n() == 0 && blotter.getPosition_f() == 0) {
            startPauseTime = 0L;
            isPauseEnabled = false;

            logger.debug("pause ended.");
        }
    }

    private void bkwFilter() {
        Money diff;
        Money excess;
        Money replaceExcess;
        if (blotter.isNearLessThanFar()) {
            diff = marketDataNearBid.peekLast().subtract(marketDataNearBid.peekFirst());
            if(diff.greaterOrEqualThan(SERIOUS_DEVIATION_FOR_BKW)){
                excess = marketDataNearBid.pollLast();
                replaceExcess = marketDataNearBid.peekFirst().add(diff.multiply(PERCENT_OF_EXCESS));
                marketDataNearBid.addLast(replaceExcess);
                logger.debug("BKW FILTER (NEAR LESS), REPLACE VALUE: " + excess.getAmount() + " ON " + replaceExcess.getAmount() + " CAUSE DEV WAS: " + diff.getAmount());
            }
        } else {
            diff = marketDataNearAsk.peekFirst().subtract(marketDataNearAsk.peekLast());
            if(diff.greaterOrEqualThan(SERIOUS_DEVIATION_FOR_BKW)){
                excess = marketDataNearBid.pollLast();
                marketDataNearAsk.removeLast();
                replaceExcess = marketDataNearAsk.peekFirst().subtract(diff.multiply(PERCENT_OF_EXCESS));
                marketDataNearAsk.addLast(replaceExcess);
                logger.debug("BKW FILTER (NEAR GREATER), REPLACE VALUE: " + excess.getAmount() + " ON " + replaceExcess.getAmount() + " CAUSE DEV WAS: " + diff.getAmount());
            }
        }
    }

}
