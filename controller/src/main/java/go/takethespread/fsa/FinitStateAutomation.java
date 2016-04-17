package go.takethespread.fsa;


import go.takethespread.Money;
import go.takethespread.managers.ExternalDataManager;
import go.takethespread.managers.exceptions.TradeException;
import go.takethespread.managers.impl.ConsoleManager;
import go.takethespread.managers.impl.ExternalDataManagerImpl;
import go.takethespread.managers.impl.TaskManager;

public class FinitStateAutomation extends Thread {
    private ConsoleManager consoleManager;
    private TaskManager taskManager;
    private ExternalDataManager externalDataManager;

    private boolean isWorking;

    //from properties
    private String nFuture;
    private String fFuture;
    private String settingAccount;
    private Money settingSpread;
    private int settingSize;


    @Override
    public void run() {
        isWorking = true;
        if (nFuture == null || fFuture == null) {
//                throw new IllegalAccessException("Please, enter the nFuture name: " + nFuture);
            return;
        }

        TaskManager.TradeTask currentTask;
        consoleManager = ConsoleManager.getInstance();
        taskManager = TaskManager.getInstance();
        externalDataManager = ExternalDataManagerImpl.getInstance();

        while (isWorking) {
            try {
                currentTask = taskManager.getCurrentTask();

                switch (currentTask.getCommand()) {
                    case GO:
                        //standart logical: check prices, waiting for signal and when signal - do the deal
                        break;
                    case GJ:
                        // break the cycle
                        break;
                    case RN:
                        executeRN(currentTask.getItem(), currentTask.getValues());
                        // return the item and handle the values
                        break;
                    case PL:
                        executePL(currentTask.getItem(), currentTask.getValues());
                        // change the item and handle the values
                        break;
                    default:

                        break;
                }

            } catch (TradeException e) {
                e.printStackTrace();
            }

        }
    }

    private void executeGo() {

    }

    private void executeGJ() {
        // correcting completion of work!
        isWorking = false;
    }

    private void executeRN(String item, String values) {

    }

    private void executePL(String item, String values) {

    }

    private class Algorithm {
        private Signal getSignal() {
            if (!checkPosition()) return Signal.DO_NOTHING;
            if (!checkSpread()) return Signal.DO_NOTHING;
            if (!checkTime()) return Signal.DO_NOTHING;
            if (true) /*buy or sell and return that*/ {
                return Signal.LETS_BUY;
            } else {
                return Signal.LETS_SELL;
            }
        }

        private boolean checkPosition() {
            return true;
        }

        private boolean checkSpread() {
            return true;
        }

        private boolean checkTime() {
            return true;
        }
    }

    private enum Signal {
        LETS_BUY,
        LETS_SELL,
        DO_NOTHING

    }
}
