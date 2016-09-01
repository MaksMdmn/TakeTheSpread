package go.takethespread.managers;

import go.takethespread.Order;

import java.util.List;

public class StatusManager implements StatusListener {
    private static StatusManager instance;

    private OrderManager orderManager = null;
    private volatile boolean isRunning = false;
    private volatile boolean isOrdersInfoUpdated = true;

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
                break;
            case GJ:
                isRunning = false;
                break;
            case OF:
                isRunning = false;
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

    public synchronized boolean isRunning() {
        return isRunning;
    }

    public synchronized boolean isOrdersInfoUpdated() {
        return isOrdersInfoUpdated;
    }

    public synchronized void restoreOrdersInfoStatus() {
        isOrdersInfoUpdated = false;
    }
}
