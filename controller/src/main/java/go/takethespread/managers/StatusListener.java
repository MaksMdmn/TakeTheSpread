package go.takethespread.managers;

import go.takethespread.Order;

import java.util.List;

public interface StatusListener {

    public void runStatusChanged(ConsoleManager.ConsoleCommand command);

    public void ordersInfoUpdated(List<Order> orders);

}
