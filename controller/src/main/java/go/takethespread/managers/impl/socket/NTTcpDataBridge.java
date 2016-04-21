package go.takethespread.managers.impl.socket;

import java.util.*;

public class NTTcpDataBridge {

    private static NTTcpDataBridge instance;
    private TreeMap<Date, String> marketData;
    private Deque<String> messages;
    private Deque<String> answers;

    private NTTcpDataBridge() {
        marketData = new TreeMap<>(Comparator.reverseOrder());
        messages = new ArrayDeque<>();
    }

    public static NTTcpDataBridge getInstance() {
        if (instance == null) {
            instance = new NTTcpDataBridge();
        }

        return instance;
    }

    public void addData(String data) {
        //data handle
        if(data.contains(":-:")){
            answers.push(data);
        }else{
            Date key = null;
            String value = null;
            marketData.put(key, value);
        }
    }

    public void addMessage(String message) {
        messages.push(message);
    }

    public String acceptMessage() {
        return messages.pollFirst();
    }

    public void addAnswer(String answer) {
        answers.push(answer);
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







    public void printAllDataToConsole() {
        if (!marketData.isEmpty()) {
            System.out.println(marketData.toString());
            marketData.clear();
        }
    }

}
