package go.takethespread;

import go.takethespread.managers.ExternalManager;
import go.takethespread.managers.ConsoleManager;
import go.takethespread.managers.socket.NTTcpExternalManagerImpl;

public class TestTCPOrders {
    static ExternalManager ntTcpExternalManager = NTTcpExternalManagerImpl.getInstance();

    public static void main(String[] args) {
        try {
            String instr1 = ConsoleManager.getInstance().getTradeSystemInfo().instrument_n;
            String instr2 = ConsoleManager.getInstance().getTradeSystemInfo().instrument_f;

            ntTcpExternalManager.startingJob();

            Order ord = ntTcpExternalManager.sendMarketBuy(instr1, 1);

            System.out.println(ntTcpExternalManager.getPosition(instr1));

            System.out.println(ord);
            System.out.println(ntTcpExternalManager.getOrderFilled(ord.getId()));

            System.out.println(ntTcpExternalManager.getPosition(instr1));

            ord = ntTcpExternalManager.sendCancelOrder(ord.getId());
            System.out.println(ord);
            System.out.println(ord.getFilled());

            System.out.println(ntTcpExternalManager.getPosition(instr1));
//            Order ord = ntTcpExternalManager.sendLimitBuy(instr1, Money.dollars(47.6), 2);
//            System.out.println(ord);
//            Thread.sleep(2000);
//            System.out.println("send change");
//            ord = ntTcpExternalManager.sendChangeOrder(ord.getId(), Money.dollars(47.7), 6);
//            System.out.println(ord);


            ntTcpExternalManager.finishingJob();

        } catch (Exception e) {

        }
    }
}
