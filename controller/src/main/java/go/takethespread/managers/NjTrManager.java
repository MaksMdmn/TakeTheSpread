package go.takethespread.managers;

import go.takethespread.Money;
import go.takethespread.NjTr.*;

public class NjTrManager {

    private static NjTrManager instance;

    private NjTrManager() {
    }

    public static NjTrManager getInstance() {
        if (instance == null) {
            instance = new NjTrManager();
        }

        return instance;
    }


    public Money getLastPrice(){
        return null;
    }

    public Money getBestBid(){
        return null;

    }

    public Money getBestAsk(){
        return null;
    }

    public void sendCancelOrder(int cancelId) {
        new NjTrOrder.Builder(cancelId, NjTrCommand.CANCEL)
                .build()
                .doOrder();
    }

    public void sendCancelAllOrders() {
        new NjTrOrder.Builder(-13, NjTrCommand.CANCELALLORDERS)
                .build()
                .doOrder();
    }

    public void sendBuyLimitOrder(String account, String instrument, double price, int quantity) {
        new NjTrOrder.Builder(-100, NjTrCommand.PLACE)
                .account(account)
                .instrument(instrument)
                .action(NjTrAction.BUY)
                .quantity(quantity)
                .orderType(NjTrOrderType.LIMIT)
                .limitPrice(price)
                .timeInForce(NjTrTIF.GTC)
                .build()
                .doOrder();
    }

    public void sendBuyMarketOrder(String account, String instrument,int quantity) {
        new NjTrOrder.Builder(-100, NjTrCommand.PLACE)
                .account(account)
                .instrument(instrument)
                .action(NjTrAction.BUY)
                .quantity(quantity)
                .orderType(NjTrOrderType.MARKET)
                .timeInForce(NjTrTIF.GTC)
                .build()
                .doOrder();
    }

    public void sendSellLimitOrder(String account, String instrument, double price, int quantity) {
        new NjTrOrder.Builder(-100, NjTrCommand.PLACE)
                .account(account)
                .instrument(instrument)
                .action(NjTrAction.SELL)
                .quantity(quantity)
                .orderType(NjTrOrderType.LIMIT)
                .limitPrice(price)
                .timeInForce(NjTrTIF.GTC)
                .build()
                .doOrder();
    }

    public void sendSellMarketOrder(String account, String instrument,int quantity) {
        new NjTrOrder.Builder(-100, NjTrCommand.PLACE)
                .account(account)
                .instrument(instrument)
                .action(NjTrAction.SELL)
                .quantity(quantity)
                .orderType(NjTrOrderType.MARKET)
                .timeInForce(NjTrTIF.GTC)
                .build()
                .doOrder();
    }

    public void sendReplaceOrder(int replaceOrderId, double price, int quantity) {
        new NjTrOrder.Builder(replaceOrderId, NjTrCommand.CHANGE)
                .quantity(quantity)
                .limitPrice(price)
                .build()
                .doOrder();
    }

}
