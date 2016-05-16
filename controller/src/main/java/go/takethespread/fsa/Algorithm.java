package go.takethespread.fsa;

import go.takethespread.Money;
import go.takethespread.managers.ExternalManager;


public class Algorithm {
    private ExternalManager externalManager;
    private Money enterSpread;
    private Money exitSpread;
    private TradeBlotter blotter;
    private Money currentSpread;
    private TradeBlotter.PositionState position;

    protected Algorithm(Money enterSpread, Money exitSpread, ExternalManager externalManager, TradeBlotter blotter) {
        this.enterSpread = enterSpread;
        this.exitSpread = exitSpread;
        this.externalManager = externalManager;
        this.blotter = blotter;

    }

    public Signal getSignal() {
        blotter.updateMainInfo();
        position = blotter.nearestPosState();
        if (position == TradeBlotter.PositionState.FLAT) {
            return getEnterSignal();
        } else {
            return getExitSignal(position);
        }
    }

    private Signal getEnterSignal() {
        if (blotter.getAsk_n().lessThan(blotter.getBid_f())) {
            currentSpread = calcAbsSpread(blotter.getAsk_n(), blotter.getBid_f());
            if (currentSpread.greaterOrEqualThan(enterSpread)) {
                return Signal.LETS_BUY;
            } else {
                return Signal.NOTHING;
            }
        }

        if (blotter.getBid_n().greaterThan(blotter.getAsk_f())) {
            currentSpread = calcAbsSpread(blotter.getBid_n(), blotter.getAsk_f());
            if (currentSpread.greaterOrEqualThan(enterSpread)) {
                return Signal.LETS_SELL;
            } else {
                return Signal.NOTHING;
            }
        }

        return Signal.NOTHING;
    }

    private Signal getExitSignal(TradeBlotter.PositionState position) {
        switch (position) {
            case LONG:
                currentSpread = calcAbsSpread(blotter.getBid_n(), blotter.getAsk_f());
                if (currentSpread.lessOrEqualThan(exitSpread)) return Signal.LETS_SELL;
                break;
            case SHORT:
                currentSpread = calcAbsSpread(blotter.getAsk_n(), blotter.getBid_f());
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



}
