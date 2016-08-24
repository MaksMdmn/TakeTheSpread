package go.takethespread;

import go.takethespread.fsa.FiniteStateAutomation;
import go.takethespread.fsa.TradeSystemInfo;
import go.takethespread.managers.ConsoleManager;
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
        TradeSystemInfo info = TradeSystemInfo.getInstance();
        FiniteStateAutomation fsa = new FiniteStateAutomation();
        ExternalManager edm = NTTcpExternalManagerImpl.getInstance();
        ConsoleManager cm = ConsoleManager.getInstance();

        edm.startingJob(info.host, info.port);
        fsa.start();

        String userMessage = "";
        while (!userMessage.equals("GJ")) {
            try {
                userMessage = br.readLine();
                if (userMessage.equals("PH")) {
                    fsa.testPhonyGetterBlotter().getSpreadCalculator().testAddPhonyData();
                    System.out.println("PHONY DONE");
                    userMessage = "";
                } else {
                    System.out.println("answer: " + cm.parseConsoleMsg(userMessage));
                }
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
