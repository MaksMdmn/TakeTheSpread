package go.takethespread.managers.impl.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TCPTest {
    public static void main(String[] args) {

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            NTTcpManager manager = NTTcpManager.getInstance();

            String m = "";
            long id = 0;
            while (true) {
                m = reader.readLine();
                if (id != 0) {
                    System.out.println(manager.receiveNTAnswer(id));
                }
                if (m.equals("GJ")) {
                    id = manager.sendNTMessage(new NTTcpMessage(NTTcpMessage.NTTcpCommand.GJ, ""));
                    Thread.sleep(1000);
                    manager.finishingTodaysJob();
                    System.out.println("finished...");
                    break;
                } else {
                    id = manager.sendNTMessage(new NTTcpMessage(NTTcpMessage.NTTcpCommand.valueOf(m), ""));
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
