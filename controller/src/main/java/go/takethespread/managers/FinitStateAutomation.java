package go.takethespread.managers;


import go.takethespread.Money;
import go.takethespread.managers.exceptions.TradeException;

public class FinitStateAutomation extends Thread {
    private ConsoleManager consoleManager;
    private NjTrManager njTrManager;
    private TradeManager tradeManager;

    private String nFuture;
    private String fFuture;
    private String settingAccount;
    private Money settingSpread;
    private int settingSize;

//need to recive baisic values from config file

    @Override
    public void run() {
        if (nFuture == null || fFuture == null) {
//                throw new IllegalAccessException("Please, enter the nFuture name: " + nFuture);
            return;
        }

        TradeManager.TradeTask currentTask;
        consoleManager = ConsoleManager.getInstance();
        njTrManager = NjTrManager.getInstance();
        tradeManager = TradeManager.getInstance();

        while (true) {
            try {
                currentTask = tradeManager.getCurrentTask();

                switch (currentTask.getCommand()) {
                    case GO:
                        //standart logical: check prices, waiting for signal and when signal - do the deal
                        break;
                    case GJ:
                        // break the cycle
                        break;
                    case RN:
                        // return the item and handle the values
                        break;
                    case PL:
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
        Money nLast = njTrManager.getLastPrice(nFuture);
        Money fLast = njTrManager.getLastPrice(fFuture);
        Money nBid = njTrManager.getBestBid(nFuture);
        Money fBid = njTrManager.getBestBid(fFuture);
        Money nAsk = njTrManager.getBestAsk(nFuture);
        Money fAsk = njTrManager.getBestAsk(fFuture);

        Money currentSpread;

        // check in poisiton or not
        if (nLast.lessThan(fLast)) {
            currentSpread = fBid.subtract(nAsk);

            if(currentSpread.greaterThan(settingSpread)){
                // AND CHECK THE SIZE!!!
                njTrManager.sendCancelAllOrders();
                // prices is incorrect !!!
                njTrManager.sendBuyLimitOrder(settingAccount, nFuture, nAsk.getAmount(), settingSize);
                njTrManager.sendSellLimitOrder(settingAccount, fFuture, fBid.getAmount(), settingSize);

                //need to check the position!
                //need to check orders!
            }

        } else {
            currentSpread = nBid.subtract(fAsk);

            if(currentSpread.greaterThan(settingSpread)){
                // AND CHECK THE SIZE!!!
                njTrManager.sendCancelAllOrders();
                // prices is incorrect !!!
                njTrManager.sendSellLimitOrder(settingAccount, nFuture, nBid.getAmount(), settingSize);
                njTrManager.sendBuyLimitOrder(settingAccount, fFuture, fAsk.getAmount(), settingSize);

                //need to check the position!
                //need to check orders!
            }
        }


    }
}
