package go.takethespread.managers.impl;

import go.takethespread.Money;
import go.takethespread.Order;
import go.takethespread.managers.ExternalManager;

import java.util.List;

public class ExternalManagerImpl implements ExternalManager {

    @Override
    public Money getBBid(String instr) {
        return null;
    }

    @Override
    public Money getBAsk(String instr) {
        return null;
    }

    @Override
    public int getBBidVolume(String instr) {
        return 0;
    }

    @Override
    public int getBAskVolume(String instr) {
        return 0;
    }

    @Override
    public Money getLast(String instr) {
        return null;
    }

    @Override
    public int getPosition(String instr) {
        return 0;
    }

    @Override
    public Order getOrder(String id) {
        return null;
    }

    @Override
    public List<Order> getOrders() {
        return null;
    }

    @Override
    public void sendLimitBuy(String instr, Money price, int size) {

    }

    @Override
    public void sendLimitSell(String instr, Money price, int size) {

    }

    @Override
    public void sendMarketBuy(String instr, Money price, int size) {

    }

    @Override
    public void sendMarketSell(String instr, Money price, int size) {

    }
}
