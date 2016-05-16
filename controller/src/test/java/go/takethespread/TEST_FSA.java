package go.takethespread;

import go.takethespread.fsa.FinitStateAutomation;
import go.takethespread.managers.InfoManager;
import go.takethespread.managers.ExternalManager;
import go.takethespread.exceptions.ConsoleException;
import go.takethespread.exceptions.TradeException;
import go.takethespread.managers.socket.NTTcpExternalManagerImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TEST_FSA {
    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        FinitStateAutomation fsa = new FinitStateAutomation();
        ExternalManager edm = NTTcpExternalManagerImpl.getInstance();
        InfoManager cm = InfoManager.getInstance();

        edm.startingJob();
        fsa.start();

        String userMessage = "";
        while (!userMessage.equals("GJ")) {
            try {
                userMessage = br.readLine();
                System.out.println("answer: " + cm.parseConsoleMsg(userMessage));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ConsoleException e) {
                e.printStackTrace();
            } catch (TradeException e) {
                e.printStackTrace();
            }
        }

        System.out.println("main off");
    }
}
