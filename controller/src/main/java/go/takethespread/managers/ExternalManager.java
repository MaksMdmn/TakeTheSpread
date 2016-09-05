package go.takethespread.managers;

import go.takethespread.Money;
import go.takethespread.Order;

import java.util.List;

public interface ExternalManager {

    public Money getBBid(String instr);

    public Money getBAsk(String instr);

    public int getBBidVolume(String instr);

    public int getBAskVolume(String instr);

    public int getPosition(String instr);

    public Order getOrder(String orderId);

    public List<Order> getOrders();

    public Money getCashValue();

    public Money getBuyingPower();

    public Money getPnL();

    public int getOrderFilled(String ordId);

    public Order sendLimitBuy(String instr, Money price, int size);

    public Order sendLimitSell(String instr, Money price, int size);

    public Order sendMarketBuy(String instr, int size);

    public Order sendMarketSell(String instr, int size);

    public Order[] sendPairMarketBuySell(String buyInstr, int buySize, String sellInstr, int sellSize);

    public Order sendCancelOrder(String ordId);

    public Order sendChangeOrder(String ordId, Money price, int size);

    public void sendCancelOrders();

    public void refreshData();

    public void startingJob(String host, int port);

    public void finishingJob();

    public boolean isConnOkay();

}
