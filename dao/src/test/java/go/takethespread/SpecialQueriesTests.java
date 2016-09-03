package go.takethespread;

import go.takethespread.exceptions.PersistException;
import go.takethespread.impl.MarketDataDaoImpl;
import go.takethespread.impl.OrderDaoImpl;
import go.takethespread.impl.PostgresDaoFactoryImpl;

import java.sql.Connection;
import java.util.Iterator;
import java.util.List;

public class SpecialQueriesTests {
    public static void main(String[] args) {
        DaoFactory<Connection> daoFactory = new PostgresDaoFactoryImpl();
        try {
            MarketDataDaoImpl mdDao = new MarketDataDaoImpl(daoFactory.getContext());
            List<MarketData> list = mdDao.readLastActualMarketData(-3);

            printList(list);


            list = mdDao.readLastActualMarketData(15);

            printList(list);

            OrderDaoImpl orderDao = new OrderDaoImpl(daoFactory.getContext());
            List<Order> orderList = orderDao.readTodayFilledOrdersDesc();

            printList(orderList);

            orderList = orderDao.readAll();

            printList(orderList);

        } catch (PersistException e) {
            e.printStackTrace();
        }
    }


    public static void printList(List list) {
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

        System.out.println("-------------------------------");
    }
}
