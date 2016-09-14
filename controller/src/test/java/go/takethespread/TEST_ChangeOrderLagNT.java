package go.takethespread;

import go.takethespread.managers.ExternalManager;
import go.takethespread.managers.socket.NTTcpExternalManagerImpl;

public class TEST_ChangeOrderLagNT {
    public static void main(String[] args) throws InterruptedException {
        ExternalManager manager = NTTcpExternalManagerImpl.getInstance();
        manager.startingJob("localhost", 8085);
        Order o = manager.sendLimitBuy("CL 10-16", Money.dollars(45.10), 5);
        String id = o.getOrdId();
        System.out.println("PLACED: " + o);
        Thread.sleep(3000);
        o = manager.sendChangeOrder(id, Money.dollars(0), 2);
        System.out.println("CHANGED 1: " + o);
        Thread.sleep(3000);
        o = manager.sendChangeOrder(id, Money.dollars(45.15), 4);
        System.out.println("CHANGED 2: " + o);
        Thread.sleep(1000);
        System.out.println("bye bye");
        manager.finishingJob();
    }
}
