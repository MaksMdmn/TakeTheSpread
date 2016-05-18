package go.takethespread.fsa;


import go.takethespread.Money;
import go.takethespread.managers.InfoManager;
import go.takethespread.managers.ExternalManager;
import go.takethespread.managers.TaskManager;
import go.takethespread.exceptions.TradeException;
import go.takethespread.managers.socket.NTTcpExternalManagerImpl;

public class FinitStateAutomation extends Thread {
    private InfoManager infoManager;
    private TaskManager taskManager;
    private ExternalManager externalManager;
    private TradeBlotter blotter;

    private String instrument_n;
    private String instrument_f;
    private Money enterSpread;
    private Money exitSpread;
    private int size;

    private Algorithm algo;

    private volatile boolean isWorking;

    @Override
    public void run() {
        isWorking = true;
        prepareToWork();
        TaskManager.TradeTask currentTask;

        while (isWorking) {
            try {
                do {
                    currentTask = taskManager.getCurrentTask();
                } while (currentTask == null);

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
        Algorithm.Signal signal = algo.getSignal();
//        switch (signal) {
//            case TRY_BUY:
//                externalManager.sendMarketBuy(instrument_n, size);
//                externalManager.sendMarketSell(instrument_f, size);
//                break;
//            case TRY_SELL:
//                externalManager.sendMarketBuy(instrument_f, size);
//                externalManager.sendMarketSell(instrument_n, size);
//                break;
//            case NOTHING:
//                break;
//            default:
//                break;
//        }
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
        try {
            infoManager = InfoManager.getInstance();
            taskManager = TaskManager.getInstance();
            externalManager = NTTcpExternalManagerImpl.getInstance();

            instrument_n = infoManager.getActualProperties().getProperty("instrument_n");
            instrument_f = infoManager.getActualProperties().getProperty("instrument_f");
            size = Integer.valueOf(infoManager.getActualProperties().getProperty("favorable_size"));

            Money propDevEnter = Money.dollars(Double.valueOf(infoManager.getActualProperties().getProperty("entering_deviation")));
            Money propDevExit = Money.dollars(Double.valueOf(infoManager.getActualProperties().getProperty("leaving_deviation")));

            enterSpread = Money.dollars(Double.valueOf(infoManager.getActualProperties().getProperty("entering_spread")));
            enterSpread = enterSpread.add(propDevEnter);
            exitSpread = Money.dollars(Double.valueOf(infoManager.getActualProperties().getProperty("leaving_spread")));
            exitSpread = exitSpread.add(propDevExit);

            blotter = new TradeBlotter(instrument_n, instrument_f, externalManager);
            algo = new Algorithm(enterSpread, exitSpread, externalManager, blotter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
