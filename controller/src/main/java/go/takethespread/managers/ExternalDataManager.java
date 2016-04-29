package go.takethespread.managers;

import go.takethespread.Money;
import go.takethespread.Order;

import java.util.List;

public interface ExternalDataManager {

    public Money getBBid(String instr);

    public Money getBAsk(String instr);

    public int getBBidVolume(String instr);

    public int getBAskVolume(String instr);

    public int getPosition(String instr);

    public Order getOrder(String orderId);

    public List<Order> getOrders();

    public String sendLimitBuy(String instr, Money price, int size);

    public String sendLimitSell(String instr, Money price, int size);

    public void sendMarketBuy(String instr, int size);

    public void sendMarketSell(String instr, int size);

}
