package go.takethespread.managers.socket;

import go.takethespread.Money;
import go.takethespread.Order;
import go.takethespread.managers.ConsoleManager;
import go.takethespread.managers.ExternalManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class NTTcpExternalManagerImpl implements ExternalManager {

    private static ExternalManager instance;
    private ConsoleManager consoleManager;
    private NTTcpManager ntTcpManager;
    private int delay;
    private ActualMarketData nearMarketData;
    private ActualMarketData farMarketData;

    private NTTcpExternalManagerImpl() {
        consoleManager = ConsoleManager.getInstance();
        ntTcpManager = NTTcpManager.getInstance();
        nearMarketData = new ActualMarketData(NTTcpManager.Term.NEAR);
        farMarketData = new ActualMarketData(NTTcpManager.Term.FAR);
        delay = 200;
    }

    public static ExternalManager getInstance() {
        if (instance == null) {
            instance = new NTTcpExternalManagerImpl();

        }
        return instance;
    }

    @Override
    public Money getBBid(String instr) {
        switch (identifyTerm(instr)) {
            case NEAR:
                return Money.dollars(nearMarketData.getBid());
            case FAR:
                return Money.dollars(farMarketData.getBid());
            default:
                throw new IllegalArgumentException("incorrect instrument name: " + instr);
        }
    }

    @Override
    public Money getBAsk(String instr) {
        switch (identifyTerm(instr)) {
            case NEAR:
                return Money.dollars(nearMarketData.getAsk());
            case FAR:
                return Money.dollars(farMarketData.getAsk());
            default:
                throw new IllegalArgumentException("incorrect instrument name: " + instr);
        }
    }

    @Override
    public int getBBidVolume(String instr) {
        switch (identifyTerm(instr)) {
            case NEAR:
                return nearMarketData.getBidVol();
            case FAR:
                return farMarketData.getBidVol();
            default:
                throw new IllegalArgumentException("incorrect instrument name: " + instr);
        }
    }

    @Override
    public int getBAskVolume(String instr) {
        switch (identifyTerm(instr)) {
            case NEAR:
                return nearMarketData.getAskVol();
            case FAR:
                return farMarketData.getAskVol();
            default:
                throw new IllegalArgumentException("incorrect instrument name: " + instr);
        }
    }

    @Override
    public int getPosition(String instr) {
        long id = ntTcpManager.sendPositionMessage(identifyTerm(instr));
        String tempPos = null;
        while (tempPos == null) {
            tempPos = ntTcpManager.receiveNTAnswer(id);
            delayWaiting();
        }
        return Integer.valueOf(tempPos);
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
        List<Order> orders = new ArrayList<>();
        String[] tempOrdersArr = tempOrders.split("ord:");
        for (String tempOrder : tempOrdersArr) {
            if (!tempOrder.isEmpty()) {
                orders.add(parseTheOrder(tempOrder));
            }
        }

        return orders;
    }

    @Override
    public Money getCashValue() {
        long id = ntTcpManager.sendCashValueMessage();
        String tempCashValue = null;
        while (tempCashValue == null) {
            tempCashValue = ntTcpManager.receiveNTAnswer(id);
            delayWaiting();
        }

        return Money.dollars(Double.valueOf(tempCashValue));
    }

    @Override
    public Money getBuyingPower() {
        long id = ntTcpManager.sendBuyingPowerMessage();
        String tempBuyingPower = null;
        while (tempBuyingPower == null) {
            tempBuyingPower = ntTcpManager.receiveNTAnswer(id);
            delayWaiting();
        }

        return Money.dollars(Double.valueOf(tempBuyingPower));
    }

    @Override
    public Money getPnL() {
        long id = ntTcpManager.sendRealizedPnLMessage();
        String tempPnL = null;
        while (tempPnL == null) {
            tempPnL = ntTcpManager.receiveNTAnswer(id);
            delayWaiting();
        }

        return Money.dollars(Double.valueOf(tempPnL));
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

    @Override
    public void sendCancelOrder(String ordId) {
        ntTcpManager.sendCancelByIdMessage(ordId);
    }

    @Override
    public void sendCancelOrders() {
        ntTcpManager.sendCancelAllMessage();
    }

    public void startingJob() {
        System.out.println("starting...");
        ntTcpManager.okay_letsGo();
        System.out.println("started.");
    }

    public void finishingJob() {
        ntTcpManager.sendOffMessage();
        ntTcpManager.finishingTodaysJob();
        System.out.println("job finished");
    }

    private NTTcpManager.Term identifyTerm(String instr) {
        if (instr.equals(consoleManager.getActualProperties().getProperty("instrument_n")))
            return NTTcpManager.Term.NEAR;
        if (instr.equals(consoleManager.getActualProperties().getProperty("instrument_f")))
            return NTTcpManager.Term.FAR;
        throw new IllegalArgumentException("incorrect instrument name: " + instr);
    }

    public void refreshData() {
        nearMarketData.updateActualMarketData();
        farMarketData.updateActualMarketData();
    }

    private Order parseTheOrder(String tempOrder) {
        Order order = new Order();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        try {
            Date date = dateFormat.parse(searchTheParameter("Time", tempOrder));
            Order.Deal deal = Order.Deal.valueOf(searchTheParameter("Action", tempOrder));
            Order.Type type = Order.Type.valueOf(searchTheParameter("Type", tempOrder));
            Order.State state = Order.State.valueOf(searchTheParameter("State", tempOrder));
            int size = Integer.valueOf(searchTheParameter("Quantity", tempOrder));
            Money price = Money.dollars(Double.valueOf(searchTheParameter("price", tempOrder)));
            int filled = Integer.valueOf(searchTheParameter("Filled", tempOrder));
            Money priceFilled = Money.dollars(Double.valueOf(searchTheParameter("Fill price", tempOrder)));

            order.setDate(date);
            order.setDeal(deal);
            order.setType(type);
            order.setState(state);
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

        int startValueIndex = 0;
        int endValueIndex = 0;

        if (str.charAt(startEqualIndex + 1) == '\'') {
            startValueIndex = startEqualIndex + 2; //='.....'  so start + 2 and to char "'"
            endValueIndex = str.indexOf("'", startValueIndex);
        } else {
            startValueIndex = startEqualIndex + 1;
            endValueIndex = str.indexOf(" ", startValueIndex);
        }

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
        if (result.contains(",")) {
            result = result.replace(",", ".");
        }
        return result;
    }

    private void delayWaiting() {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class ActualMarketData {
        double bid;
        int bidVol;
        double ask;
        int askVol;
        NTTcpManager.Term term;

        private ActualMarketData(NTTcpManager.Term term) {
            this.term = term;
        }

        private void updateActualMarketData() {
            long id = ntTcpManager.sendMarketDataMessage(term);
            String data = null;
            while (data == null) {
                data = ntTcpManager.receiveNTAnswer(id);
                delayWaiting();
            }

            String[] marketData = data.replace(',', '.').split(" ");
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

}
