package go.takethespread.managers.impl.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TCPTest {
    private static boolean flag = true;
    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            NTTcpServer server = new NTTcpServer();
            NTTcpDataBridge bridge = server.getDataBridge();
            server.initServ();
            Thread.sleep(8000);

            System.out.println("Server started");
            Thread.sleep(2000);
            bridge.addMessage("TEST 1");
            bridge.printAllDataToConsole();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(flag){
                        bridge.printAllDataToConsole();
                    }
                }
            }).start();

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
