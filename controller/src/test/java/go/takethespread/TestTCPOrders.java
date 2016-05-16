package go.takethespread;

import go.takethespread.managers.ExternalManager;
import go.takethespread.managers.InfoManager;
import go.takethespread.managers.socket.NTTcpExternalManagerImpl;

public class TestTCPOrders {
    static ExternalManager ntTcpExternalManager = NTTcpExternalManagerImpl.getInstance();

    public static void main(String[] args) {
        try {
            String instr1 = InfoManager.getInstance().getActualProperties().getProperty("instrument_n");
            String instr2 = InfoManager.getInstance().getActualProperties().getProperty("instrument_f");

            ntTcpExternalManager.startingJob();

//            Order ord = ntTcpExternalManager.sendMarketBuy(instr1, 1);
//            System.out.println(ord);
//            System.out.println(ntTcpExternalManager.getOrderFilled(ord.getId()));

            Order ord = ntTcpExternalManager.sendLimitBuy(instr1,Money.dollars(47.5), 2);
            System.out.println(ord);
            ord = ntTcpExternalManager.sendCancelOrder(ord.getId());
            System.out.println(ord);

            ntTcpExternalManager.finishingJob();

        }catch (Exception e){

        }
    }
}
