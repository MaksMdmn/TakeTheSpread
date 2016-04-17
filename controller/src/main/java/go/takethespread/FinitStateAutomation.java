package go.takethespread;


import go.takethespread.managers.ConsoleManager;
import go.takethespread.managers.TradeTaskManager;

public class FinitStateAutomation extends Thread {
    private ConsoleManager consoleManager;
    private TradeTaskManager tradeTaskManager;

    private boolean isWorking;

    private String nFuture;
    private String fFuture;
    private String settingAccount;
    private Money settingSpread;
    private int settingSize;

//need to receive basic values from config file

//    @Override
//    public void run() {
//        isWorking = true;
//        if (nFuture == null || fFuture == null) {
////                throw new IllegalAccessException("Please, enter the nFuture name: " + nFuture);
//            return;
//        }
//
//        TradeTaskManager.TradeTask currentTask;
//        consoleManager = ConsoleManager.getInstance();
//        ntPlatformManager = NTPlatformManager.getInstance();
//        tradeTaskManager = TradeTaskManager.getInstance();
//
//        try {
//            ntPlatformManager.startWorkingWithNT();
//
//            while (isWorking) {
//                try {
//                    currentTask = tradeTaskManager.getCurrentTask();
//
//                    switch (currentTask.getCommand()) {
//                        case GO:
//                            //standart logical: check prices, waiting for signal and when signal - do the deal
//                            break;
//                        case GJ:
//                            // break the cycle
//                            break;
//                        case RN:
//                            executeRN(currentTask.getItem(), currentTask.getValues());
//                            // return the item and handle the values
//                            break;
//                        case PL:
//                            executePL(currentTask.getItem(), currentTask.getValues());
//                            // change the item and handle the values
//                            break;
//                        default:
//
//                            break;
//                    }
//
//                } catch (TradeException e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//            ntPlatformManager.endWorkingWithNT();
//        } catch (TradeException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void executeGo() {
//        Money nLast = ntPlatformManager.getLastPrice(nFuture);
//        Money nBid = ntPlatformManager.getBestBid(nFuture);
//        Money nAsk = ntPlatformManager.getBestAsk(nFuture);
//        Money fLast = ntPlatformManager.getLastPrice(fFuture);
//        Money fBid = ntPlatformManager.getBestBid(fFuture);
//        Money fAsk = ntPlatformManager.getBestAsk(fFuture);
//
//        Money currentSpread;
//
//        // check in poisiton or not
//        if (nLast.lessThan(fLast)) {
//            currentSpread = fBid.subtract(nAsk);
//
//            if (currentSpread.greaterThan(settingSpread)) {
//                // AND CHECK THE SIZE!!!
//                ntPlatformManager.sendCancelAllOrders();
//                // prices is incorrect !!!
//                ntPlatformManager.sendBuyLimitOrder(new Order());
//                ntPlatformManager.sendSellLimitOrder(new Order());
//
//                //need to check the position!
//                //need to check orders!
//            }
//
//        } else {
//            currentSpread = nBid.subtract(fAsk);
//
//            if (currentSpread.greaterThan(settingSpread)) {
//                // AND CHECK THE SIZE!!!
//                ntPlatformManager.sendCancelAllOrders();
//                // prices is incorrect !!!
//                ntPlatformManager.sendSellLimitOrder(new Order());
//                ntPlatformManager.sendBuyLimitOrder(new Order());
//
//                //need to check the position!
//                //need to check orders!
//            }
//        }
//    }
//
//    private void executeGJ() {
//        // correcting completion of work!
//        isWorking = false;
//    }
//
//    private void executeRN(String item, String values) {
//
//    }
//
//    private void executePL(String item, String values) {
//
//    }
}
