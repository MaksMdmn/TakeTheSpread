package go.takethespread.fsa;


import go.takethespread.ClassNameUtil;
import go.takethespread.Order;
import go.takethespread.managers.ConsoleManager;
import go.takethespread.managers.ExternalManager;
import go.takethespread.managers.TaskManager;
import go.takethespread.exceptions.TradeException;
import go.takethespread.managers.socket.NTTcpExternalManagerImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FiniteStateAutomation extends Thread {
    private ConsoleManager consoleManager;
    private TaskManager taskManager;
    private ExternalManager externalManager;
    private TradeBlotter blotter;
    private TradeSystemInfo tradeSystemInfo;

    private LimitMaker lm;
    private MarketMaker mm;
    private PositionWatcher pw;
    private Algorithm algo;

    private volatile boolean isWorking;

    private static final Logger logger = LogManager.getLogger(ClassNameUtil.getCurrentClassName());

    public FiniteStateAutomation() {
        logger.info("creation of FSA, logger started");

        consoleManager = ConsoleManager.getInstance();
        taskManager = TaskManager.getInstance();
        externalManager = NTTcpExternalManagerImpl.getInstance();

        tradeSystemInfo = consoleManager.getTradeSystemInfo();
        blotter = new TradeBlotter(tradeSystemInfo, externalManager);
        algo = new Algorithm(tradeSystemInfo, externalManager, blotter);

        lm = new LimitMaker(externalManager, blotter);
        mm = new MarketMaker(externalManager, blotter);
        pw = new PositionWatcher(blotter, mm);

        logger.info("creation of FSA success");
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

                logger.debug("current command: " + currentTask.getCommand());

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
            } catch (Exception e) {
                logger.error(e);
                executeGJ();
            }
        }
    }

    private void executeGO() {
        logger.info("execute GO");

        blotter.updateMarketData();
        blotter.updatePositionData();

        logger.debug("market and pos data updated");
        logger.debug("is pos equal: + " + blotter.getPosition_n() + " " + blotter.getPosition_f() + " " + pw.isPosEqual());

        if (pw.isPosEqual()) {
            pw.equalizePositions();
            blotter.updatePositionData();
        }

        blotter.updateAuxiliaryData();

        logger.debug("auxiliary data updated");

        Algorithm.Signal signal = algo.getSignal();

        logger.info("current signal: " + signal);

        int size;
        int size_n;
        int size_f;
        int tmpSize;

        switch (signal) {
            case M_M_BUY:
                size_n = mm.defineMaxMarketSize(Term.NEAR, Side.ASK);
                size_f = mm.defineMaxMarketSize(Term.FAR, Side.BID);
                tmpSize = MarketMaker.choosePairDealSize(size_n, size_f);
                size = pw.defineMaxPossibleSize(tmpSize);

                mm.hitOrderToMarket(size, Term.NEAR, Order.Deal.Buy);
                mm.hitOrderToMarket(size, Term.FAR, Order.Deal.Sell);
                break;
            case L_M_BUY:
                tmpSize = mm.defineMaxMarketSize(Term.FAR, Side.BID);
                size = pw.defineMaxPossibleSize(tmpSize);

                lm.rollLimitOrder(size, Term.NEAR, Order.Deal.Buy);
                break;
            case M_L_BUY:
                tmpSize = mm.defineMaxMarketSize(Term.NEAR, Side.ASK);
                size = pw.defineMaxPossibleSize(tmpSize);

                lm.rollLimitOrder(size, Term.FAR, Order.Deal.Sell);
                break;
            case M_M_SELL:
                size_n = mm.defineMaxMarketSize(Term.NEAR, Side.BID);
                size_f = mm.defineMaxMarketSize(Term.FAR, Side.ASK);
                tmpSize = MarketMaker.choosePairDealSize(size_n, size_f);

                size = pw.defineMaxPossibleSize(tmpSize);

                mm.hitOrderToMarket(size, Term.NEAR, Order.Deal.Sell);
                mm.hitOrderToMarket(size, Term.FAR, Order.Deal.Buy);
                break;
            case L_M_SELL:
                tmpSize = mm.defineMaxMarketSize(Term.FAR, Side.ASK);
                size = pw.defineMaxPossibleSize(tmpSize);

                lm.rollLimitOrder(size, Term.NEAR, Order.Deal.Sell);
                break;
            case M_L_SELL:
                tmpSize = mm.defineMaxMarketSize(Term.NEAR, Side.BID);
                size = pw.defineMaxPossibleSize(tmpSize);

                lm.rollLimitOrder(size, Term.FAR, Order.Deal.Buy);
                break;
            case NOTHING:
                //?????? what should I do here?
                break;
            default:
                break;
        }
        logger.info("GO executed, pos status: " + blotter.getPosition_n() + " " + blotter.getPosition_f());
    }

    private void executeGJ() {
        // correcting completion of work!
        logger.info("execute GJ");
        externalManager.finishingJob();
        isWorking = false;
        logger.info("GJ executed");
    }

    private void executeRN(String item, String values) {

    }

    private void executePL(String item, String values) {

    }


}
