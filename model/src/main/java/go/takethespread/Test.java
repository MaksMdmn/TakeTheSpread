package go.takethespread;

import com.sun.jna.Native;

public class Test {
    public static void main(String[] args) {

        System.load("D:\\xGarbage\\0_NINJATRADER_NOT_FOR_DELETE\\bin\\NtDirect.dll");
        NTdll INSTANCE = (NTdll) Native.loadLibrary("NtDirect", NTdll.class);

        String account = new String("Sim101");
        String instrument = new String("CL 06-16");

        INSTANCE.SetUp("localhost", 36973);
        System.out.println(INSTANCE.Connected(0));
        INSTANCE.SubscribeMarketData(instrument);
//        System.out.println(NjTrAPI.INSTANCE.Orders(account));
//        NjTrAPI.INSTANCE.Command(NjTrCommand.PLACE.name(),account,instrument,NjTrAction.SELL.name(),2,NjTrOrderType.MARKET.name(),0d,0d,NjTrTIF.GTC.name(),"","","","");


        int count = 0;
        while (count < 1000) {
            System.out.println("cash: " + INSTANCE.CashValue(account));
            System.out.println("PnL: " + Math.round(INSTANCE.RealizedPnL(account)));
            System.out.println("pos: " + INSTANCE.MarketPosition(instrument, account));
            System.out.println("orders: " + INSTANCE.Orders(account));

            System.out.println("bid ask: " + INSTANCE.MarketData(instrument, 1) + " | " + INSTANCE.MarketData(instrument, 2));
            count++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        INSTANCE.UnsubscribeMarketData(instrument);

    }
}
