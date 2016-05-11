package go.takethespread.managers;

import go.takethespread.managers.exceptions.TradeException;

import java.util.ArrayDeque;
import java.util.Deque;

public class TaskManager {

    private static TaskManager instance;

    private Deque<TradeTask> taskPool = new ArrayDeque<TradeTask>();

    private TaskManager() {

    }

    public static TaskManager getInstance() {
        if (instance == null) {
            instance = new TaskManager();
        }

        return instance;
    }

    public static TradeTask createTradeTask(InfoManager.ConsoleCommand command, String item, String values) {
        return new TradeTask(command, item, values);
    }

    public synchronized void pushTask(TradeTask task) throws TradeException {
        if (taskPool == null) {
            throw new TradeException("taskPool wasn't initialized: " + taskPool);
        }
        taskPool.push(task);
    }

    public synchronized void pollTask(TradeTask task) throws TradeException {
        if (taskPool == null) {
            throw new TradeException("taskPool wasn't initialized: " + taskPool);
        }
        taskPool.poll();
    }

    public synchronized void removeTask(TradeTask task) throws TradeException {
        if (taskPool == null) {
            throw new TradeException("taskPool wasn't initialized: " + taskPool);
        }
        taskPool.remove(task);
    }

    public synchronized TradeTask getCurrentTask() throws TradeException {
        if (taskPool == null) {
            throw new TradeException("taskPool wasn't initialized: " + taskPool);
        }
        return taskPool.peekFirst();
    }


    public static class TradeTask {
        private InfoManager.ConsoleCommand command;
        private String item;
        private String values;

        private TradeTask(InfoManager.ConsoleCommand command, String item, String values) {
            this.command = command;
            this.item = item;
            this.values = values;
        }

        public  InfoManager.ConsoleCommand getCommand() {
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
