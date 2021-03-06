package go.takethespread.fsa;


import go.takethespread.Money;
import go.takethespread.Side;
import go.takethespread.Term;
import go.takethespread.managers.*;
import go.takethespread.util.ClassNameUtil;
import go.takethespread.Order;
import go.takethespread.exceptions.TradeException;
import go.takethespread.managers.socket.NTTcpExternalManagerImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

public class FiniteStateAutomation extends Thread {
    private TaskManager taskManager;
    private ExternalManager externalManager;
    private TradeBlotter blotter;
    private TradeSystemInfo tradeSystemInfo;
    private InfoManager infoManager;
    private StatusManager statusManager;

    private LimitMaker lm;
    private LimitMaker lm_additional;
    private MarketMaker mm;
    private PositionWatcher pw;
    private Algorithm algo;

    private volatile boolean isWorking;

    private static final Logger logger = LogManager.getLogger(ClassNameUtil.getCurrentClassName());

    public FiniteStateAutomation() {
        logger.info("creation of FSA, logger started");

        tradeSystemInfo = TradeSystemInfo.getInstance();

        taskManager = TaskManager.getInstance();
        externalManager = NTTcpExternalManagerImpl.getInstance();

        blotter = new TradeBlotter(tradeSystemInfo, externalManager);

        infoManager = InfoManager.getInstance();
        infoManager.setBlotter(blotter);

        statusManager = StatusManager.getInstance();

        lm = new LimitMaker(externalManager, blotter);
        mm = new MarketMaker(externalManager, blotter);

        switch (tradeSystemInfo.current_tactics) {
            case 0:
                logger.debug("MODE IS: ____________CLASSIC____________");
                algo = new Algorithm_Classic(tradeSystemInfo, externalManager, blotter);
                pw = new PositionWatcher(blotter, mm, lm);
                break;
            case 1:
                logger.debug("MODE IS: ____________LIMIT ENTERING____________");
                algo = new Algorithm_LimitEntering(tradeSystemInfo, externalManager, blotter);
                pw = new PositionWatcher(blotter, mm, lm);
                break;
            case 2:
                logger.debug("MODE IS: ____________START IN MARKET____________");
                algo = new Algorithm_StartInMarket(tradeSystemInfo, externalManager, blotter);
                lm_additional = new LimitMaker(externalManager, blotter);
                pw = new PositionWatcher(blotter, mm, lm, lm_additional);
                break;
            default:
                break;
        }


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

                if (!blotter.isMarketDataCorrect()) {
                    continue;
                }

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
                    case OU:
                        executeOU();
                        break;
                    default:
                        break;
                }
                logger.info(currentTask.getCommand() + " executed");

//            } catch (TradeException e) {
//                e.printStackTrace();
            } catch (Exception e) {
                logger.debug(e + " " + Arrays.toString(e.getStackTrace()));
                logger.error(e);
                logger.error(Arrays.toString(e.getStackTrace()));
                executeGJ();
            }
        }
    }

    private void executeGO() {
        logger.info("execute GO");

        blotter.updateMarketData();
        statusManager.receivedMarketData(blotter);

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
        int tempSize;
        Money tempPrice;
        switch (tradeSystemInfo.current_tactics) {
            case 0:
                logger.debug("MODE: __________________CLASSIC__________________");
                switch (signal) {
                    case M_M_BUY:
                        if (lm.cancelOrderAndGetFilled() == 0) {
                            size_n = mm.defineMaxMarketSize(Term.NEAR, Side.ASK);
                            size_f = mm.defineMaxMarketSize(Term.FAR, Side.BID);
                            tempSize = MarketMaker.choosePairDealSize(size_n, size_f);
                            size = pw.defineMaxPossibleSize(tempSize);

                            mm.hitPairOrdersToMarket(size, Term.NEAR, size, Term.FAR);
                        }
                        break;
                    case L_M_BUY:
                        tempSize = mm.defineMaxMarketSize(Term.FAR, Side.BID);
                        size = pw.defineMaxPossibleSize(tempSize);

                        lm.rollLimitOrder(size, Term.NEAR, Order.Deal.Buy);
                        break;
                    case M_L_BUY:
                        tempSize = mm.defineMaxMarketSize(Term.NEAR, Side.ASK);
                        size = pw.defineMaxPossibleSize(tempSize);

                        lm.rollLimitOrder(size, Term.FAR, Order.Deal.Sell);
                        break;
                    case M_M_SELL:
                        if (lm.cancelOrderAndGetFilled() == 0) {
                            size_n = mm.defineMaxMarketSize(Term.NEAR, Side.BID);
                            size_f = mm.defineMaxMarketSize(Term.FAR, Side.ASK);
                            tempSize = MarketMaker.choosePairDealSize(size_n, size_f);

                            size = pw.defineMaxPossibleSize(tempSize);

                            mm.hitPairOrdersToMarket(size, Term.FAR, size, Term.NEAR);
                        }
                        break;
                    case L_M_SELL:
                        tempSize = mm.defineMaxMarketSize(Term.FAR, Side.ASK);
                        size = pw.defineMaxPossibleSize(tempSize);

                        lm.rollLimitOrder(size, Term.NEAR, Order.Deal.Sell);
                        break;
                    case M_L_SELL:
                        tempSize = mm.defineMaxMarketSize(Term.NEAR, Side.BID);
                        size = pw.defineMaxPossibleSize(tempSize);

                        lm.rollLimitOrder(size, Term.FAR, Order.Deal.Buy);
                        break;
                    case NOTHING:
                        if (lm.isOrderPlaced()) {
                            lm.cancelOrderAndGetFilled();
                        }
                        break;
                    default:
                        break;
                }
                break;
            case 1:
                logger.debug("MODE: __________________LIMIT_ENTERING__________________");
                switch (signal) {
                    case L_M_BUY:
                        tempSize = mm.defineMaxMarketSize(Term.FAR, Side.BID);
                        size = pw.defineMaxPossibleSize(tempSize);
                        tempPrice = blotter.getSpreadCalculator().getGuideValue().subtract(tradeSystemInfo.entering_dev);

                        lm.rollLimitOrderIncludingPriceInput(
                                size,
                                Term.NEAR,
                                Order.Deal.Buy,
                                tempPrice);

                        logger.debug("ENTERING ORDER PLACED AT LIMIT PRICE: " + tempPrice.getAmount() + " GUIDE PRICE: " + blotter.getSpreadCalculator().getGuideValue().getAmount());
                        break;
                    case L_M_SELL:
                        tempSize = mm.defineMaxMarketSize(Term.FAR, Side.ASK);
                        size = pw.defineMaxPossibleSize(tempSize);
                        tempPrice = blotter.getSpreadCalculator().getGuideValue().add(tradeSystemInfo.entering_dev);

                        lm.rollLimitOrderIncludingPriceInput(
                                size,
                                Term.NEAR,
                                Order.Deal.Sell,
                                tempPrice);
                        logger.debug("ENTERING ORDER PLACED AT LIMIT PRICE: " + tempPrice.getAmount() + " GUIDE PRICE: " + blotter.getSpreadCalculator().getGuideValue().getAmount());
                        break;
                    case M_L_SELL:
                        tempSize = mm.defineMaxMarketSize(Term.NEAR, Side.BID);
                        size = pw.defineMaxPossibleSize(tempSize);

                        lm.rollLimitOrder(
                                size,
                                Term.FAR,
                                Order.Deal.Buy);
                        break;
                    case M_L_BUY:
                        tempSize = mm.defineMaxMarketSize(Term.NEAR, Side.ASK);
                        size = pw.defineMaxPossibleSize(tempSize);

                        lm.rollLimitOrder(
                                size,
                                Term.FAR,
                                Order.Deal.Sell);
                        break;
                    case NOTHING:
                        break;
                    default:
                        break;
                }
                break;
            case 2:
                logger.debug("MODE: __________________START_IN_MARKET__________________");
                switch (signal) {
                    case L_M_SELL:
                        tempSize = mm.defineMaxMarketSize(Term.FAR, Side.ASK);
                        size = pw.defineMaxPossibleSize(tempSize);
                        tempPrice = blotter.getAsk_n();

                        lm.rollLimitOrderIncludingPriceInput(
                                size,
                                Term.NEAR,
                                Order.Deal.Sell,
                                tempPrice);
                        logger.debug("ENTER ORDER PLACED AT LIMIT PRICE: " + tempPrice.getAmount());
                        break;

                    case L_M_BUY:
                        tempSize = mm.defineMaxMarketSize(Term.FAR, Side.BID);
                        size = pw.defineMaxPossibleSize(tempSize);
                        tempPrice = blotter.getBid_n();

                        lm.rollLimitOrderIncludingPriceInput(
                                size,
                                Term.NEAR,
                                Order.Deal.Buy,
                                tempPrice);

                        logger.debug("ENTER ORDER PLACED AT LIMIT PRICE: " + tempPrice.getAmount());
                        break;
                    case L_L_BUY:
                        tempSize = mm.choosePairDealSize(
                                mm.defineMaxMarketSize(Term.NEAR, Side.ASK),
                                mm.defineMaxMarketSize(Term.FAR, Side.BID));
                        size = pw.defineMaxPossibleSize(tempSize);

                        lm.rollLimitOrderIncludingPriceInput(
                                size,
                                Term.NEAR,
                                Order.Deal.Buy,
                                blotter.getSpreadCalculator().getExitPointNear());

                        lm_additional.rollLimitOrderIncludingPriceInput(
                                size,
                                Term.FAR,
                                Order.Deal.Sell,
                                blotter.getSpreadCalculator().getExitPointFar()
                        );
                        break;
                    case L_L_SELL:
                        tempSize = mm.choosePairDealSize(
                                mm.defineMaxMarketSize(Term.NEAR, Side.BID),
                                mm.defineMaxMarketSize(Term.FAR, Side.ASK));
                        size = pw.defineMaxPossibleSize(tempSize);

                        lm.rollLimitOrderIncludingPriceInput(
                                size,
                                Term.NEAR,
                                Order.Deal.Sell,
                                blotter.getSpreadCalculator().getExitPointNear());

                        lm_additional.rollLimitOrderIncludingPriceInput(
                                size,
                                Term.FAR,
                                Order.Deal.Buy,
                                blotter.getSpreadCalculator().getExitPointFar()
                        );
                        break;

                    case NOTHING:
                        if (lm.isOrderPlaced()) {
                            lm.cancelOrderAndGetFilled();
                        }
                        if (lm_additional.isOrderPlaced()) {
                            lm_additional.cancelOrderAndGetFilled();
                        }
                        break;
                    default:
                        break;
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
            taskManager.removeAllTasks();
            writeInformationToDataBaseWhenFinishing();
        } catch (TradeException e) {
            e.printStackTrace();
        }
    }

    private void executeOF() {
        writeInformationToDataBaseWhenFinishing();
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

    private void executeOU() {
        try {
            taskManager.pollTask();
        } catch (TradeException e) {
            e.printStackTrace();
        }
        blotter.updateOrdersData();
    }

    private void writeInformationToDataBaseWhenFinishing() {
        if (lm.isOrderPlaced()) {
            externalManager.sendCancelOrder(lm.getRollingOrderId());
        }
        blotter.updateOrdersData();
        statusManager.ordersInfoUpdated(blotter.getOrders());//to DB
        statusManager.restoreOrdersInfoStatus();

        MarketDataCollector.getInstance().pushToDataBase();
    }
}
