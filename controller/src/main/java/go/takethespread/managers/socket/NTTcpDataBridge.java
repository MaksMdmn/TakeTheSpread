package go.takethespread.managers.socket;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class NTTcpDataBridge {

    private static NTTcpDataBridge instance;
    private BlockingDeque<String> messages;
    private BlockingDeque<String> answers;

    private NTTcpDataBridge() {
        messages = new LinkedBlockingDeque<>();
        answers = new LinkedBlockingDeque<>();
    }

    public static NTTcpDataBridge getInstance() {
        if (instance == null) {
            instance = new NTTcpDataBridge();
        }

        return instance;
    }

    public void addAnswer(String data) {
        if (data.contains("TEST")) {
            System.out.println(data);
            return;
        }

        if (data.isEmpty()) {
            return;
        }

        answers.push(data);

    }

    public void addMessage(String message) {
        messages.push(message);
    }

    public String acceptMessage() {
        return messages.pollFirst();
    }

    public String acceptAnswer() {
        return answers.pollFirst();
    }

    public boolean haveMessage() {
        return !messages.isEmpty();
    }

    public boolean haveAnswers() {
        return !answers.isEmpty();
    }

}
