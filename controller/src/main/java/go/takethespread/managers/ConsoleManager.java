package go.takethespread.managers;

import go.takethespread.exceptions.ConsoleException;
import go.takethespread.exceptions.TradeException;
import go.takethespread.fsa.TradeSystemInfo;

public class ConsoleManager {

    private static ConsoleManager instance;
    private TradeSystemInfo tradeSystemInfo;
    private TaskManager taskManager;

    private ConsoleManager() {
        tradeSystemInfo = new TradeSystemInfo();
        taskManager = TaskManager.getInstance();
    }

    public static ConsoleManager getInstance() {
        if (instance == null) {
            instance = new ConsoleManager();

        }
        return instance;
    }

    public String parseConsoleMsg(String msg) throws ConsoleException, TradeException {
        // token is ' ', token between values is ','

        String command = "";
        String item = "";
        String values = "";

        switch (msg.split(" ").length) {
            case 1:
                command = msg.split(" ")[0];
                break;
            case 2:
                command = msg.split(" ")[0];
                item = msg.split(" ")[1];
                break;
            case 3:
                command = msg.split(" ")[0];
                item = msg.split(" ")[1];
                values = msg.split(" ")[2];
                break;
            default:
                throw new IllegalArgumentException("array length lower that 1 or higher than 3: " + msg);
        }


        executeConsoleCommand(command, item, values);

        String answer = "ANSWEROK";
        return answer;
    }

    public TradeSystemInfo getTradeSystemInfo(){
        return tradeSystemInfo;
    }
    private void executeConsoleCommand(String command, String item, String values) throws ConsoleException, TradeException {
        commandVerification(command);
        itemVerification(item);
        valuesVerification(values);

        ConsoleCommand consoleCommand = ConsoleCommand.valueOf(command);

        TaskManager.TradeTask ts = TaskManager.createTradeTask(consoleCommand, item, values);

        taskManager.pushTask(ts);
    }

    private boolean commandVerification(String command) throws ConsoleException {
        ConsoleCommand consoleCommand;
        try {
            consoleCommand = ConsoleCommand.valueOf(command);
        } catch (IllegalArgumentException e) {
            throw new ConsoleException("GETTING WRONG COMMAND: " + command, e);
        }
        return true;
    }

    private boolean itemVerification(String item) throws ConsoleException {
        if (tradeSystemInfo.isPropNull()) throw new ConsoleException("Settings-example file is empty" );
        return tradeSystemInfo.isPropExists(item);

    }

    private boolean valuesVerification(String values) throws ConsoleException {
        if (tradeSystemInfo.isPropNull()) throw new ConsoleException("Settings-example file is empty" );
        return tradeSystemInfo.isPropExists(values);
    }

    public enum ConsoleCommand {
        GO, //start
        GJ, //stop
        RN, //ReturN the Value
        PL, //PLace the Value

    }
}

