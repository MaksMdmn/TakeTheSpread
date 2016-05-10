package go.takethespread.fsa;

import go.takethespread.Money;
import go.takethespread.managers.ExternalManager;

public class Algorithm {
    private ExternalManager externalManager;
    private String instrument_n;
    private String instrument_f;
    private Money enterSpread;
    private Money exitSpread;
    private Money currentSpread;

    private PositionState position;
    private Money bid_n;
    private Money ask_n;
    private Money bid_f;
    private Money ask_f;

    protected Algorithm(String instrument_n, String instrument_f, Money enterSpread, Money exitSpread,
                        ExternalManager externalManager) {
        this.instrument_n = instrument_n;
        this.instrument_f = instrument_f;
        this.enterSpread = enterSpread;
        this.exitSpread = exitSpread;
        this.externalManager = externalManager;
        dataUpdate();

    }

    public Signal getSignal() {
        dataUpdate();
        if (position == PositionState.FLAT) {
            return makeDeal();
        } else {
            return closeDeal(position);
        }
    }

    private Signal makeDeal() {
        if (ask_n.lessThan(bid_f)) {
            currentSpread = calcAbsSpread(ask_n, bid_f);
            if (currentSpread.greaterOrEqualThan(enterSpread)) {
                return Signal.LETS_BUY;
            } else {
                return Signal.NOTHING;
            }
        }

        if (bid_n.greaterThan(ask_f)) {
            currentSpread = calcAbsSpread(bid_n, ask_f);
            if (currentSpread.greaterOrEqualThan(enterSpread)) {
                return Signal.LETS_SELL;
            } else {
                return Signal.NOTHING;
            }
        }

        return Signal.NOTHING;
    }

    private Signal closeDeal(PositionState position) {
        switch (position) {
            case LONG:
                currentSpread = calcAbsSpread(bid_n, ask_f);
                if (currentSpread.lessOrEqualThan(exitSpread)) return Signal.LETS_SELL;
                break;
            case SHORT:
                currentSpread = calcAbsSpread(ask_n, bid_f);
                if (currentSpread.lessOrEqualThan(exitSpread)) return Signal.LETS_BUY;
                break;
            default:
                throw new IllegalArgumentException("Illegal position value in 'close' method: " + position);
        }
        return Signal.NOTHING;
    }

    private Money calcAbsSpread(Money val1, Money val2) {
        return Money.dollars(Math.abs(val1.subtract(val2).getAmount()));
    }

    private void dataUpdate() {
        externalManager.refreshData();
        bid_n = externalManager.getBBid(instrument_n);
        ask_n = externalManager.getBAsk(instrument_n);
        bid_f = externalManager.getBBid(instrument_f);
        ask_f = externalManager.getBAsk(instrument_f);
        position = getCurrentPosition();
    }

    private PositionState getCurrentPosition() {
        int tempPos = externalManager.getPosition(instrument_n);
        if (tempPos > 0) return PositionState.LONG;
        if (tempPos == 0) return PositionState.FLAT;
        if (tempPos < 0) return PositionState.SHORT;
        throw new IllegalArgumentException("IT'S IMPOSSIBLE: " + tempPos);
    }

    protected enum Signal {
        LETS_BUY,
        LETS_SELL,
        NOTHING
    }

    protected enum PositionState {
        LONG,
        SHORT,
        FLAT
    }

    @Override
    public String toString() {
        return "Algorithm{" +
                "bid_n=" + bid_n.getAmount() +
                ", ask_n=" + ask_n.getAmount() +
                ", bid_f=" + bid_f.getAmount() +
                ", ask_f=" + ask_f.getAmount() +
                ", currentSpread=" + currentSpread.getAmount() +
                ", enterSpread=" + enterSpread.getAmount() +
                '}';
    }
}
