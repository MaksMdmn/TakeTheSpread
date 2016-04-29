package go.takethespread.managers.impl;

import go.takethespread.Money;
import go.takethespread.Order;
import go.takethespread.managers.ExternalDataManager;
import go.takethespread.managers.impl.socket.NTTcpManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExternalNTDataManagerImpl implements ExternalDataManager {

    private static ExternalDataManager instance;
    private ConsoleManager consoleManager;
    private NTTcpManager ntTcpManager;
    private int delay;

    private ExternalNTDataManagerImpl() {
        consoleManager = ConsoleManager.getInstance();
        ntTcpManager = NTTcpManager.getInstance();
    }

    public static ExternalDataManager getInstance() {
        if (instance == null) {
            instance = new ExternalNTDataManagerImpl();

        }
        return instance;
    }

    @Override
    public Money getBBid(String instr) {
        NTTcpManager.Term t = identifyTerm(instr);
        return Money.dollars(new ActualMarketData(t).getBid());
    }

    @Override
    public Money getBAsk(String instr) {
        NTTcpManager.Term t = identifyTerm(instr);
        return Money.dollars(new ActualMarketData(t).getAsk());
    }

    @Override
    public int getBBidVolume(String instr) {
        NTTcpManager.Term t = identifyTerm(instr);
        return new ActualMarketData(t).getBidVol();
    }

    @Override
    public int getBAskVolume(String instr) {
        NTTcpManager.Term t = identifyTerm(instr);
        return new ActualMarketData(t).getAskVol();
    }

    @Override
    public int getPosition(String instr) {
        long id = ntTcpManager.sendPositionMessage(identifyTerm(instr));
        Integer pos = null;
        while (pos == null) {
            pos = Integer.valueOf(ntTcpManager.receiveNTAnswer(id));
            delayWaiting();
        }
        return pos;
    }

    @Override
    public Order getOrder(String orderId) {
        long id = ntTcpManager.sendOrderByIdMessage(orderId);
        String tempOrder = null;
        while (tempOrder == null) {
            tempOrder = ntTcpManager.receiveNTAnswer(id);
            delayWaiting();
        }
//        Order='9c102d6da4254c9eb87e6a5c747b9c64/Sim101'
//        Name='Buy'
//        State=Filled
//        Instrument='CL 06-16'
//        Action=Buy Limit
//        price=46,7
//        Stop price=0
//        Quantity=1
//        Strategy='TcpClientStrategy'
//        Type=Limit
//        Tif=Gtc Oco=''
//        Filled=1
//        Fill price=46,37
//        Token='9c102d6da4254c9eb87e6a5c747b9c64'
//        Gtd='01.12.2099 0:00:00'
//        Time='29.04.2016 11:10:40'

        return parseTheOrder(tempOrder);
    }

    @Override
    public List<Order> getOrders() {
        long id = ntTcpManager.sendOrdersMessage();
        String tempOrders = null;
        while (tempOrders == null) {
            tempOrders = ntTcpManager.receiveNTAnswer(id);
            delayWaiting();
        }
        List<Order>orders = new ArrayList<>();
        for(String tempOrder :tempOrders.split("ord:")){
            orders.add(parseTheOrder(tempOrder));
        }

        return orders;
    }

    @Override
    public String sendLimitBuy(String instr, Money price, int size) {
        long id = ntTcpManager.sendBuyLimitMessage(identifyTerm(instr), size, price.getAmount());
        String result = null;
        while (result == null) {
            result = ntTcpManager.receiveNTAnswer(id);
            delayWaiting();
        }
        return result;
    }

    @Override
    public String sendLimitSell(String instr, Money price, int size) {
        long id = ntTcpManager.sendSellLimitMessage(identifyTerm(instr), size, price.getAmount());
        String result = null;
        while (result == null) {
            result = ntTcpManager.receiveNTAnswer(id);
            delayWaiting();
        }
        return result;
    }

    @Override
    public void sendMarketBuy(String instr, int size) {
        ntTcpManager.sendBuyMarketMessage(identifyTerm(instr), size);
    }

    @Override
    public void sendMarketSell(String instr, int size) {
        ntTcpManager.sendSellMarketMessage(identifyTerm(instr), size);
    }

    private NTTcpManager.Term identifyTerm(String instr) {
        if (instr.equals(consoleManager.getActualProperties().getProperty("n_instrument")))
            return NTTcpManager.Term.NEAR;
        if (instr.equals(consoleManager.getActualProperties().getProperty("f_instrument")))
            return NTTcpManager.Term.FAR;
        throw new IllegalArgumentException("incorrect instrument name: " + instr);
    }

    private class ActualMarketData {
        double bid;
        int bidVol;
        double ask;
        int askVol;

        private ActualMarketData(NTTcpManager.Term term) {
            long id = ntTcpManager.sendMarketDataMessage(term);
            String data = null;
            while (data == null) {
                data = ntTcpManager.receiveNTAnswer(id);
                delayWaiting();
            }
            String[] marketData = data.split(" ");
            this.bid = Double.valueOf(marketData[1]);
            this.bidVol = Integer.valueOf(marketData[2]);
            this.ask = Double.valueOf(marketData[4]);
            this.askVol = Integer.valueOf(marketData[5]);
        }

        public double getBid() {
            return bid;
        }

        public int getBidVol() {
            return bidVol;
        }

        public double getAsk() {
            return ask;
        }

        public int getAskVol() {
            return askVol;
        }
    }

    private void delayWaiting() {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Order parseTheOrder(String tempOrder){
        Order order = new Order();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:MM:SS");
        try {
            Date date = dateFormat.parse(searchTheParameter("Time", tempOrder));
            Order.Deal deal = Order.Deal.valueOf(searchTheParameter("Action", tempOrder).toUpperCase());
            Order.Type type = Order.Type.valueOf(searchTheParameter("Type", tempOrder).toUpperCase());
            int size = Integer.valueOf(searchTheParameter("Quantity", tempOrder));
            Money price = Money.dollars(Double.valueOf(searchTheParameter("price", tempOrder)));
            int filled = Integer.valueOf(searchTheParameter("Filled", tempOrder));
            Money priceFilled = Money.dollars(Double.valueOf(searchTheParameter("Fill price", tempOrder)));

            order.setDate(date);
            order.setDeal(deal);
            order.setType(type);
            order.setSize(size);
            order.setPrice(price);
            order.setFilled(filled);
            order.setPriceFilled(priceFilled);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return order;
    }

    private String searchTheParameter(String param, String str) {
        int startParamIndex = str.indexOf(param);
        int startEqualIndex = str.indexOf("=", startParamIndex);
        int startValueIndex = startEqualIndex + 1;
        int endValueIndex = str.indexOf(" ", startValueIndex);
        String result = str.substring(startValueIndex, endValueIndex);
        if (result.contains("'")) {
            result = result.replace("'", "");
        }
        if (result.contains("Buy")) {
            result = "Buy";
        }
        if (result.contains("Sell")) {
            result = "Sell";
        }
        return result;
    }
}
