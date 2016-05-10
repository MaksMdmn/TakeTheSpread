package go.takethespread;

import go.takethespread.fsa.FinitStateAutomation;
import go.takethespread.managers.ConsoleManager;
import go.takethespread.managers.ExternalManager;
import go.takethespread.managers.exceptions.ConsoleException;
import go.takethespread.managers.exceptions.TradeException;
import go.takethespread.managers.socket.NTTcpExternalManagerImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TEST_FSA {
    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        FinitStateAutomation fsa = new FinitStateAutomation();

        ConsoleManager cm = ConsoleManager.getInstance();
        ExternalManager edm = NTTcpExternalManagerImpl.getInstance();

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
