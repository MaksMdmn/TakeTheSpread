package go.takethespread.managers;

import go.takethespread.Money;
import go.takethespread.NT.NTOrder;

import java.util.List;

public class NTManager {

    private static NTManager instance;

    private NTManager() {
    }

    public static NTManager getInstance() {
        if (instance == null) {
            instance = new NTManager();
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

    public boolean getConnectionNT(int showMessage){
        return true;
    }

    public boolean closeConnectionNT(){
        return true;
    }

    public Money getCashValue(){
        return null;
    }

    public int getPosition(){
        return 0;
    }

    public List<NTOrder>getAllOrders(){
        return null;
    }

    public Money getPnL(){
        return null;
    }

    public String getOrderStatus(String id){
        return null;
    }


}
