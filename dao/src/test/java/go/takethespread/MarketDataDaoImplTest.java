package go.takethespread;

import go.takethespread.exceptions.PersistException;
import go.takethespread.impl.PostgresDaoFactoryImpl;

import java.sql.Connection;
import java.util.Date;

public class MarketDataDaoImplTest {
    public static void main(String[] args) {
        MarketData md1 = new MarketData();
        MarketData md2 = new MarketData();
        md1.setTerm(Term.NEAR);
        md1.setBid(Money.dollars(43.2));
        md1.setAsk(Money.dollars(43.7));
        md1.setLast(Money.dollars(412.2d));
        md1.setBidSize(412);
        md1.setAskSize(666);
        md1.setDate(new Date());

        md2.setTerm(Term.FAR);
        md2.setBid(Money.dollars(48.1));
        md2.setAsk(Money.dollars(49.4));
        md2.setLast(Money.dollars(41112.2d));
        md2.setBidSize(12);
        md2.setAskSize(555);
        md2.setDate(new Date(new Date().getTime() + 50000L));

        DaoFactory<Connection> daoFactory = new PostgresDaoFactoryImpl();
        try {
            GenericDao<MarketData, Integer> mdDao = daoFactory.getDao(daoFactory.getContext(), MarketData.class);
            System.out.println("- TEST INSERT -");
            md1.setId(mdDao.persist(md1));
            md2.setId(mdDao.persist(md2));
            System.out.println("- TEST SELECT - " );
            System.out.println(mdDao.read(md1.getId()));

            System.out.println("- TEST SELECT ALL -");
            System.out.println(mdDao.readAll());

            System.out.println("- TEST UPDATE -");
            md1.setAskSize(9999);
            mdDao.update(md1);
            System.out.println(mdDao.read(md1.getId()));

            System.out.println("- TEST DELETE -");
            mdDao.delete(md1);
            System.out.println(mdDao.readAll());


        } catch (PersistException e) {
            e.printStackTrace();
        }


    }
}
