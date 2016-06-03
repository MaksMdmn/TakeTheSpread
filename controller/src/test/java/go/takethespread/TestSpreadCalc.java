package go.takethespread;

import go.takethespread.fsa.SpreadCalculator;
import go.takethespread.fsa.TradeBlotter;
import go.takethespread.fsa.TradeSystemInfo;
import go.takethespread.managers.ExternalManager;
import go.takethespread.managers.socket.NTTcpExternalManagerImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TestSpreadCalc {
    public static volatile String userRow = "";

    public static void main(String[] args) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                    while (!userRow.equals("done")) {
                        userRow = reader.readLine();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        ExternalManager manager = NTTcpExternalManagerImpl.getInstance();

        System.out.println("start!!!");
        manager.startingJob();

        TradeSystemInfo info = new TradeSystemInfo();
        TradeBlotter blotter = new TradeBlotter(info, manager);

        SpreadCalculator calculator = new SpreadCalculator(blotter, info);

        System.out.println("update");
        blotter.updateMainInfo();

        while (!userRow.equals("done")) {
            try {
                blotter.updateMainInfo();
                calculator.collectCalcData();
                System.out.println("sprd: " + calculator.calcSpread().getAmount());

                if (userRow.equals("p")) {
                    calculator.pause();
                    System.out.println(calculator.isPauseEnabled());
                    userRow = "";
                }

                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("if all seems good, bb");
        manager.finishingJob();
        System.out.println("already off");
    }
}
