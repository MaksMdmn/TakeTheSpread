package go.takethespread.managers;

import go.takethespread.*;
import go.takethespread.managers.exceptions.TradeException;

import java.util.ArrayList;
import java.util.List;

public class NTPlatformManager {

    private static NTPlatformManager instance;

    private InfoManager infoManager;
    private NTdll dll;

    private NTPlatformManager() {
        infoManager = InfoManager.getInstance();
    }

    public static NTPlatformManager getInstance() {
        if (instance == null) {
            instance = new NTPlatformManager();
        }

        return instance;
    }

    public Money getLastPrice(String instrument) {
        return Money.dollars(dll.MarketData(instrument, 0));
    }

    public Money getBestBid(String instrument) {
        return Money.dollars(dll.MarketData(instrument, 1));

    }

    public Money getBestAsk(String instrument) {
        return Money.dollars(dll.MarketData(instrument, 2));
    }

    public void sendCancelOrder(String orderId) {
        updateTemplateAndSend(OrderCommand.CANCEL, "", OrderAction.NO_ACTION, 0, OrderType.NO_TYPE, Money.dollars(0d), orderId);
    }

    public void sendCancelAllOrders() {
        updateTemplateAndSend(OrderCommand.CANCELALLORDERS, "", OrderAction.NO_ACTION, 0, OrderType.NO_TYPE, Money.dollars(0d), "");
    }

    public void sendBuyLimitOrder(String instrument, int size, Money price) {
        updateTemplateAndSend(OrderCommand.PLACE, instrument, OrderAction.BUY, size, OrderType.LIMIT, price, getNewOrderId());
    }

    public void sendBuyMarketOrder(String instrument, int size) {
        //ask + 3% = market price for buy
        Money price = getBestAsk(instrument).multiply(1.03);
        updateTemplateAndSend(OrderCommand.PLACE, instrument, OrderAction.BUY, size, OrderType.MARKET, price, getNewOrderId());
    }

    public void sendSellLimitOrder(String instrument, int size, Money price) {
        updateTemplateAndSend(OrderCommand.PLACE, instrument, OrderAction.SELL, size, OrderType.LIMIT, price, getNewOrderId());
    }

    public void sendSellMarketOrder(String instrument, int size) {
        //bid - 3% best price for sell
        Money price = getBestBid(instrument).multiply(0.97);
        updateTemplateAndSend(OrderCommand.PLACE, instrument, OrderAction.SELL, size, OrderType.MARKET, price, getNewOrderId());
    }

    public int getFilledOfOrder(String orderId) {
        return dll.Filled(orderId);
    }

    public boolean initConnectionNT() {
        return dll.Connected(0) == 0;
    }

    public boolean closeConnectionNT() {
        return dll.TearDown() == 0;
    }

    public Money getCashValue() {
        return Money.dollars(dll.CashValue(infoManager.getActualProperties().getProperty("account")));
    }

    public Money getBuyingPower() {
        return Money.dollars(dll.BuyingPower(infoManager.getActualProperties().getProperty("account")));
    }

    public int getPosition(String instrument) {
        return dll.MarketPosition(instrument, infoManager.getActualProperties().getProperty("account"));
    }

    public List<String> getAllOrdersId() {
        List<String> resultList = new ArrayList<>();
        String tempArr[] = dll.Orders(infoManager.getActualProperties().getProperty("account")).split("|");
        for (String s : tempArr) {
            resultList.add(s);
        }

        return resultList;
    }

    public Money getPnL() {
        return Money.dollars(dll.RealizedPnL(infoManager.getActualProperties().getProperty("account")));
    }

    public String getNewOrderId() {
        return dll.NewOrderId();
    }

    public String getOrderStatus(String orderId) {
        return dll.OrderStatus(orderId);
    }

    public void startWorkingWithNT() throws TradeException {
        if (initConnectionNT()) {
            throw new TradeException("Connection wasn't established");
        }
        String n_instrument = infoManager.getActualProperties().getProperty("n_instrument");
        String f_instrument = infoManager.getActualProperties().getProperty("f_instrument");

        int n_subscribe = dll.SubscribeMarketData(n_instrument);
        int f_subscribe = dll.SubscribeMarketData(f_instrument);
        if (n_subscribe != 0 || f_subscribe != 0) {
            throw new TradeException("Cannot Subscribe market data, answer: near is " + n_subscribe + ", far is " + f_subscribe);
        }
    }

    public void endWorkingWithNT() throws TradeException {
        String n_instrument = infoManager.getActualProperties().getProperty("n_instrument");
        String f_instrument = infoManager.getActualProperties().getProperty("f_instrument");

        int n_subscribe = dll.UnsubscribeMarketData(n_instrument);
        int f_subscribe = dll.UnsubscribeMarketData(f_instrument);
        if (n_subscribe != 0 || f_subscribe != 0) {
            throw new TradeException("Cannot Unsubscribe market data, answer: near is " + n_subscribe + ", far is " + f_subscribe);
        }

        if (closeConnectionNT()) {
            throw new TradeException("TearDown error");
        }
    }

    private void updateTemplateAndSend(OrderCommand command, String instrument, OrderAction action,
                                       int quantity, OrderType type, Money price, String orderId) {
        String tmplCommand = command.name();
        String tmplAccount = infoManager.getActualProperties().getProperty("account");
        String tmplAction = action == OrderAction.NO_ACTION ? "" : action.name();
        String tmplType = type == OrderType.NO_TYPE ? "" : type.name();
        double tmplPrice = price.getAmount();
        double tmplStopprice = 0d;
        String tmplTIF = OrderTIF.GTC.name();
        String tmplOco = "";
        String tmplStrategy = "";
        String tmplStrategyId = "";

        dll.Command(tmplCommand, tmplAccount, instrument, tmplAction, quantity, tmplType, tmplPrice, tmplStopprice,
                tmplTIF, tmplOco, orderId, tmplStrategy, tmplStrategyId);
    }

}
