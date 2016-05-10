package go.takethespread.managers.socket;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class NTTcpDataBridge {

    private static NTTcpDataBridge instance;
    private BlockingDeque<String> messages;
    private BlockingDeque<String> answers;
    private BlockingDeque<String> nearMarketData;
    private BlockingDeque<String> farMarketData;

    private NTTcpDataBridge() {
        messages = new LinkedBlockingDeque<>();
        answers = new LinkedBlockingDeque<>();
        nearMarketData = new LinkedBlockingDeque<>();
        farMarketData = new LinkedBlockingDeque<>();
    }

    protected static NTTcpDataBridge getInstance() {
        if (instance == null) {
            instance = new NTTcpDataBridge();
        }

        return instance;
    }

    protected void addAnswer(String data) {
        if (data.isEmpty()) {
            return;
        }

        if (data.contains("TEST")) {
            System.out.println(data);
            return;
        }

        if (data.startsWith("n")) {
            nearMarketData.add(data.split(NTTcpMessage.ntToken)[1]);
            return;
        }

        if (data.startsWith("f")) {
            farMarketData.add(data.split(NTTcpMessage.ntToken)[1]);
            return;
        }

        answers.push(data);

    }

    protected void addMessage(String message) {
        messages.push(message);
    }

    protected String acceptMessage() {
        return messages.pollFirst();
    }

    protected String acceptAnswer() {
        return answers.pollFirst();
    }

    protected String acceptNearMarketData(){
        return nearMarketData.peekLast();
    }

    protected String acceptFarMarketData(){
        return nearMarketData.peekLast();
    }

    protected boolean haveMessage() {
        return !messages.isEmpty();
    }

    protected boolean haveAnswers() {
        return !answers.isEmpty();
    }

    protected boolean haveMarketData() {return !nearMarketData.isEmpty() && !farMarketData.isEmpty();}

}
