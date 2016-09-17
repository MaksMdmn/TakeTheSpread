package go.takethespread.managers;

import go.takethespread.Money;
import go.takethespread.Order;
import go.takethespread.fsa.TradeBlotter;

import java.util.List;

public class StatusManager implements StatusListener {
    private static StatusManager instance;

    private OrderManager orderManager = null;
    private MarketDataCollector marketDataCollector = null;
    private volatile boolean isRunning = false;
    private volatile boolean isGoExecuting = false;
    private volatile boolean isOrdersInfoUpdated = true;
    private volatile boolean transactionHappened = false;
    private Money[] lastTransactions;

    private StatusManager() {
    }

    public static StatusManager getInstance() {
        if (instance == null) {
            instance = new StatusManager();
        }
        return instance;
    }

    @Override
    public void runStatusChanged(ConsoleManager.ConsoleCommand command) {
        switch (command) {
            case GO:
                isRunning = true;
                isGoExecuting = true;
                break;
            case GJ:
                isRunning = false;
                isGoExecuting = false;
                break;
            case OF:
                isRunning = false;
                isGoExecuting = false;
                break;
            default:
                /*NOP*/
                break;
        }

    }

    @Override
    public void ordersInfoUpdated(List<Order> orders) {
        if (orderManager == null) {
            orderManager = OrderManager.getInstance();
        }
        orderManager.addOrderToDBWithoutDuplicates(orders);
        isOrdersInfoUpdated = true;
    }

    @Override
    public void transactionTookPlace(Money[] near_far_prices) {
        transactionHappened = true;
        lastTransactions = near_far_prices;
    }

    @Override
    public void receivedMarketData(TradeBlotter blotter) {
        if (marketDataCollector == null) {
            marketDataCollector = MarketDataCollector.getInstance();
        }
        marketDataCollector.collectMarketData(blotter);
        if (marketDataCollector.isItTimeToPushToDb()) {
            marketDataCollector.pushToDataBase();
        }
    }

    public synchronized boolean isRunning() {
        return isRunning;
    }

    public synchronized boolean isGoExecuting() {
        return isGoExecuting;
    }

    public synchronized boolean isOrdersInfoUpdated() {
        return isOrdersInfoUpdated;
    }

    public synchronized boolean isTransactionHappened() {
        return isOrdersInfoUpdated;
    }

    public synchronized void restoreOrdersInfoStatus() {
        isOrdersInfoUpdated = false;
    }

    public synchronized double[] getLastTransactionPricesAndRestore() {
        double[] result = new double[2];
        result[0] = lastTransactions[0] == null ? 0d : lastTransactions[0].getAmount();
        result[1] = lastTransactions[1] == null ? 0d : lastTransactions[1].getAmount();
        lastTransactions = null;
        transactionHappened = false;
        return result;
    }

}
