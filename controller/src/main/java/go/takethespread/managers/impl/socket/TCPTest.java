package go.takethespread.managers.impl.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TCPTest {
    private static boolean flag = true;
    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Server starting...");
            NTTcpServer server = new NTTcpServer();
            NTTcpDataBridge bridge = server.getDataBridge();
            server.initServerWork();
            bridge.addMessage("TEST 1.23:-:1");

            Thread test = new Thread(() -> {
                while(flag){
                    bridge.printAllDataToConsole();
                }
            });
            test.setDaemon(true);
            test.start();

            String s = "";
            while(true){
                s = reader.readLine();
                if (s.equals("GJ")){
                    flag= false;
                    bridge.addMessage(s);
                    Thread.sleep(2000);
                    break;
                }
                bridge.addMessage(s);
            }
            server.shutDown();
            System.out.println("server off");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
