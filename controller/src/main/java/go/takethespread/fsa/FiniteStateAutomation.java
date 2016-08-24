package go.takethespread.fsa;


import go.takethespread.Side;
import go.takethespread.Term;
import go.takethespread.util.ClassNameUtil;
import go.takethespread.Order;
import go.takethespread.exceptions.TradeException;
import go.takethespread.managers.ConsoleManager;
import go.takethespread.managers.ExternalManager;
import go.takethespread.managers.InfoManager;
import go.takethespread.managers.TaskManager;
import go.takethespread.managers.socket.NTTcpExternalManagerImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

public class FiniteStateAutomation extends Thread {
    private ConsoleManager consoleManager;
    private TaskManager taskManager;
    private ExternalManager externalManager;
    private TradeBlotter blotter;
    private TradeSystemInfo tradeSystemInfo;
    private InfoManager infoManager;

    private LimitMaker lm;
    private MarketMaker mm;
    private PositionWatcher pw;
    private Algorithm algo;

    private volatile boolean isWorking;

    private static final Logger logger = LogManager.getLogger(ClassNameUtil.getCurrentClassName());

    public FiniteStateAutomation() {
        logger.info("creation of FSA, logger started");

        tradeSystemInfo = TradeSystemInfo.getInstance();
        tradeSystemInfo.initProp();

        consoleManager = ConsoleManager.getInstance();
        taskManager = TaskManager.getInstance();
        externalManager = NTTcpExternalManagerImpl.getInstance();

        blotter = new TradeBlotter(tradeSystemInfo, externalManager);

        infoManager = InfoManager.getInstance();
        infoManager.setBlotter(blotter);

        algo = new Algorithm(tradeSystemInfo, externalManager, blotter);

        lm = new LimitMaker(externalManager, blotter);
        mm = new MarketMaker(externalManager, blotter);
        pw = new PositionWatcher(blotter, mm);

        logger.info("creation of FSA success");
    }

    public TradeBlotter testPhonyGetterBlotter() {
        return blotter;
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

                logger.info("execute " + currentTask.getCommand());
                switch (currentTask.getCommand()) {
                    case GO:
                        executeGO();
                        break;
                    case CN:
                        executeCN();
                        break;
                    case GJ:
                        executeGJ();
                        break;
                    case OF:
                        executeOF();
                        break;
                    case TT:
                        executeTT();
                        break;
                    case LN:
                        executeLN();
                        break;
                    case RS:
                        executeRS();
                        break;
                    case OS:
                        executeOS();
                        break;
                    case BS:
                        executeBS();
                        break;
                    default:
                        break;
                }
                logger.info(currentTask.getCommand() + " executed");

//            } catch (TradeException e) {
//                e.printStackTrace();
            } catch (Exception e) {
                logger.error(e);
                logger.error(Arrays.toString(e.getStackTrace()));
                executeGJ();
            }
        }
    }

    private void executeGO() {
        logger.info("execute GO");

        blotter.updateMarketData();
        blotter.updatePositionData();

        logger.info("market and pos data updated");
        logger.debug("is pos equal: " + blotter.getPosition_n() + " " + blotter.getPosition_f() + " " + pw.isPosEqual());

        if (pw.isPosEqual()) {
            pw.updateEqualAndRelevantPos();
        } else {
            pw.equalizePositions();

        }
        blotter.updateAuxiliaryData();

        logger.info("auxiliary data updated");

        Algorithm.Signal signal = algo.getSignal();

        logger.debug("current signal: " + signal);

        int size;
        int size_n;
        int size_f;
        int tmpSize;

        switch (signal) {
            case M_M_BUY:
                if (lm.cancelOrderSize() == 0) {
                    size_n = mm.defineMaxMarketSize(Term.NEAR, Side.ASK);
                    size_f = mm.defineMaxMarketSize(Term.FAR, Side.BID);
                    tmpSize = MarketMaker.choosePairDealSize(size_n, size_f);
                    size = pw.defineMaxPossibleSize(tmpSize);

                    mm.hitOrderToMarket(size, Term.NEAR, Order.Deal.Buy);
                    mm.hitOrderToMarket(size, Term.FAR, Order.Deal.Sell);
                }
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
                if (lm.cancelOrderSize() == 0) {
                    size_n = mm.defineMaxMarketSize(Term.NEAR, Side.BID);
                    size_f = mm.defineMaxMarketSize(Term.FAR, Side.ASK);
                    tmpSize = MarketMaker.choosePairDealSize(size_n, size_f);

                    size = pw.defineMaxPossibleSize(tmpSize);

                    mm.hitOrderToMarket(size, Term.NEAR, Order.Deal.Sell);
                    mm.hitOrderToMarket(size, Term.FAR, Order.Deal.Buy);
                }
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
                if (lm.isOrderPlaced()) {
                    lm.cancelOrderSize();
                }
                break;
            default:
                break;
        }
        logger.info("GO executed, pos status: " + blotter.getPosition_n() + " " + blotter.getPosition_f());
    }

    private void executeCN() {
        try {
            taskManager.pollTask();
        } catch (TradeException e) {
            e.printStackTrace();
        }
        externalManager.startingJob(tradeSystemInfo.host, tradeSystemInfo.port);
    }

    private void executeGJ() {
        // correct completion of work!
        try {
            externalManager.sendCancelOrders();
            taskManager.removeAllTasks();
        } catch (TradeException e) {
            e.printStackTrace();
        }
    }

    private void executeOF() {
        externalManager.finishingJob();
        isWorking = false;
    }

    private void executeTT() {

    }

    private void executeOS() {

    }

    private void executeBS() {

    }

    private void executeLN() {
    }

    private void executeRS() {

    }

}
