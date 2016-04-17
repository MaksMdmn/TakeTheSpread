package go.takethespread.managers;

import go.takethespread.Money;
import go.takethespread.Order;

import java.util.List;

public interface ExternalDataManager {

    public Money getBBid(String instr);

    public Money getBAsk(String instr);

    public int getBBidVolume(String instr);

    public int getBAskVolume(String instr);

    public Money getLast(String instr);

    public int getPosition(String instr);

    public Order getOrder(String id);

    public List<Order> getOrders();

    public void sendLimitBuy(String instr, Money price, int size);

    public void sendLimitSell(String instr, Money price, int size);

    public void sendMarketBuy(String instr, Money price, int size);

    public void sendMarketSell(String instr, Money price, int size);

}
