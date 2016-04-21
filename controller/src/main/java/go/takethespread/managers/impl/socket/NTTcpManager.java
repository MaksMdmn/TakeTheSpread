package go.takethespread.managers.impl.socket;

import java.util.HashMap;

public class NTTcpManager {
    private static NTTcpManager instance;
    private NTTcpDataBridge bridge;
    private HashMap<Integer, String> msgAnwMap;
    private int messageCounter = 0;

    private NTTcpManager() {
        msgAnwMap = new HashMap<>();
        bridge = NTTcpDataBridge.getInstance();
    }

    public static NTTcpManager getInstance() {
        if (instance == null) {
            instance = new NTTcpManager();
        }

        return instance;
    }

    public int sendNTMessage(String msg) {
        msgAnwMap.put(messageCounter, null);
        msg += ":-:" + messageCounter;
        bridge.addMessage(msg);
        return messageCounter++;
    }

    //if == null then havent answer yet
    public String receiveNTAnswer(int key) {
        collectAllAnswers();
        return msgAnwMap.get(key);
    }

    private void collectAllAnswers() {
        while (bridge.haveAnswers()) {
            String tempStr = bridge.acceptAnswer();
            String[] tempArr = tempStr.split(":-:");

            if (tempArr.length != 2) throw new IllegalArgumentException("Parsing error: answer array have length != 2");

            String answer = tempArr[0];
            int key = Integer.valueOf(tempArr[1]);

            msgAnwMap.put(key, answer);
        }
    }
}
