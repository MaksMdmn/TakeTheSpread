package go.takethespread.managers.impl;

import go.takethespread.managers.exceptions.TradeException;

import java.util.ArrayDeque;
import java.util.Deque;

public class TradeTaskManager {

    private static TradeTaskManager instance;

    private Deque<TradeTask> taskPool = new ArrayDeque<TradeTask>();

    private TradeTaskManager() {

    }

    public static TradeTaskManager getInstance() {
        if (instance == null) {
            instance = new TradeTaskManager();
        }

        return instance;
    }

    public static TradeTask createTradeTask(ConsoleManager.ConsoleCommand command, String item, String values) {
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


    public static class TradeTask {
        private ConsoleManager.ConsoleCommand command;
        private String item;
        private String values;

        private TradeTask(ConsoleManager.ConsoleCommand command, String item, String values) {
            this.command = command;
            this.item = item;
            this.values = values;
        }

        public ConsoleManager.ConsoleCommand getCommand() {
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
