package go.takethespread.managers;

public interface StatusListener {

    public void runStatusChanged(ConsoleManager.ConsoleCommand command);

    public void ordersInfoUpdated();

}
