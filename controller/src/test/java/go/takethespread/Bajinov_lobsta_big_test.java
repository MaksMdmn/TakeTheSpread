package go.takethespread;

import go.takethespread.exceptions.ConsoleException;
import go.takethespread.exceptions.TradeException;
import go.takethespread.fsa.FiniteStateAutomation;
import go.takethespread.managers.ConsoleManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Bajinov_lobsta_big_test {
    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        new FiniteStateAutomation().start();
        ConsoleManager cm = ConsoleManager.getInstance();
        String userMsg = "";

        while (true) {
            try {
                userMsg = br.readLine();
                System.out.println(cm.parseConsoleMsg(userMsg));

            } catch (IOException e) {
                System.out.println("wrong msg (from main method)");
            } catch (ConsoleException e) {
                e.printStackTrace();
            } catch (TradeException e) {
                e.printStackTrace();
            } finally {
                if (userMsg.equals("OF")) {
                    break;
                }
            }


        }

        System.out.println("EXIT FROM MAIN METHOD");
    }
}
