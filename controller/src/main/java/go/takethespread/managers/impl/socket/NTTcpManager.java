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

    public enum InstrumentTerm {
        NEAR,
        FAR
    }

    public enum DataType {
        BID,
        ASK
    }

}
