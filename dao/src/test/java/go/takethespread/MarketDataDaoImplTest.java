package go.takethespread;


import go.takethespread.exceptions.PersistException;
import go.takethespread.impl.PostgresDaoFactoryImpl;

import java.util.Date;

public class MarketDataDaoImplTest {
    public static void main(String[] args) {
        try {
            PostgresDaoFactoryImpl factory = new PostgresDaoFactoryImpl();
            GenericDao<MarketData, Integer> mdDao = factory.getDao(factory.getContext(), MarketData.class);
            MarketData md;
            long startT = System.currentTimeMillis();
            for (int i = 0; i < 1500; i++) {
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
                mdDao.persist(md);
            }

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
}
