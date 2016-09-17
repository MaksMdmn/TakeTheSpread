package go.takethespread;

import go.takethespread.exceptions.PersistException;
import go.takethespread.impl.PostgresDaoFactoryImpl;
import go.takethespread.managers.MarketDataCollector;

import java.util.Date;
import java.util.List;

public class Test_pseudoWork_of_MDCollector {
    public static void main(String[] args) {
        try {
            PostgresDaoFactoryImpl factory = new PostgresDaoFactoryImpl();
            GenericDao<MarketData, Integer> mdDao = factory.getDao(factory.getContext(), MarketData.class);
            MarketDataCollector collector = MarketDataCollector.getInstance();

            MarketData md;
            long startT = System.currentTimeMillis();
            for (int i = 0; i < 2999; i++) {
                md = new MarketData();
                md.setDate(new Date());
                md.setBid_n(getRandomNumbDouble());
                md.setBidSize_n(getRandomNumbInt());
                md.setAsk_n(getRandomNumbDouble());
                md.setAskSize_n(getRandomNumbInt());
                md.setBid_f(getRandomNumbDouble());
                md.setBidSize_f(getRandomNumbInt());
                md.setAsk_f(getRandomNumbDouble());
                md.setAskSize_f(getRandomNumbInt());
                collector.TESTcollectMarketData(md);

                switch (i) {
                    case 1000:
                        if (collector.isItTimeToPushToDb()) {
                            collector.pushToDataBase();
                        }
                        checkDBandPrintMsg(i, mdDao);
                        break;
                    case 1500:
                        if (collector.isItTimeToPushToDb()) {
                            collector.pushToDataBase();
                        }
                        checkDBandPrintMsg(i, mdDao);
                        break;
                    case 1501:
                        if (collector.isItTimeToPushToDb()) {
                            collector.pushToDataBase();
                        }
                        checkDBandPrintMsg(i, mdDao);
                        break;
                    case 2998:
                        if (collector.isItTimeToPushToDb()) {
                            collector.pushToDataBase();
                        }
                        checkDBandPrintMsg(i, mdDao);
                        break;
                    default:
                        break;
                }
            }
            collector.pushToDataBase();
            checkDBandPrintMsg(9999, mdDao);
            System.out.println(System.currentTimeMillis() - startT + " ms.");

        } catch (PersistException e) {
            e.printStackTrace();
        }

    }


    public static Money getRandomNumbDouble() {
        return Money.dollars(Math.random() * 10);
    }

    public static int getRandomNumbInt() {
        return (int) (Math.random() * 100);
    }

    public static void checkDBandPrintMsg(int addToMsg, GenericDao<MarketData, Integer> mdDao) {
        System.out.println("i: " + addToMsg);
        try {
            List<MarketData> mdList = mdDao.readAll();
            if (mdList == null) {
                System.out.println("list is null now");
            } else {
                System.out.println("list size: " + mdList.size());
            }
        } catch (PersistException e) {
            e.printStackTrace();
        }
    }

}
