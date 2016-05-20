package go.takethespread;

import go.takethespread.fsa.TradeBlotter;
import go.takethespread.managers.ExternalManager;
import go.takethespread.managers.InfoManager;
import go.takethespread.managers.socket.NTTcpExternalManagerImpl;

public class TestLimitMaker {
    public static void main(String[] args) {
        int q = 100;
        int curQ = 0;
        ExternalManager externalManager = NTTcpExternalManagerImpl.getInstance();
        externalManager.startingJob();

        InfoManager infoManager = InfoManager.getInstance();
        String instrumentN = infoManager.getActualProperties().getProperty("instrument_n");
        String instrumentF = infoManager.getActualProperties().getProperty("instrument_f");
        TradeBlotter blotter = new TradeBlotter(instrumentN, instrumentF, externalManager);
//        OrderMaker orderMaker = new OrderMaker(blotter, externalManager, infoManager);

        while (curQ < q) {
            blotter.updateMainInfo();
//            orderMaker.doNearBuyFarSell();

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("sec: " + curQ++);
        }

        externalManager.finishingJob();
        System.out.println("done.");
    }
}
