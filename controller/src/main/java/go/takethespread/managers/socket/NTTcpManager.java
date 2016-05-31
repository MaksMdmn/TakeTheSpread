package go.takethespread.managers.socket;

import go.takethespread.fsa.Term;

import java.util.Arrays;
import java.util.HashMap;

public class NTTcpManager {
    private static NTTcpManager instance;
    private NTTcpServer server;
    private NTTcpServer.NTTcpDataBridge bridge;
    private HashMap<Long, String> answersMap;

    private NTTcpManager() {
        answersMap = new HashMap<>();
    }

    protected static NTTcpManager getInstance() {
        if (instance == null) {
            instance = new NTTcpManager();
        }

        return instance;
    }

    protected String getFreshMarketData(Term term) {
        if (term == Term.NEAR) {
            return bridge.acceptNearMarketData();
        } else {
            return bridge.acceptFarMarketData();
        }
    }

    protected void startUpServer() {
        server = new NTTcpServer();
        server.initServerWork();
        bridge = server.getDataBridge();
    }

    //always last

    protected void finishingTodayJob() {
        server.shutDown();
        //correct ending
        //repotring
    }

    protected void sendOffMessage() {
        bridge.addMessage(new NTTcpMessage(NTTcpMessage.NTTcpCommand.GJ, "").prepareToSending());
    }

    protected long sendOrdersMessage() {
        return sendNTMessage(new NTTcpMessage(NTTcpMessage.NTTcpCommand.ORDS, ""));
    }

    protected long sendOrderByIdMessage(String ordId) {
        return sendNTMessage(new NTTcpMessage(NTTcpMessage.NTTcpCommand.BYID, ordId));
    }

    protected long sendPositionMessage(Term term) {
        return sendNTMessage(new NTTcpMessage(NTTcpMessage.NTTcpCommand.POS, getInstrumentNumber(term)));
    }

    protected long sendBuyingPowerMessage() {
        return sendNTMessage(new NTTcpMessage(NTTcpMessage.NTTcpCommand.BPOW, ""));
    }

    protected long sendCashValueMessage() {
        return sendNTMessage(new NTTcpMessage(NTTcpMessage.NTTcpCommand.CSHV, ""));
    }

    protected long sendRealizedPnLMessage() {
        return sendNTMessage(new NTTcpMessage(NTTcpMessage.NTTcpCommand.RPNL, ""));
    }

    protected long sendBuyMarketMessage(Term term, int size) {
        return sendNTMessage(new NTTcpMessage(NTTcpMessage.NTTcpCommand.BMRT, getOrderParametres(term, size, 0d)));
    }

    protected long sendSellMarketMessage(Term term, int size) {
        return sendNTMessage(new NTTcpMessage(NTTcpMessage.NTTcpCommand.SMRT, getOrderParametres(term, size, 0d)));
    }

    protected long sendBuyLimitMessage(Term term, int size, double price) {
        return sendNTMessage(new NTTcpMessage(NTTcpMessage.NTTcpCommand.BLMT, getOrderParametres(term, size, price)));
    }

    protected long sendSellLimitMessage(Term term, int size, double price) {
        return sendNTMessage(new NTTcpMessage(NTTcpMessage.NTTcpCommand.SLMT, getOrderParametres(term, size, price)));
    }

    protected long sendFilledMessage(String ordId) {
        return sendNTMessage(new NTTcpMessage(NTTcpMessage.NTTcpCommand.FLLD, ordId));
    }

    protected void sendCancelAllMessage() {
        sendNTMessage(new NTTcpMessage(NTTcpMessage.NTTcpCommand.CNAL, ""));
    }

    protected long sendCancelByIdMessage(String ordId) {
        return sendNTMessage(new NTTcpMessage(NTTcpMessage.NTTcpCommand.CNID, ordId));
    }

    protected long sendChangeByIdMessage(String ordId, int size, double price) {
        return sendNTMessage(new NTTcpMessage(NTTcpMessage.NTTcpCommand.CHOR, ordId + " " + size + " " + price));
    }

    protected long sendNTMessage(NTTcpMessage msg) {
        long msgId = msg.getId();
        answersMap.put(msgId, null);
        bridge.addMessage(msg.prepareToSending());
        return msgId;
    }

    protected String receiveNTAnswer(long key) {
        collectAllAnswers();
        return answersMap.get(key);
    }

    private String getOrderParametres(Term term, int size, double price) {
        return price == 0d
                ? getInstrumentNumber(term) + " " + String.valueOf(size)
                : getInstrumentNumber(term) + " " + String.valueOf(size) + " " + String.valueOf(price);
    }

    private String getInstrumentNumber(Term term) {
        return String.valueOf(term == Term.NEAR ? 0 : 1);
    }

    protected static long getMessageId() {
        return System.currentTimeMillis();
    }

    private synchronized void collectAllAnswers() {
        while (bridge.haveAnswers()) {
            String tempStr = bridge.acceptAnswer();
            String[] tempArr = tempStr.split(NTTcpMessage.ntToken);
            long key;
            String value;

            try {
                if (tempArr.length != 2)
                    throw new IllegalArgumentException("Parsing error: answer array have length != 2, actual: " + tempArr.length + " arr: " + Arrays.toString(tempArr));

                key = Long.valueOf(tempArr[0]);
                value = tempArr[1];

                answersMap.put(key, value);
            } catch (Exception e) {
                System.out.println("err: " + Arrays.toString(tempArr));
                e.printStackTrace();
            }
        }
    }


}
