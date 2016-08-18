package go.takethespread;

import go.takethespread.exceptions.PersistException;
import go.takethespread.impl.PostgresDaoFactoryImpl;

import java.sql.Connection;
import java.util.Date;

public class OrderDaoImplTest {
    public static void main(String[] args) {
        Order o1 = new Order();
        Order o2 = new Order();
        Order o3 = new Order();

        o1.setDate(new Date());
        o1.setDeal(Order.Deal.Buy);
        o1.setFilled(3);
        o1.setOrdId("123421321");
        o1.setInstrument("CL 09-16");
        o1.setPrice(Money.dollars(56.2));
        o1.setSize(10);
        o1.setPriceFilled(Money.dollars(56.2));
        o1.setState(Order.State.Filled);
        o1.setType(Order.Type.Market);

        o2.setDate(new Date());
        o2.setDeal(Order.Deal.Buy);
        o2.setFilled(3);
        o2.setOrdId("71321");
        o2.setInstrument("CL 09-16");
        o2.setPrice(Money.dollars(6.2));
        o2.setSize(3);
        o2.setPriceFilled(Money.dollars(5.2));
        o2.setState(Order.State.Filled);
        o2.setType(Order.Type.Market);

        o3.setDate(new Date());
        o3.setDeal(Order.Deal.Sell);
        o3.setFilled(3);
        o3.setOrdId("178321");
        o3.setInstrument("CL 08-16");
        o3.setPrice(Money.dollars(5.2));
        o3.setSize(10);
        o3.setPriceFilled(Money.dollars(6.2));
        o3.setState(Order.State.Accepted);
        o3.setType(Order.Type.Market);


        DaoFactory<Connection> daoFactory = new PostgresDaoFactoryImpl();
        try {
            GenericDao<Order, Integer> orderDao = daoFactory.getDao(daoFactory.getContext(), Order.class);
            System.out.println("- TEST INSERT -");
            int key = orderDao.persist(o1);
            o1.setId(key);
            System.out.println("- TEST SELECT - + id: " + key);
            System.out.println(orderDao.read(key));
            o2.setId(orderDao.persist(o2));
            o3.setId(orderDao.persist(o3));

            System.out.println("- TEST SELECT ALL -");
            System.out.println(orderDao.readAll());

            System.out.println("- TEST UPDATE -");
            o1.setPrice(Money.dollars(9999d));
            orderDao.update(o1);
            System.out.println(orderDao.read(key));

            System.out.println("- TEST DELETE -");
            orderDao.delete(o3);
            System.out.println(orderDao.readAll());

        } catch (PersistException e) {
            e.printStackTrace();
        }

    }
}
