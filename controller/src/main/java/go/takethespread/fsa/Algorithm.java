package go.takethespread.fsa;

import go.takethespread.Money;
import go.takethespread.managers.ExternalDataManager;
import go.takethespread.managers.impl.ConsoleManager;

public class Algorithm {
    private ExternalDataManager externalDataManager;
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
                        ExternalDataManager externalDataManager) {
        this.instrument_n = instrument_n;
        this.instrument_f = instrument_f;
        this.enterSpread = enterSpread;
        this.exitSpread = exitSpread;
        this.externalDataManager = externalDataManager;

        this.bid_n = externalDataManager.getBBid(instrument_n);
        this.ask_n = externalDataManager.getBAsk(instrument_n);
        this.bid_f = externalDataManager.getBBid(instrument_f);
        this.ask_f = externalDataManager.getBAsk(instrument_f);
        this.position = getCurrentPosition();
    }

    public Signal getSignal() {
        if (position == PositionState.FLAT) {
            return makeDeal();
        } else {
            return closeDeal(position);
        }
    }

    private PositionState getCurrentPosition() {
        int tempPos = externalDataManager.getPosition(instrument_n);
        if (tempPos > 0) return PositionState.LONG;
        if (tempPos == 0) return PositionState.FLAT;
        if (tempPos < 0) return PositionState.SHORT;
        throw new IllegalArgumentException("IT'S IMPOSSIBLE: " + tempPos);
    }

    private Signal makeDeal() {
        if (ask_n.lessThan(bid_f)) {
            currentSpread = calcAbsSpread(ask_n, bid_f);
            if (currentSpread.greaterOrEqualThan(enterSpread)) {
                return Signal.LETS_BUY;
            }
        }

        if (bid_n.greaterThan(ask_f)) {
            currentSpread = calcAbsSpread(bid_n, ask_f);
            if (currentSpread.greaterOrEqualThan(enterSpread)) {
                return Signal.LETS_SELL;
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

}
