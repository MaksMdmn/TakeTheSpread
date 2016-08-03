package go.takethespread;

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

        TradeSystemInfo info = TradeSystemInfo.getInstance();
        TradeBlotter blotter = new TradeBlotter(info, manager);

        System.out.println("update");
        blotter.updateMarketData();

        int counter = 0;
        while (!userRow.equals("done")) {
            try {
                blotter.updateMarketData();
                blotter.updateAuxiliaryData();
                System.out.println("sprd: " + blotter.getSpreadCalculator().getCurSpread().getAmount());

                if (userRow.equals("pa")) {
                    blotter.getSpreadCalculator().pause();
                    System.out.println(blotter.getSpreadCalculator().isPauseEnabled());
                    userRow = "";
                } else if(userRow.equals("ph")){
                    blotter.getSpreadCalculator().testAddPhonyData();
                    System.out.println("phony data added");
                    userRow = "";
                }
                System.out.println(++counter);
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("if all seems good, bb");
        manager.finishingJob();
        System.out.println("already off");
    }
}
