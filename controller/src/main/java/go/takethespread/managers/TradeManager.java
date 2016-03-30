package go.takethespread.managers;

import go.takethespread.managers.exceptions.TradeException;

import java.util.ArrayDeque;
import java.util.Deque;

public class TradeManager {

    private static TradeManager instance;

    private Deque<TradeTask> taskPool = new ArrayDeque<TradeTask>();

    private TradeManager() {

    }

    public static TradeManager getInstance() {
        if (instance == null) {
            instance = new TradeManager();
        }

        return instance;
    }

    public static TradeTask createTradeTask(ConsoleCommand command, String item, String values) {
        return new TradeTask(command, item, values);
    }

    public void pushTask(TradeTask task) throws TradeException {
        if (taskPool == null) {
            throw new TradeException("taskPool wasn't initialized: " + taskPool);
        }
        taskPool.push(task);
    }

    public void pollTask(TradeTask task) throws TradeException {
        if (taskPool == null) {
            throw new TradeException("taskPool wasn't initialized: " + taskPool);
        }
        taskPool.poll();
    }

    public void removeTask(TradeTask task) throws TradeException {
        if (taskPool == null) {
            throw new TradeException("taskPool wasn't initialized: " + taskPool);
        }
        taskPool.remove(task);
    }

    public TradeTask getCurrentTask() throws TradeException {
        if (taskPool == null) {
            throw new TradeException("taskPool wasn't initialized: " + taskPool);
        }
        return taskPool.peek();
    }


    protected static class TradeTask {
        private ConsoleCommand command;
        private String item;
        private String values;

        private TradeTask(ConsoleCommand command, String item, String values) {
            this.command = command;
            this.item = item;
            this.values = values;
        }

        public ConsoleCommand getCommand() {
            return command;
        }

        public String getItem() {
            return item;
        }

        public String getValues() {
            return values;
        }
    }
}
