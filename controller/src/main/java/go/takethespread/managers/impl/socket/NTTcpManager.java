package go.takethespread.managers.impl.socket;

import java.util.Arrays;
import java.util.HashMap;

public class NTTcpManager {
    private static NTTcpManager instance;
    private NTTcpServer server;
    private NTTcpDataBridge bridge;
    private HashMap<Long, String> answersMap;

    private NTTcpManager() {
        answersMap = new HashMap<>();
        bridge = NTTcpDataBridge.getInstance();
        server = new NTTcpServer();
        server.initServerWork();
    }

    public static NTTcpManager getInstance() {
        if (instance == null) {
            instance = new NTTcpManager();
        }

        return instance;
    }

    public long sendNTMessage(NTTcpMessage msg) {
        long msgId = msg.getId();
        answersMap.put(msgId, null);
        bridge.addMessage(msg.prepareToSending());
        return msgId;
    }

    public void sendOffMessage() {
        bridge.addMessage(new NTTcpMessage(NTTcpMessage.NTTcpCommand.GJ, "").prepareToSending());
    }

    public long sendOrdersMessage() {
        return sendNTMessage(new NTTcpMessage(NTTcpMessage.NTTcpCommand.ORDS, ""));
    }

    public long sendOrderByIdMessage(String ordId) {
        return sendNTMessage(new NTTcpMessage(NTTcpMessage.NTTcpCommand.BYID, ordId));
    }

    public long sendPositionMessage(Term term) {
        return sendNTMessage(new NTTcpMessage(NTTcpMessage.NTTcpCommand.POS, getInstrumentNumber(term)));
    }

    public long sendBuyingPowerMessage() {
        return sendNTMessage(new NTTcpMessage(NTTcpMessage.NTTcpCommand.BPOW, ""));
    }

    public long sendCashValueMessage() {
        return sendNTMessage(new NTTcpMessage(NTTcpMessage.NTTcpCommand.CSHV, ""));
    }

    public long sendRealizedPnLMessage() {
        return sendNTMessage(new NTTcpMessage(NTTcpMessage.NTTcpCommand.RPNL, ""));
    }

    public long sendBuyMarketMessage(Term term, int size) {
        return sendNTMessage(new NTTcpMessage(NTTcpMessage.NTTcpCommand.BMRT, getOrderParametres(term,size,0d)));
    }

    public long sendSellMarketMessage(Term term, int size) {
        return sendNTMessage(new NTTcpMessage(NTTcpMessage.NTTcpCommand.SMRT, getOrderParametres(term,size,0d)));
    }

    public long sendBuyLimitMessage(Term term, int size, double price) {
        return sendNTMessage(new NTTcpMessage(NTTcpMessage.NTTcpCommand.BLMT, getOrderParametres(term,size,price)));
    }

    public long sendSellLimitMessage(Term term, int size, double price) {
        return sendNTMessage(new NTTcpMessage(NTTcpMessage.NTTcpCommand.SLMT, getOrderParametres(term,size,price)));
    }

    public long sendMarketDataMessage(Term term) {
        return sendNTMessage(new NTTcpMessage(NTTcpMessage.NTTcpCommand.BDAK, getInstrumentNumber(term)));
    }

    public long sendFilledMessage(String ordId){
        //?? mb delete it
        return 0L;
    }

    public void sendCancelAllMessage(){
        bridge.addMessage(new NTTcpMessage(NTTcpMessage.NTTcpCommand.CNAL, "").prepareToSending());
    }

    public void sendCancelByIdMessage(String ordId){
        bridge.addMessage(new NTTcpMessage(NTTcpMessage.NTTcpCommand.CNID, ordId).prepareToSending());
    }

    private String getOrderParametres(Term term, int size, double price) {
        return price == 0d
                ? getInstrumentNumber(term) + " " + String.valueOf(size)
                : getInstrumentNumber(term) + " " + String.valueOf(size) + " " + String.valueOf(price);
    }

    private String getInstrumentNumber(Term term) {
        return String.valueOf(term == Term.NEAR ? 0 : 1);
    }

    //    BDAK //bid ask data
    //if == null then havent answer yet
    public String receiveNTAnswer(long key) {
        collectAllAnswers();
        return answersMap.get(key);
    }

    //always last

    public void finishingTodaysJob() {
        server.shutDown();
        //correct ending
        //repotring
    }

    protected static long getMessageId() {
        return System.currentTimeMillis();
    }

    private synchronized void collectAllAnswers() {
        while (bridge.haveAnswers()) {
            String tempStr = bridge.acceptAnswer();
            String[] tempArr = tempStr.split(NTTcpMessage.ntToken);

            if (tempArr.length != 2)
                throw new IllegalArgumentException("Parsing error: answer array have length != 2, actual: " + tempArr.length + " arr: " + Arrays.toString(tempArr));

            long key = Long.valueOf(tempArr[0]);
            String answer = tempArr[1];
            answersMap.put(key, answer);
        }
    }

    public enum Term {
        NEAR,
        FAR
    }

}
