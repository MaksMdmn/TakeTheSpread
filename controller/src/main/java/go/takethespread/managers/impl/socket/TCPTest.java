package go.takethespread.managers.impl.socket;


public class TCPTest {
    public static void main(String[] args) {

        try {
            NTTcpManager manager = NTTcpManager.getInstance();
            long id;
            String ordId;
            id = manager.sendBuyingPowerMessage();

            Thread.sleep(1000);
            System.out.println(manager.receiveNTAnswer(id));

            id = manager.sendBuyLimitMessage(NTTcpManager.Term.NEAR, 1, 46.0d);

            Thread.sleep(1000);
            ordId = manager.receiveNTAnswer(id);
            System.out.println(ordId);

            manager.sendCancelByIdMessage(ordId);

            Thread.sleep(1000);

            id = manager.sendBuyLimitMessage(NTTcpManager.Term.NEAR, 1, 46.7d);

            Thread.sleep(1000);
            ordId = manager.receiveNTAnswer(id);

            id = manager.sendFilledMessage(ordId);

            Thread.sleep(1000);
            System.out.println(manager.receiveNTAnswer(id));

            id = manager.sendPositionMessage(NTTcpManager.Term.NEAR);

            Thread.sleep(1000);
            System.out.println(manager.receiveNTAnswer(id));

            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println("CANCELING");
            manager.sendSellMarketMessage(NTTcpManager.Term.NEAR, 1);
            manager.sendSellLimitMessage(NTTcpManager.Term.NEAR, 1, 47);
            manager.sendSellLimitMessage(NTTcpManager.Term.NEAR, 1, 46.9);
            manager.sendBuyLimitMessage(NTTcpManager.Term.NEAR, 1, 46.2);

            Thread.sleep(1000);

            manager.sendCancelAllMessage();

            id = manager.sendRealizedPnLMessage();
            Thread.sleep(200);
            System.out.println(manager.receiveNTAnswer(id));

            id = manager.sendOrderByIdMessage(ordId);
            Thread.sleep(1000);
            System.out.println(manager.receiveNTAnswer(id));


            id = manager.sendOrdersMessage();
            Thread.sleep(200);
            String s = manager.receiveNTAnswer(id);
            System.out.println(s);
            System.out.println();
            for(String s1 :s.split("ord:")){
                System.out.println(s1);
            }

            id = manager.sendMarketDataMessage(NTTcpManager.Term.NEAR);
            Thread.sleep(200);
            System.out.println(manager.receiveNTAnswer(id));

            id = manager.sendBuyLimitMessage(NTTcpManager.Term.NEAR, 1, 46.1d);
            Thread.sleep(200);
            ordId = manager.receiveNTAnswer(id);
            System.out.println(ordId);

            id = manager.sendSellLimitMessage(NTTcpManager.Term.NEAR, 1, 47d);
            Thread.sleep(200);
            ordId = manager.receiveNTAnswer(id);
            System.out.println(ordId);

            id = manager.sendSellLimitMessage(NTTcpManager.Term.NEAR, 1, 46.9d);
            Thread.sleep(200);
            ordId = manager.receiveNTAnswer(id);
            System.out.println(ordId);

            manager.sendCancelAllMessage();

            Thread.sleep(200);

            manager.sendOffMessage();
            Thread.sleep(1000);
            manager.finishingTodaysJob();



        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
