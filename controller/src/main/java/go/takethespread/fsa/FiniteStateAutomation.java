package go.takethespread.fsa;


import go.takethespread.Order;
import go.takethespread.managers.ConsoleManager;
import go.takethespread.managers.ExternalManager;
import go.takethespread.managers.TaskManager;
import go.takethespread.exceptions.TradeException;
import go.takethespread.managers.socket.NTTcpExternalManagerImpl;

public class FiniteStateAutomation extends Thread {
    private ConsoleManager consoleManager;
    private TaskManager taskManager;
    private ExternalManager externalManager;
    private TradeBlotter blotter;
    private TradeSystemInfo tradeSystemInfo;

    private MixedOrderMaker mxm;
    private MarketOrderMaker mom;

    private Algorithm algo;

    private volatile boolean isWorking;


    public FiniteStateAutomation() {
        consoleManager = ConsoleManager.getInstance();
        taskManager = TaskManager.getInstance();
        externalManager = NTTcpExternalManagerImpl.getInstance();

        tradeSystemInfo = consoleManager.getTradeSystemInfo();
        blotter = new TradeBlotter(tradeSystemInfo, externalManager);
        algo = new Algorithm(tradeSystemInfo, externalManager, blotter);

        mom = new MarketOrderMaker(blotter, externalManager, consoleManager, tradeSystemInfo);
        mxm = new MixedOrderMaker(blotter, externalManager, consoleManager, mom);

    }

    @Override
    public void run() {
        isWorking = true;
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
        blotter.updateMainInfo();
        Algorithm.Signal signal = algo.getSignal();
        Order.Deal deal_n;
        Order.Deal deal_f;
        int size_n;
        int size_f;
        int size;
        int filled;

        algo.printMe();

        mom.setCurrentPhase(blotter.defineCurrentPhase());
        switch (signal) {
            case M_M_BUY:
                deal_n = Order.Deal.Buy;
                deal_f = Order.Deal.Sell;

                filled = mxm.posEqualize();//h
                size = MarketOrderMaker.calcPairDealSize(
                        mom.getOrientedSize(Term.NEAR, Side.ASK),
                        mom.getOrientedSize(Term.FAR, Side.BID));

                filled += mom.hitMarketOrder(size, Term.NEAR, deal_n); //h
                mom.hitMarketOrder(size, Term.FAR, deal_f);
                mom.updateMaxSize(filled);
                break;
            case L_M_BUY:
                deal_n = Order.Deal.Buy;
                size_n = mom.getOrientedSize(Term.FAR, Side.BID);

                mxm.placeLimitOrder(size_n, Term.NEAR, deal_n);
                break;
            case M_L_BUY:
                deal_f = Order.Deal.Sell;
                size_f = mom.getOrientedSize(Term.NEAR, Side.ASK);

                mxm.placeLimitOrder(size_f, Term.FAR, deal_f);
                break;
            case M_M_SELL:
                deal_n = Order.Deal.Sell;
                deal_f = Order.Deal.Buy;

                filled = mxm.posEqualize();
                size = MarketOrderMaker.calcPairDealSize(
                        mom.getOrientedSize(Term.NEAR, Side.BID),
                        mom.getOrientedSize(Term.FAR, Side.ASK));

                mom.hitMarketOrder(size, Term.NEAR, deal_n);
                filled += mom.hitMarketOrder(size, Term.FAR, deal_f);
                mom.updateMaxSize(filled);
                break;
            case L_M_SELL:
                deal_n = Order.Deal.Sell;
                size_n = mom.getOrientedSize(Term.FAR, Side.ASK);

                mxm.placeLimitOrder(size_n, Term.NEAR, deal_n);
                break;
            case M_L_SELL:
                deal_f = Order.Deal.Buy;
                size_f = mom.getOrientedSize(Term.NEAR, Side.BID);

                mxm.placeLimitOrder(size_f, Term.FAR, deal_f);
                break;
            case NOTHING:
                filled = mxm.posEqualize();
                mom.updateMaxSize(filled);
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


}
