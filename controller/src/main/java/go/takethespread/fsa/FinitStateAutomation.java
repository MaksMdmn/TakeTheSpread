package go.takethespread.fsa;


import go.takethespread.Money;
import go.takethespread.managers.ExternalDataManager;
import go.takethespread.managers.exceptions.TradeException;
import go.takethespread.managers.impl.ConsoleManager;
import go.takethespread.managers.impl.ExternalNTDataManagerImpl;
import go.takethespread.managers.impl.TaskManager;

public class FinitStateAutomation extends Thread {
    private ConsoleManager consoleManager;
    private TaskManager taskManager;
    private ExternalDataManager externalDataManager;

    private String instrument_n;
    private String instrument_f;
    private Money enterSpread;
    private Money exitSpread;
    private int size;

    private Algorithm algo;

    private boolean isWorking;

    @Override
    public void run() {
        isWorking = true;

        prepareToWork();

        TaskManager.TradeTask currentTask;
        consoleManager = ConsoleManager.getInstance();
        taskManager = TaskManager.getInstance();
        externalDataManager = ExternalNTDataManagerImpl.getInstance();

        while (isWorking) {
            try {
                currentTask = taskManager.getCurrentTask();

                switch (currentTask.getCommand()) {
                    case GO:
                        executeGO();
                        //standart logical: check prices, waiting for signal and when signal - do the deal
                        break;
                    case GJ:
                        executeGJ();
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

    private void executeGO() {
        algo = new Algorithm(instrument_n, instrument_f, enterSpread, exitSpread, externalDataManager);
        Algorithm.Signal signal = algo.getSignal();
        switch (signal){
            case LETS_BUY:
                externalDataManager.sendMarketBuy(instrument_n, size);
                externalDataManager.sendMarketSell(instrument_f, size);
                break;
            case LETS_SELL:
                externalDataManager.sendMarketBuy(instrument_f, size);
                externalDataManager.sendMarketSell(instrument_n, size);
                break;
            case NOTHING:
                break;
            default:
                break;
        }
    }

    private void executeGJ() {
        // correcting completion of work!
        isWorking = false;
    }

    private void executeRN(String item, String values) {

    }

    private void executePL(String item, String values) {

    }

    private void prepareToWork() {
        instrument_n = consoleManager.getActualProperties().getProperty("instrument_n");
        instrument_f = consoleManager.getActualProperties().getProperty("instrument_f");

        Money propSpread = Money.dollars(Double.valueOf(consoleManager.getActualProperties().getProperty("spread")));
        Money propDevEnter = Money.dollars(Double.valueOf(consoleManager.getActualProperties().getProperty("for_open_deviation")));
        Money propDevExit = Money.dollars(Double.valueOf(consoleManager.getActualProperties().getProperty("for_close_deviation")));

        enterSpread = propSpread.add(propDevEnter);
        exitSpread = propSpread.add(propDevExit);
    }


}
