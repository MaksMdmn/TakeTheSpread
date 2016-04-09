package go.takethespread.managers;

import go.takethespread.Money;
import go.takethespread.NT.*;

import java.util.List;

public class NTPlatformManager {

    private static NTPlatformManager instance;

    private NTPlatformManager() {
    }

    public static NTPlatformManager getInstance() {
        if (instance == null) {
            instance = new NTPlatformManager();
        }

        return instance;
    }

    public static NTOrder createOrder(Money price, int size, NTAction action, NTOrderType type) {
        NTOrder resultOrder = new NTOrder();
        String id = instance.getNewOrderId();
        //<will be in settings
        String account = "";
        String instrument = "";
        //will be in settings>
        NTCommand command = NTCommand.PLACE;
        NTOrderStatus status = NTOrderStatus.INITIALIZED;
        NTtif tif = NTtif.GTC;

        resultOrder.setId(id);
        resultOrder.setAccount(account);
        resultOrder.setInstrument(instrument);
        resultOrder.setPrice(price);
        resultOrder.setSize(size);
        resultOrder.setAction(action);
        resultOrder.setCommand(command);
        resultOrder.setType(type);
        resultOrder.setStatus(status);
        resultOrder.setTif(tif);

        return resultOrder;
    }


    public Money getLastPrice(String instrument) {
        return null;
    }

    public Money getBestBid(String instrument) {
        return null;

    }

    public Money getBestAsk(String instrument) {
        return null;
    }

    public void sendCancelOrder(NTOrder order) {
    }

    public void sendCancelAllOrders() {
    }

    public void sendBuyLimitOrder(NTOrder order) {
    }

    public void sendBuyMarketOrder(NTOrder order) {
    }

    public void sendSellLimitOrder(NTOrder order) {
    }

    public void sendSellMarketOrder(NTOrder order) {
    }

    public int getFilledOfOrder(String orderId) {
        return 0;
    }

    public boolean getConnectionNT(int showMessage) {
        return true;
    }

    public boolean closeConnectionNT() {
        return true;
    }

    public Money getCashValue() {
        return null;
    }

    public Money getBuyingPower() {
        return null;
    }

    public int getPosition() {
        return 0;
    }

    public List<String> getAllOrdersId() {
        return null;
    }

    public Money getPnL() {
        return null;
    }

    public String getNewOrderId() {
        return null;
    }

//    Order State

    //      Initialized
//    Order information validated on local PC
//    Yellow
//      PendingSubmit
//    Order submitted to the connectivity provider
//    Orange
//      Accepted
//    Order confirmation received by broker
//    Light blue
//      Working
//    Order confirmation received by exchange
//    Green
//      PendingChange
//    Order modification submitted to the connectivity provider
//    Orange
//      PendingCancel
//    Order cancellation submitted to the connectivity provider/exchange
//    Red
//      Cancelled
//    Order cancellation confirmed cancelled by exchange
//    Red
//       Rejected
//    Order rejected locally, by connectivity provider or exchange
//    Red
//       PartFilled
//    Order partially filled
//    Red
//       Filled
//    Order completely filled
//    Green
    public String getOrderStatus(String id) {
        return null;
    }


}
