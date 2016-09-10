package go.takethespread.fsa;

import go.takethespread.Term;
import go.takethespread.managers.StatusListener;
import go.takethespread.managers.StatusManager;
import go.takethespread.util.ClassNameUtil;
import go.takethespread.Money;
import go.takethespread.Order;
import go.takethespread.managers.ExternalManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class TradeBlotter {
    private Money bid_n;
    private Money ask_n;
    private Money bid_f;
    private Money ask_f;
    private int bidVol_n;
    private int askVol_n;
    private int bidVol_f;
    private int askVol_f;
    private ExternalManager externalManager;
    private TradeSystemInfo tradeSystemInfo;
    private SpreadCalculator spreadCalculator;
    private int position_n;
    private int position_f;
    private List<Order> orders;
    private Money cash;
    private Money buypow;
    private Money pnl;
    private Phase curPhase;
    private Situation curSituation;
    private StatusListener listener = StatusManager.getInstance();

    private static final Logger logger = LogManager.getLogger(ClassNameUtil.getCurrentClassName());

    public TradeBlotter(TradeSystemInfo tradeSystemInfo, ExternalManager externalManager) {
        this.tradeSystemInfo = tradeSystemInfo;
        this.externalManager = externalManager;
        this.spreadCalculator = new SpreadCalculator(this, tradeSystemInfo);
        this.curPhase = Phase.OFF_SEASON; //default
        this.curSituation = Situation.CONTANGO; //default
    }

    public Term instrumentToTerm(String instrument) {
        if (instrument.equals(tradeSystemInfo.instrument_n)) {
            return Term.NEAR;
        }

        if (instrument.equals(tradeSystemInfo.instrument_f)) {
            return Term.FAR;
        }

        throw new IllegalArgumentException("Incorrect instrument: " + instrument);
    }

    public String termToInstrument(Term term) {
        if (term == Term.NEAR) {
            return tradeSystemInfo.instrument_n;
        }

        if (term == Term.FAR) {
            return tradeSystemInfo.instrument_f;
        }

        throw new IllegalArgumentException("Incorrect term: " + term);
    }

    public synchronized Money getBid_n() {
        return bid_n;
    }

    public synchronized Money getAsk_n() {
        return ask_n;
    }

    public synchronized Money getBid_f() {
        return bid_f;
    }

    public synchronized Money getAsk_f() {
        return ask_f;
    }

    public synchronized int getBidVol_n() {
        return bidVol_n;
    }

    public synchronized int getAskVol_n() {
        return askVol_n;
    }

    public synchronized int getBidVol_f() {
        return bidVol_f;
    }

    public synchronized int getAskVol_f() {
        return askVol_f;
    }

    public synchronized String getInstrument_n() {
        return tradeSystemInfo.instrument_n;
    }

    public synchronized String getInstrument_f() {
        return tradeSystemInfo.instrument_f;
    }

    public synchronized int getPosition_n() {
        return position_n;
    }

    public synchronized int getPosition_f() {
        return position_f;
    }

    public synchronized Money getCash() {
        return cash;
    }

    public synchronized Money getBuypow() {
        return buypow;
    }

    public synchronized Money getPnl() {
        return pnl;
    }

    public synchronized List<Order> getOrders() {
        return orders;
    }

    public synchronized SpreadCalculator getSpreadCalculator() {
        return spreadCalculator;
    }

    public synchronized TradeSystemInfo getTradeSystemInfo() {
        return tradeSystemInfo;
    }

    public synchronized Phase getCurPhase() {
        return curPhase;
    }

    public synchronized Situation getCurSituation() {
        return curSituation;
    }

    public void updateMarketData() {
        logger.info("data updating...");
        externalManager.refreshData();
        bid_n = externalManager.getBBid(tradeSystemInfo.instrument_n);
        ask_n = externalManager.getBAsk(tradeSystemInfo.instrument_n);
        bid_f = externalManager.getBBid(tradeSystemInfo.instrument_f);
        ask_f = externalManager.getBAsk(tradeSystemInfo.instrument_f);
        bidVol_n = externalManager.getBBidVolume(tradeSystemInfo.instrument_n);
        askVol_n = externalManager.getBAskVolume(tradeSystemInfo.instrument_n);
        bidVol_f = externalManager.getBBidVolume(tradeSystemInfo.instrument_f);
        askVol_f = externalManager.getBAskVolume(tradeSystemInfo.instrument_f);
        cash = externalManager.getCashValue();
        buypow = externalManager.getBuyingPower();
        pnl = externalManager.getPnL();
        logger.debug("new market data: " +
                bid_n.getAmount() + " " + bidVol_n + " " +
                ask_n.getAmount() + " " + askVol_n + " " +
                bid_f.getAmount() + " " + bidVol_f + " " +
                ask_f.getAmount() + " " + askVol_f);
//
//        logger.warn("new market data: " +
//                bid_n.getAmount() + " " + bidVol_n + " " +
//                ask_n.getAmount() + " " + askVol_n + " " +
//                bid_f.getAmount() + " " + bidVol_f + " " +
//                ask_f.getAmount() + " " + askVol_f);

    }

    public void updatePositionData() {
        logger.info("position updating...");
        position_n = externalManager.getPosition(tradeSystemInfo.instrument_n);
        position_f = externalManager.getPosition(tradeSystemInfo.instrument_f);
        logger.debug("position updated: " + position_n + " " + position_f);
    }

    public void updateAuxiliaryData() {
        logger.info("auxiliary data updating...");
        spreadCalculator.makeCalculations();
        curSituation = defineCurSituation();
        curPhase = defineCurPhase();

        logger.debug("new auxiliary data: calcSpread " + spreadCalculator.getCalcSpread().getAmount() + " curPhase " + curPhase + " curSituation " + curSituation);
    }


    public void updateOrdersData() {
        orders = externalManager.getOrders();
        listener.ordersInfoUpdated(orders);

        Money[] transactionPrices;
        if (isNearLessThanFar()) {
            if (curSituation == Situation.CONTANGO) {
                transactionPrices = new Money[]{ask_n, bid_f};
            } else if (curSituation == Situation.BACKWARDATION) {
                transactionPrices = new Money[]{bid_n, ask_f};
            } else {
                transactionPrices = new Money[]{ask_n, bid_f}; // DEFAULT, MB CHANGE THAT IN FUTURE
            }
        } else {
            if (curSituation == Situation.CONTANGO) {
                transactionPrices = new Money[]{bid_n, ask_f};
            } else if (curSituation == Situation.BACKWARDATION) {
                transactionPrices = new Money[]{ask_n, bid_f};
            } else {
                transactionPrices = new Money[]{bid_n, ask_f};// DEFAULT, MB CHANGE THAT IN FUTURE
            }

        }

        listener.transactionTookPlace(transactionPrices);
    }

    public boolean isNearLessThanFar() {
        return bid_n.lessOrEqualThan(bid_f);
    }


    public Money getBestSpread() {
        return getBestSpread(curSituation);
    }

    public synchronized Money getBestSpread(Situation situation) {
        if (isNearLessThanFar()) {
            if (situation == Situation.CONTANGO) {
                return Money.absl(ask_n.subtract(bid_f));
            }

            if (situation == Situation.BACKWARDATION) {
                return Money.absl(bid_n.subtract(ask_f));
            }

            return Money.absl(ask_n.subtract(bid_f)); //default
        } else {
            if (situation == Situation.CONTANGO) {
                return Money.absl(bid_n.subtract(ask_f));
            }

            if (situation == Situation.BACKWARDATION) {
                return Money.absl(ask_n.subtract(bid_f));
            }
            return Money.absl(bid_n.subtract(ask_f)); //default
        }
    }

    private Phase defineCurPhase() {
        int pos_n = Math.abs(position_n);
        int pos_f = Math.abs(position_f);

        if (pos_n != pos_f) {
            throw new IllegalArgumentException("pos are not equal, n and f: " + position_n + " " + position_f);
        }

        //okay, abs positions are equalize, but they must have different sign
        if (position_n == position_f && position_n != 0) {
            throw new IllegalArgumentException("both pos are totally equal and have the same sign, n and f: " + position_n + " " + position_f);
        }

        Money bestSpread = getBestSpread(curSituation);

        if (!tradeSystemInfo.limit_entering_mode) {
            if (curSituation == Situation.CONTANGO) {
                if (bestSpread.lessOrEqualThan(spreadCalculator.getCalcSpread())
                        && pos_n > 0) {
                    return Phase.DISTRIBUTION;
                }

                if (bestSpread.greaterOrEqualThan(spreadCalculator.getEnteringSpread())
                        && pos_n < tradeSystemInfo.max_size) {
                    return Phase.ACCUMULATION;
                }
            } else if (curSituation == Situation.BACKWARDATION) {
                if (bestSpread.greaterOrEqualThan(spreadCalculator.getCalcSpread())
                        && pos_n > 0) {
                    return Phase.DISTRIBUTION;
                }

                if (bestSpread.lessOrEqualThan(spreadCalculator.getEnteringSpread())
                        && pos_n < tradeSystemInfo.max_size) {
                    return Phase.ACCUMULATION;
                }
            }

            return Phase.OFF_SEASON;
        } else {
            if (bestSpread.lessOrEqualThan(spreadCalculator.getCalcSpread())
                    && pos_n > 0) {
                return Phase.DISTRIBUTION;
            }
            return Phase.ACCUMULATION;
        }

    }

    private Situation defineCurSituation() {
        if (position_n == 0 && position_n == position_f) {
            return spreadCalculator.tryToClearCurSituation();
        } else {
            return curSituation;
        }
    }


    protected enum Phase {
        ACCUMULATION,
        DISTRIBUTION,
        OFF_SEASON
    }

    public enum Situation {
        CONTANGO,
        BACKWARDATION
    }

    //here all market data and pos (like you are in terminal)
}
