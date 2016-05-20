package go.takethespread.fsa;


import go.takethespread.Money;
import go.takethespread.Order;
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

    private LimitOrderMaker lom;
    private MarketOrderMaker mom;


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
        Order.Deal deal_n;
        Order.Deal deal_f;
        int size_n;
        int size_f;
        switch (signal) {
            case M_M_BUY:
                deal_n = Order.Deal.Buy;
                deal_f = Order.Deal.Sell;
                size_n = mom.getOrientedSize(Term.NEAR, Side.ASK);
                size_f = mom.getOrientedSize(Term.FAR, Side.BID);

                mom.hitMarket(size_n, Term.NEAR, deal_n);
                mom.hitMarket(size_f, Term.FAR, deal_f);
                break;
            case L_M_BUY:
                deal_n = Order.Deal.Buy;
                deal_f = Order.Deal.Sell;

                size_n = mom.getOrientedSize(Term.FAR, Side.BID);
                lom.tryingTo(size_n, Term.NEAR, deal_n);

                size_f = lom.getCollectedSize();
                mom.hitMarket(size_f, Term.FAR, deal_f);
                break;
            case M_L_BUY:
                deal_n = Order.Deal.Buy;
                deal_f = Order.Deal.Sell;

                size_f = mom.getOrientedSize(Term.NEAR, Side.ASK);
                lom.tryingTo(size_f, Term.FAR, deal_f);

                size_n = lom.getCollectedSize();
                mom.hitMarket(size_n, Term.NEAR, deal_n);
                break;
            case M_M_SELL:
                deal_n = Order.Deal.Sell;
                deal_f = Order.Deal.Buy;
                size_n = mom.getOrientedSize(Term.NEAR, Side.BID);
                size_f = mom.getOrientedSize(Term.FAR, Side.ASK);

                mom.hitMarket(size_n, Term.NEAR, deal_n);
                mom.hitMarket(size_f, Term.FAR, deal_f);
                break;
            case L_M_SELL:
                deal_n = Order.Deal.Sell;
                deal_f = Order.Deal.Buy;

                size_n = mom.getOrientedSize(Term.FAR, Side.ASK);
                lom.tryingTo(size_n, Term.NEAR, deal_n);

                size_f = lom.getCollectedSize();
                mom.hitMarket(size_f, Term.FAR, deal_f);
                break;
            case M_L_SELL:
                deal_n = Order.Deal.Sell;
                deal_f = Order.Deal.Buy;

                size_f = mom.getOrientedSize(Term.NEAR, Side.BID);
                lom.tryingTo(size_f, Term.FAR, deal_f);

                size_n = lom.getCollectedSize();
                mom.hitMarket(size_n, Term.NEAR, deal_n);
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
        try {
            infoManager = InfoManager.getInstance();
            taskManager = TaskManager.getInstance();
            externalManager = NTTcpExternalManagerImpl.getInstance();

            instrument_n = infoManager.getActualProperties().getProperty("instrument_n");
            instrument_f = infoManager.getActualProperties().getProperty("instrument_f");

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
