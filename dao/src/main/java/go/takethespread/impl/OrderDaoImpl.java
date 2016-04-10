package go.takethespread.impl;

import go.takethespread.Order;
import go.takethespread.OrderDao;
import go.takethespread.exceptions.DbException;

import java.util.List;

public class OrderDaoImpl implements OrderDao {
    //думаю пока подождёт, так как ордера могут быть в терминале\отчётах

    @Override
    public Order getById(int id) throws DbException {
        return null;
    }

    @Override
    public List<Order> getAll() throws DbException {
        return null;
    }

    @Override
    public int insert(Order order) throws DbException {
        return 0;
    }

    @Override
    public void update(Order order, int id) throws DbException {

    }

    @Override
    public void deleteById(int id) throws DbException {

    }
}
