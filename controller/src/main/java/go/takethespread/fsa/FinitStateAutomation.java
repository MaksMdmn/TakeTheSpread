package go.takethespread.fsa;


import go.takethespread.Money;
import go.takethespread.managers.ConsoleManager;
import go.takethespread.managers.ExternalManager;
import go.takethespread.managers.TaskManager;
import go.takethespread.managers.exceptions.TradeException;
import go.takethespread.managers.socket.NTTcpExternalManagerImpl;

public class FinitStateAutomation extends Thread {
    private ConsoleManager consoleManager;
    private TaskManager taskManager;
    private ExternalManager externalManager;

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
        TaskManager.TradeTask currentTask = null;

        while (isWorking) {
            try {
                if (currentTask == null) {
                    while (currentTask == null) {
                        currentTask = taskManager.getCurrentTask();
                    }
                } else {
                    currentTask = taskManager.getCurrentTask();
                }


                System.out.println("COMMANDOS: " + currentTask.getCommand().toString());

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

                System.out.println(algo.toString());

            } catch (TradeException e) {
                e.printStackTrace();
            }

        }
    }

    private void executeGO() {
        Algorithm.Signal signal = algo.getSignal();
        switch (signal) {
            case LETS_BUY:
                externalManager.sendMarketBuy(instrument_n, size);
                externalManager.sendMarketSell(instrument_f, size);
                break;
            case LETS_SELL:
                externalManager.sendMarketBuy(instrument_f, size);
                externalManager.sendMarketSell(instrument_n, size);
                break;
            case NOTHING:
                break;
            default:
                break;
        }
    }

    private void executeGJ() {
        // correcting completion of work!
        externalManager.finishingJob();
        isWorking = false;
    }

    private void executeRN(String item, String values) {

    }

    private void executePL(String item, String values) {

    }

    private void prepareToWork() {
        consoleManager = ConsoleManager.getInstance();
        taskManager = TaskManager.getInstance();
        externalManager = NTTcpExternalManagerImpl.getInstance();

        instrument_n = consoleManager.getActualProperties().getProperty("instrument_n");
        instrument_f = consoleManager.getActualProperties().getProperty("instrument_f");
        size = Integer.valueOf(consoleManager.getActualProperties().getProperty("deal_size"));

        Money propDevEnter = Money.dollars(Double.valueOf(consoleManager.getActualProperties().getProperty("for_open_deviation")));
        Money propDevExit = Money.dollars(Double.valueOf(consoleManager.getActualProperties().getProperty("for_close_deviation")));

        enterSpread = Money.dollars(Double.valueOf(consoleManager.getActualProperties().getProperty("spread_open")));
        enterSpread = enterSpread.add(propDevEnter);
        exitSpread = Money.dollars(Double.valueOf(consoleManager.getActualProperties().getProperty("spread_close")));
        exitSpread = exitSpread.add(propDevExit);

        algo = new Algorithm(instrument_n, instrument_f, enterSpread, exitSpread, externalManager);
    }


}
