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

        String n_instrument = consoleManager.getActualProperties().getProperty("n_instrument");
        String f_instrument = consoleManager.getActualProperties().getProperty("f_instrument");
        Money spread = Money.dollars(Double.valueOf(consoleManager.getActualProperties().getProperty("spread")));

        private Signal getSignal() throws TradeException {

            if (!checkTime()) return Signal.DO_NOTHING;

            switch (checkPosition()){
                case IN_FLAT:
                    if (!checkSpread()) return Signal.DO_NOTHING;
                    break;
                case IN_LONG:
                    if (!checkSpread()) return Signal.DO_NOTHING;
                    break;
                case IN_SHORT:
                    if (!checkSpread()) return Signal.DO_NOTHING;
                    break;
                default:
                    break;
            }


            if (true) /*buy or sell and return that*/ {
                return Signal.LETS_BUY;
            } else {
                return Signal.LETS_SELL;
            }
        }

        private InPosition checkPosition() throws TradeException {
            int pos_n = externalDataManager.getPosition(n_instrument);
            int pos_f = externalDataManager.getPosition(f_instrument);

            if (pos_n == 0 && pos_f == 0) {
                return InPosition.IN_FLAT;
            }

            if (pos_n > 0 && pos_n == pos_f * -1) {
                return InPosition.IN_LONG;
            }

            if (pos_n < 0 && pos_n * -1 == pos_f) {
                return InPosition.IN_FLAT;
            }

            throw new TradeException("Positions is not equvivalent now, NEAR: " + pos_n + " FAR: " + pos_f);
        }

        private boolean checkSpread() {
            return true;
        }

        private boolean checkTime() {
            return true;
        }

    }

    private enum InPosition {
        //ORIENTATE ON NEAREST FUTURE!
        IN_LONG,
        IN_SHORT,
        IN_FLAT
    }

    private enum Signal {
        LETS_BUY,
        LETS_SELL,
        DO_NOTHING

    }
}
