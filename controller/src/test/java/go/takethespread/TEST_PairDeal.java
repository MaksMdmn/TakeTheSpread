package go.takethespread;

import go.takethespread.fsa.TradeSystemInfo;
import go.takethespread.managers.ExternalManager;
import go.takethespread.managers.socket.NTTcpExternalManagerImpl;

import java.util.Date;

public class TEST_PairDeal {
    public static void main(String[] args) throws InterruptedException {
        ExternalManager manager = NTTcpExternalManagerImpl.getInstance();
        TradeSystemInfo info = TradeSystemInfo.getInstance();

        System.out.println("connection?");
        manager.startingJob("localhost", 8085);
        while (!manager.isConnOkay()) {
            /*NOP*/
        }

        System.out.println("got it");
        Thread.sleep(5000);

        System.out.println("start sending: " + new Date());
        long x1 = System.currentTimeMillis();
        manager.sendPairMarketBuySell(info.instrument_n, 1, info.instrument_f, 1);
        long x2 = System.currentTimeMillis();
        System.out.println("sent: " + new Date());

        System.out.println("diff: " + diffBtwMlls(x1, x2));
        manager.finishingJob();
    }

    public static long diffBtwMlls(long x1, long x2) {
        return x2 - x1;
    }
}
