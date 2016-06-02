package go.takethespread.fsa;


import go.takethespread.Money;

import java.util.Date;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class SpreadCalculator {
    private TradeBlotter blotter;
    private TradeSystemInfo tradeSystemInfo;

    private BlockingDeque<Money> marketData;
    private long startTime;
    private long updateTime;
    private long spreadCalcTime;
    private Money lastSumCalc;
    private Money lastRemovingElem;

    public SpreadCalculator(TradeBlotter blotter, TradeSystemInfo tradeSystemInfo) {
        this.blotter = blotter;
        this.tradeSystemInfo = tradeSystemInfo;
        this.marketData = new LinkedBlockingDeque<>();
        for (int i = 0; i < 100; i++) {
            marketData.add(Money.dollars(0.11));
        }

        this.startTime = System.currentTimeMillis();
        this.lastSumCalc = Money.dollars(0d);
        this.spreadCalcTime = tradeSystemInfo.spreadCalc_time_sec * 1000L; //ms
    }


    public Money spreadCalc() {
        Money answer;

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
            answer = lastSumCalc.multiply(1d / marketData.size()); // sum/marketData.size()
        } else {
            lastSumCalc = lastSumCalc.subtract(lastRemovingElem).add(marketData.peekLast());
            double denominator = marketData.size(); //it's already new size (+1)
            answer = lastSumCalc.multiply(1d / denominator);
        }
        return answer;
    }

    public void collectCalcData() {
        Money ask_lower;
        Money bid_higher;
        if (blotter.isNearLessThanFar()) {
            ask_lower = blotter.getAsk_n();
            bid_higher = blotter.getBid_f();
        } else {
            ask_lower = blotter.getAsk_f();
            bid_higher = blotter.getBid_n();
        }

        Money spread = bid_higher.subtract(ask_lower);

        System.out.println(ask_lower.getAmount() + " <-ask--bid-> " + bid_higher.getAmount() + " spread-->" + spread.getAmount() + " time--> " + new Date().toString());

        if (spread.lessThan(Money.dollars(0))) {
            throw new IllegalArgumentException("bid lower than ask, a/b: " + ask_lower + " " + bid_higher);
        }

        if (isEnoughData()) {
            lastRemovingElem = marketData.removeFirst();
            marketData.addLast(spread);
        } else {
            marketData.addLast(spread);
        }
        updateTime = System.currentTimeMillis();
    }

    public boolean isEnoughData() {
        return (updateTime - startTime) > spreadCalcTime;
    }

}
