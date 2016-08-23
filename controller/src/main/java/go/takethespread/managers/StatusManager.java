package go.takethespread.managers;

public class StatusManager implements StatusListener {
    private static StatusManager instance;

    private volatile boolean isRunStatusChanged = false;
    private volatile boolean isOrdersInfoUpdated = false;

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
                isRunStatusChanged = true;
                break;
            case GJ:
                isRunStatusChanged = false;
                break;
            case OF:
                isRunStatusChanged = false;
                break;
            default:
                /*NOP*/
                break;
        }

    }

    @Override
    public void ordersInfoUpdated() {
        isOrdersInfoUpdated = true;
    }

    public synchronized boolean isRunStatusChanged() {
        return isRunStatusChanged;
    }

    public synchronized boolean isOrdersInfoUpdated() {
        return isOrdersInfoUpdated;
    }

    public synchronized void restoreOrdersInfoStatus() {
        isOrdersInfoUpdated = false;
    }
}
