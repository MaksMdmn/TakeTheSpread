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

    public String sendLimitBuy(String instr, Money price, int size);

    public String sendLimitSell(String instr, Money price, int size);

    public void sendMarketBuy(String instr, int size);

    public void sendMarketSell(String instr, int size);

    public void sendCancelOrder(String ordId);

    public void sendCancelOrders();

    public void refreshData();

    public void startingJob();

    public void finishingJob();

}
