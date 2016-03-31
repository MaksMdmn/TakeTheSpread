package go.takethespread.managers;

import go.takethespread.managers.exceptions.ConsoleException;
import go.takethespread.managers.exceptions.TradeException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConsoleManager {

    private static ConsoleManager instance;

    private TradeManager tradeManager;
    private Properties possibleSettings;

    private ConsoleManager() {
        possibleSettings = propertiesInit();
        tradeManager = TradeManager.getInstance();
    }

    public static ConsoleManager getInstance() {
        if (instance == null) {
            instance = new ConsoleManager();

        }
        return instance;
    }

    private Properties propertiesInit() {
        possibleSettings = new Properties();
        String fileName = "possibleSettings.properties";
        try (InputStream input = getInstance().getClass().getClassLoader().getResourceAsStream(fileName);) {
            if (input == null) {
                throw new RuntimeException("Settings-example file was unable to find");
            }
            possibleSettings.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return possibleSettings;

    }

    public void readConsoleMessage(String msg) throws ConsoleException, TradeException {
        // token is ' ', token between values is ','

        String command = msg.split(" ")[0];
        String item = msg.split(" ")[1];
        String values = msg.split(" ")[2];

        executeConsoleCommand(command, item, values);

    }

    private void executeConsoleCommand(String command, String item, String values) throws ConsoleException, TradeException {
        commandVerification(command);
        itemVerification(item);
        valuesVerification(values);

        ConsoleCommand consoleCommand = ConsoleCommand.valueOf(command);

        TradeManager.TradeTask ts = TradeManager.createTradeTask(consoleCommand, item, values);

        tradeManager.pushTask(ts);
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
        if (possibleSettings == null) {
            throw new ConsoleException("Settings-example file is empty, " + possibleSettings);
        }

        return possibleSettings.containsKey(item);

    }

    private boolean valuesVerification(String values) throws ConsoleException {
        if (possibleSettings == null) {
            throw new ConsoleException("Settings-example file is empty, " + possibleSettings);
        }

        return possibleSettings.containsValue(values);
    }


}


