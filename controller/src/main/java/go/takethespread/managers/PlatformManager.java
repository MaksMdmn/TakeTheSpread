package go.takethespread.managers;

import go.takethespread.Money;

import java.util.List;

public interface PlatformManager {

    public Money getLastPrice(String instrument);

    public Money getBestBid(String instrument);

    public Money getBestAsk(String instrument);

    public void sendCancelOrder(String id);

    public void sendCancelAllOrders();

    public void sendBuyLimitOrder(String account, String instrument, Money price, int quantity);

    public void sendBuyMarketOrder(String account, String instrument,int quantity);

    public void sendSellLimitOrder(String account, String instrument, Money price, int quantity);

    public void sendSellMarketOrder(String account, String instrument,int quantity);

    public int getFilledOfOrder(String orderId);

    public boolean getConnectionNT(int showMessage);

    public boolean closeConnectionNT();

    public Money getCashValue();

    public Money getBuyingPower();

    public int getPosition();

    public List<String> getAllOrdersId();

    public Money getPnL();

    public String getOrderStatus(String id);
}
