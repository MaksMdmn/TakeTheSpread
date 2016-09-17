package go.takethespread;

import go.takethespread.managers.ExternalManager;
import go.takethespread.managers.socket.NTTcpExternalManagerImpl;

public class TEST_get_empty_orders_from_NT {
    public static void main(String[] args) {
        ExternalManager manager = NTTcpExternalManagerImpl.getInstance();
        try {
            manager.startingJob("localhost", 8085);
            System.out.println("connected.");
            Thread.sleep(2000);
            System.out.println(manager.getOrders());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            manager.finishingJob();
        }
    }
}
