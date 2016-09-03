package go.takethespread.managers;

import go.takethespread.Money;
import go.takethespread.Order;

import java.util.List;

public interface StatusListener {

    public void runStatusChanged(ConsoleManager.ConsoleCommand command);

    public void ordersInfoUpdated(List<Order> orders);

    public void transactionTookPlace(Money[] near_far_prices);

}
