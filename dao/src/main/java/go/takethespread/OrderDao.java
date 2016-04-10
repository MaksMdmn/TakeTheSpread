package go.takethespread;

import go.takethespread.exceptions.DbException;

import java.util.List;

public interface OrderDao extends GenericDao<Order> {

    public Order getById(int id) throws DbException;

    public List<Order> getAll() throws DbException;

    public int insert(Order order) throws DbException;

    public void update(Order order, int id) throws DbException;

    public void deleteById(int id) throws DbException;
}
