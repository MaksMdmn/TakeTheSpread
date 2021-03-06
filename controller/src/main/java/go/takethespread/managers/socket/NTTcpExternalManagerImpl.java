package go.takethespread.managers.socket;

import go.takethespread.Money;
import go.takethespread.Order;
import go.takethespread.Term;
import go.takethespread.fsa.TradeSystemInfo;
import go.takethespread.managers.ConsoleManager;
import go.takethespread.managers.ExternalManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class NTTcpExternalManagerImpl implements ExternalManager {

    private static ExternalManager instance;
    private static int attemptMaxNumbers;
    private static int attempts;
    private int delay;
    private NTTcpManager ntTcpManager;
    private ActualMarketData nearMarketData;
    private ActualMarketData farMarketData;

    private NTTcpExternalManagerImpl() {
        ntTcpManager = NTTcpManager.getInstance();
        nearMarketData = new ActualMarketData(Term.NEAR);
        farMarketData = new ActualMarketData(Term.FAR);
        attemptMaxNumbers = 500; //10 sec
        delay = 20;
    }

    public static synchronized ExternalManager getInstance() {
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
        String tempPos = waitingForAnswer(id);
        return Integer.valueOf(tempPos);
    }

    @Override
    public Order getOrder(String orderId) {
        long id = ntTcpManager.sendOrderByIdMessage(orderId);
        String tempOrder = waitingForAnswer(id);

        return parseTheOrder(tempOrder);
    }

    @Override
    public List<Order> getOrders() {
        long id = ntTcpManager.sendOrdersMessage();
        String tempOrders = waitingForAnswer(id);
        if (tempOrders.equals(" ")) {
            return null;
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
        String tempCashValue = waitingForAnswer(id);
        return Money.dollars(Double.valueOf(tempCashValue.replace(",", ".")));
    }

    @Override
    public Money getBuyingPower() {
        long id = ntTcpManager.sendBuyingPowerMessage();
        String tempBuyingPower = waitingForAnswer(id);
        return Money.dollars(Double.valueOf(tempBuyingPower.replace(",", ".")));
    }

    @Override
    public Money getPnL() {
        long id = ntTcpManager.sendRealizedPnLMessage();
        String tempPnL = waitingForAnswer(id);
        return Money.dollars(Double.valueOf(tempPnL.replace(",", ".")));
    }

    @Override
    public int getOrderFilled(String ordId) {
        long id = ntTcpManager.sendFilledMessage(ordId);
        String filled = waitingForAnswer(id);
        return Integer.valueOf(filled);
    }

    @Override
    public Order sendLimitBuy(String instr, Money price, int size) {
        long id = ntTcpManager.sendBuyLimitMessage(identifyTerm(instr), size, price.getAmount());
        return parseTheOrder(waitingForAnswer(id));
    }

    @Override
    public Order sendLimitSell(String instr, Money price, int size) {
        long id = ntTcpManager.sendSellLimitMessage(identifyTerm(instr), size, price.getAmount());
        return parseTheOrder(waitingForAnswer(id));
    }

    @Override
    public Order sendMarketBuy(String instr, int size) {
        int tempPos = getPosition(instr);
        long id = ntTcpManager.sendBuyMarketMessage(identifyTerm(instr), size);
        Order answer = parseTheOrder(waitingForAnswer(id));
        while (tempPos == getPosition(instr)) {
            trackStatus();
            /*NOP*/
        }
        resetStatus();
        return answer;
    }

    @Override
    public Order sendMarketSell(String instr, int size) {
        int tempPos = getPosition(instr);
        long id = ntTcpManager.sendSellMarketMessage(identifyTerm(instr), size);
        Order answer = parseTheOrder(waitingForAnswer(id));
        while (tempPos == getPosition(instr)) {
            trackStatus();
            /*NOP*/
        }
        resetStatus();
        return answer;
    }

    @Override
    public Order[] sendPairMarketBuySell(String buyInstr, int buySize, String sellInstr, int sellSize) {
        Order[] answer = new Order[2];

        //oriented on second instrument, because second order is last in queue of execution in NT.
        int tempPos = getPosition(sellInstr);

        long id1 = ntTcpManager.sendBuyMarketMessage(identifyTerm(buyInstr), buySize);
        long id2 = ntTcpManager.sendSellMarketMessage(identifyTerm(sellInstr), sellSize);
        answer[0] = parseTheOrder(waitingForAnswer(id1));
        answer[1] = parseTheOrder(waitingForAnswer(id2));

        while (tempPos == getPosition(sellInstr)) {
            trackStatus();
            /*NOP*/
        }
        resetStatus();
        return answer;
    }

    @Override
    public Order sendCancelOrder(String ordId) {
        long id = ntTcpManager.sendCancelByIdMessage(ordId);
        return parseTheOrder(waitingForAnswer(id));
    }

    @Override
    public Order sendChangeOrder(String ordId, Money price, int size) {
        long id = ntTcpManager.sendChangeByIdMessage(ordId, size, price.getAmount());
        return parseTheOrder(waitingForAnswer(id));
    }

    @Override
    public void sendCancelOrders() {
        ntTcpManager.sendCancelAllMessage();
    }

    @Override
    public void startingJob(String host, int port) {
        ntTcpManager.startUpServer(host, port);
        if (ntTcpManager.isServerWork()) {
            System.out.println("server started");
        } else {
            System.out.println("have some trouble, man");
        }
    }

    public void finishingJob() {
        ntTcpManager.sendCancelAllMessage();
        ntTcpManager.sendOffMessage();
        ntTcpManager.finishingTodayJob();
        System.out.println("job finished");
    }

    public void refreshData() {
        nearMarketData.updateActualMarketData();
        farMarketData.updateActualMarketData();
    }

    public boolean isConnOkay() {
        return ntTcpManager.isServerWork();
    }

    private Term identifyTerm(String instr) {
        TradeSystemInfo info = TradeSystemInfo.getInstance();
        if (instr.equals(info.instrument_n))
            return Term.NEAR;
        if (instr.equals(info.instrument_f))
            return Term.FAR;
        throw new IllegalArgumentException("incorrect instrument name: " + instr);
    }


    private Order parseTheOrder(String tempOrder) {
        Order order = new Order();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        try {
            String id = searchTheParameter("Order", tempOrder);
            String instr = searchTheParameter("Instrument", tempOrder);
            Date date = dateFormat.parse(searchTheParameter("Time", tempOrder));
            Order.Deal deal = Order.Deal.valueOf(searchTheParameter("Action", tempOrder));
            Order.Type type = Order.Type.valueOf(searchTheParameter("Type", tempOrder));
            Order.State state = Order.State.valueOf(searchTheParameter("State", tempOrder));
            int size = Integer.valueOf(searchTheParameter("Quantity", tempOrder));
            Money price = Money.dollars(Double.valueOf(searchTheParameter("price", tempOrder)));
            int filled = Integer.valueOf(searchTheParameter("Filled", tempOrder));
            Money priceFilled = Money.dollars(Double.valueOf(searchTheParameter("Fill price", tempOrder)));

            order.setOrdId(id);
            order.setInstrument(instr);
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
        } catch (NumberFormatException e) {
            System.out.println("error on this order " + tempOrder);
            e.printStackTrace();
        }

        return order;
    }

    private String searchTheParameter(String param, String str) {
        int startParamIndex = str.indexOf(param + "=");
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
        if (result.contains("/")) {
            result = result.split("/")[0];
        }
        return result;
    }

    private String waitingForAnswer(long id) {
        String answer = null;
        while (answer == null) {
            answer = ntTcpManager.receiveNTAnswer(id);
            trackStatus();
        }
        resetStatus();
        return answer;
    }

    private void trackStatus() {
        try {
            attempts++;
            Thread.sleep(delay);
            if (attempts > attemptMaxNumbers) {
                throw new RuntimeException("delay is too big for correcting work : " + delay * attemptMaxNumbers + " ms.");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void resetStatus() {
        attempts = 0;
    }


    private class ActualMarketData {
        double bid;
        int bidVol;
        double ask;
        int askVol;
        Term term;

        private ActualMarketData(Term term) {
            this.term = term;
        }

        private void updateActualMarketData() {
            String data = null;
            while (data == null) {
                data = ntTcpManager.getFreshMarketData(term); //tut
                trackStatus();
            }
            resetStatus();

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
