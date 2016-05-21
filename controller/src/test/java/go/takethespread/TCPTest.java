package go.takethespread;


import go.takethespread.managers.ConsoleManager;
import go.takethespread.managers.ExternalManager;
import go.takethespread.managers.socket.NTTcpExternalManagerImpl;

import java.util.List;

public class TCPTest {
    static ExternalManager ntTcpExternalManager = NTTcpExternalManagerImpl.getInstance();
    public static void main(String[] args) {
        try {
            String instr1 = ConsoleManager.getInstance().getActualProperties().getProperty("instrument_n");
            String instr2 = ConsoleManager.getInstance().getActualProperties().getProperty("instrument_f");

            System.out.println("start!!!");
            ntTcpExternalManager.startingJob();

            System.out.println("update");
            ntTcpExternalManager.refreshData();

            System.out.println(
                    "nearData: " +
                            ntTcpExternalManager.getBBid(instr1) + " vol " +
                            ntTcpExternalManager.getBBidVolume(instr1) + " " +
                            ntTcpExternalManager.getBAsk(instr1) + " vol " +
                            ntTcpExternalManager.getBAskVolume(instr1) + " " +
                            "farData: " +
                            ntTcpExternalManager.getBBid(instr2) + " vol " +
                            ntTcpExternalManager.getBBidVolume(instr2) + " " +
                            ntTcpExternalManager.getBAsk(instr2) + " vol " +
                            ntTcpExternalManager.getBAskVolume(instr2)
            );

            System.out.println("orders: ");
            List<Order> orderList = ntTcpExternalManager.getOrders();
            System.out.println(orderList == null ? "orders are null!!!" : orderList);

            System.out.println("account's data:");
            System.out.println(ntTcpExternalManager.getBuyingPower());
            System.out.println(ntTcpExternalManager.getCashValue());
            System.out.println(ntTcpExternalManager.getPnL());

            System.out.println("positions near and far: ");
            System.out.println(ntTcpExternalManager.getPosition(instr1));
            System.out.println(ntTcpExternalManager.getPosition(instr2));

            System.out.println("sending limit orders near and far:");
            ntTcpExternalManager.sendLimitBuy(instr1, ntTcpExternalManager.getBBid(instr1).multiply(0.99d), 1);
            ntTcpExternalManager.sendLimitSell(instr2, ntTcpExternalManager.getBAsk(instr2).multiply(1.01d), 1);
            System.out.println("and try to check this orders: ");

            System.out.println("try to cancel them: ");
            ntTcpExternalManager.sendCancelOrders();
            System.out.println("check orders now:");
            System.out.println(ntTcpExternalManager.getOrders());
            System.out.println("and after 1 sec:");
            Thread.sleep(1000);
            System.out.println(ntTcpExternalManager.getOrders());

            System.out.println("sending market orders near and far: ");
            ntTcpExternalManager.sendMarketBuy(instr1, 1);
            ntTcpExternalManager.sendMarketSell(instr2, 1);
            ntTcpExternalManager.sendMarketSell(instr2, 1);

            System.out.println("and check the positions near and far: ");
            System.out.println(ntTcpExternalManager.getPosition(instr1));
            System.out.println(ntTcpExternalManager.getPosition(instr2));


            System.out.println("check order filling: ");
            ntTcpExternalManager.refreshData();
            System.out.println("poss: "
                    + ntTcpExternalManager.getPosition(instr1)
                    + " "
                    + ntTcpExternalManager.getPosition(instr2));
            ntTcpExternalManager.sendLimitBuy(instr1, ntTcpExternalManager.getBBid(instr1),15);


            System.out.println("if all seems good, bb");
            ntTcpExternalManager.finishingJob();

            System.out.println("already off");

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Oopps.. here's a big troble D:");
            ntTcpExternalManager.finishingJob();
            System.out.println("but serv's done");
        }
    }
}
