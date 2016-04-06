package go.takethespread.NjTr;

public class Test {
    public static void main(String[] args) {
        String account = new String("Sim101");
        String instrument = new String("CL 06-16");

//        NjTrAPI.INSTANCE.SetUp("localhost", new Integer(36973));
//        NjTrAPI.INSTANCE.SubscribeMarketData(instrument);
        System.out.println(NjTrAPI.INSTANCE.Orders(account));
        NjTrAPI.INSTANCE.Command(NjTrCommand.PLACE.name(),account,instrument,NjTrAction.SELL.name(),2,NjTrOrderType.MARKET.name(),0d,0d,NjTrTIF.GTC.name(),"","","","");


        /*int count = 0;
        while (count < 1000) {
            System.out.println("cash: " + NjTrAPI.INSTANCE.CashValue(account));
            System.out.println("PnL: " + Math.round(NjTrAPI.INSTANCE.RealizedPnL(account)));
            System.out.println("pos: " +NjTrAPI.INSTANCE.MarketPosition(instrument, account));
            System.out.println("orders: " + NjTrAPI.INSTANCE.Orders(account));

            System.out.println("bid ask: " + NjTrAPI.INSTANCE.MarketData(instrument, 1) + " | " + NjTrAPI.INSTANCE.MarketData(instrument, 2));
            count++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        NjTrAPI.INSTANCE.UnsubscribeMarketData(instrument);*/

    }
}
