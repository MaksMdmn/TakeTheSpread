package go.takethespread.managers;

import go.takethespread.DaoFactory;
import go.takethespread.GenericDao;
import go.takethespread.Order;
import go.takethespread.exceptions.PersistException;
import go.takethespread.impl.PostgresDaoFactoryImpl;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

public class OrderManager {

    private static OrderManager instance;

    private DaoFactory<Connection> daoFactory;
    private GenericDao<Order, Integer> orderDao;
    private List<String> wroteOrderIdList;
    private LinkedBlockingDeque<Order> unviewedOrders;
    private int dealsNumber;

    private OrderManager() {
        initDataBaseEntities();
        wroteOrderIdList = new LinkedList<>();
        unviewedOrders = new LinkedBlockingDeque<>();
        dealsNumber = 0;
    }

    public static OrderManager getInstance() {
        if (instance == null) {
            instance = new OrderManager();
        }
        return instance;
    }

    public void addOrderToDBWithoutDuplicates(List<Order> orders) {
        try {
            if (wroteOrderIdList.isEmpty()) {
                for (Order o : orders) {
                    orderDao.persist(o);
                    wroteOrderIdList.add(o.getOrdId());
                    unviewedOrders.add(o);
                    updateDealsNumber(o);
                }
            } else {
                for (Order o : orders) {
                    if (!wroteOrderIdList.contains(o.getOrdId())) {
                        orderDao.persist(o);
                        wroteOrderIdList.add(o.getOrdId());
                        unviewedOrders.add(o);
                        updateDealsNumber(o);
                    }
                }
            }
        } catch (PersistException e) {
            e.printStackTrace();
        }
    }

    public int getDealsNumber() {
        return dealsNumber;
    }

    private void initDataBaseEntities() {
        try {
            daoFactory = new PostgresDaoFactoryImpl();
            orderDao = daoFactory.getDao(daoFactory.getContext(), Order.class);
        } catch (PersistException e) {
            e.printStackTrace();
        }
    }

    private void updateDealsNumber(Order o) {
        if (o.getState() == Order.State.Filled) {
            dealsNumber += o.getFilled();
        }
    }
}
