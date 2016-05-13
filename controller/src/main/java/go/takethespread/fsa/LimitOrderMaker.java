package go.takethespread.fsa;

import go.takethespread.Order;
import go.takethespread.managers.ExternalManager;

import java.util.List;

public class LimitOrderMaker extends OrderMaker {
    private TradeBlotter blotter;
    private ExternalManager externalManager;
    private List<Order> activeOrders;

    public LimitOrderMaker(TradeBlotter blotter, ExternalManager externalManager) {
        this.blotter = blotter;
        this.externalManager = externalManager;
    }

    public void doNearBuyFarSell(){

    }


    public void doFarBuyNearSell(){

    }

}
