package go.takethespread.managers.impl.socket;

public class TCPTest {
    public static void main(String[] args) {

        try {
            NTTcpManager manager = NTTcpManager.getInstance();
            System.out.println("started");


            long id;

            id = manager.sendBuyingPowerMessage();
            Thread.sleep(1000);
            System.out.println(manager.receiveNTAnswer(id));
            Thread.sleep(5000);

            id = manager.sendBuyLimitMessage(NTTcpManager.Term.NEAR, 1, 45.6d);
            Thread.sleep(5000);
            String ordId = manager.receiveNTAnswer(id);
            System.out.println(ordId);

            Thread.sleep(1000);
            manager.sendCancelAllMessage();

            manager.sendOffMessage();
            Thread.sleep(1000);
            manager.finishingTodaysJob();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        private static boolean flag = true;

//        try {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//            System.out.println("Server starting...");
//            NTTcpServer server = new NTTcpServer();
//            NTTcpDataBridge bridge = server.getDataBridge();
//            server.initServerWork();
//            System.out.println("testing connection...");
//            bridge.addMessage("TEST 1.23:-:1");
//
//            String s = "";
//            while(true){
//                s = reader.readLine();
//                if (s.equals("GJ")){
//                    flag= false;
//                    bridge.addMessage(s);
//                    Thread.sleep(2000);
//                    break;
//                }
//                bridge.addMessage(s);
//            }
//            server.shutDown();
//            System.out.println("server off");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

}
