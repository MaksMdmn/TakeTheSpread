package go.takethespread.managers;

import go.takethespread.managers.exceptions.ConsoleException;

public class ConsoleManager {

    private static ConsoleManager instance;

    private ConsoleManager() {
    }

    public static ConsoleManager getInstance() {
        if (instance == null) {
            instance = new ConsoleManager();
        }

        return instance;
    }

    public void readConsoleMessage(String msg) throws ConsoleException {
        // token is ' ', token between item\values is ','

        String command = msg.split(" ")[0];
        String item = msg.split(" ")[1];
        String values = msg.split(" ")[2];

        ConsoleCommand consoleCommand;
        try {
            consoleCommand = ConsoleCommand.valueOf(msg);
        } catch (IllegalArgumentException e) {
            throw new ConsoleException("GETTING WRONG COMMAND: " + command, e);
        }
        //try for item
        //try for values
        executeConsoleCommand(consoleCommand);

    }

    private void executeConsoleCommand(ConsoleCommand command) {

        // every command need to read args: item + values
        switch (command) {
            case GO:
                break;
            case GJ:
                break;
            case RN:
                break;
            case PL:
                break;
            default:
                break;
        }
    }
}


