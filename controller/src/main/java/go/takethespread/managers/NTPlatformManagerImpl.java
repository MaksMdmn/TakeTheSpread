package go.takethespread.managers;

import go.takethespread.Money;

import java.util.List;

public class NTPlatformManagerImpl implements PlatformManager {

    private static NTPlatformManagerImpl instance;

    private NTPlatformManagerImpl() {
    }

    public static NTPlatformManagerImpl getInstance() {
        if (instance == null) {
            instance = new NTPlatformManagerImpl();
        }

        return instance;
    }


    public Money getLastPrice(String instrument){
        return null;
    }

    public Money getBestBid(String instrument){
        return null;

    }

    public Money getBestAsk(String instrument){
        return null;
    }

    public void sendCancelOrder(String id) {
    }

    public void sendCancelAllOrders() {
    }

    public void sendBuyLimitOrder(String account, String instrument, Money price, int quantity) {
    }

    public void sendBuyMarketOrder(String account, String instrument,int quantity) {
    }

    public void sendSellLimitOrder(String account, String instrument, Money price, int quantity) {
    }

    public void sendSellMarketOrder(String account, String instrument,int quantity) {
    }

    public int getFilledOfOrder(String orderId){
        return 0;
    }

    public boolean getConnectionNT(int showMessage){
        return true;
    }

    public boolean closeConnectionNT(){
        return true;
    }

    public Money getCashValue(){
        return null;
    }

    public Money getBuyingPower(){
        return null;
    }

    public int getPosition(){
        return 0;
    }

    public List<String> getAllOrdersId(){
        return null;
    }

    public Money getPnL(){
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
    public String getOrderStatus(String id){
        return null;
    }


}
