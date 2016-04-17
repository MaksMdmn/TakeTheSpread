package go.takethespread.managers.impl;

import go.takethespread.managers.exceptions.ConsoleException;
import go.takethespread.managers.exceptions.TradeException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConsoleManager {

    private static ConsoleManager instance;

    private TaskManager taskManager;
    private Properties actualProperties;

    private ConsoleManager() {
        propertiesInit();
        actualProperties = getActualProperties();
        taskManager = TaskManager.getInstance();
    }

    public static ConsoleManager getInstance() {
        if (instance == null) {
            instance = new ConsoleManager();

        }
        return instance;
    }

    public void readConsoleMessage(String msg) throws ConsoleException, TradeException {
        // token is ' ', token between values is ','

        String command = msg.split(" ")[0];
        String item = msg.split(" ")[1];
        String values = msg.split(" ")[2];

        executeConsoleCommand(command, item, values);

    }


    public Properties getActualProperties(){
        return actualProperties;
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
        if (actualProperties == null) {
            throw new ConsoleException("Settings-example file is empty, " + actualProperties);
        }

        return actualProperties.containsKey(item);

    }

    private boolean valuesVerification(String values) throws ConsoleException {
        if (actualProperties == null) {
            throw new ConsoleException("Settings-example file is empty, " + actualProperties);
        }

        return actualProperties.containsValue(values);
    }

    private void propertiesInit() {
        actualProperties = new Properties();
        String fileName = "possibleSettings.properties";
        try (InputStream input = getInstance().getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                throw new RuntimeException("Settings-example file was unable to find");
            }
            actualProperties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public enum ConsoleCommand {
        GO, //start
        GJ, //stop
        RN, //ReturN the Value
        PL, //PLace the Value

    }
}


