package go.takethespread.managers.impl.socket;

import java.util.Date;
import java.util.HashMap;

public class NTTcpSender {
    private static NTTcpSender instance;
    private NTTcpDataBridge bridge;
    private HashMap<String, String> msgAnsw;

    private NTTcpSender() {
        msgAnsw = new HashMap<>();
        bridge = NTTcpDataBridge.getInstance();
    }

    public static NTTcpSender getInstance() {
        if (instance == null) {
            instance = new NTTcpSender();
        }

        return instance;
    }

    public String sendNTMessage(String msg) {
        String msgId = msg + "|" + new Date().toString();
        msgAnsw.put(msgId, null);
        bridge.addMessage(msgId);
        return msgId;
    }


}
