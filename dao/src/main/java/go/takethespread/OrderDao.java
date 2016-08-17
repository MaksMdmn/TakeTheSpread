package go.takethespread;

import go.takethespread.exceptions.PersistException;

import java.util.List;

public interface OrderDao {
    public Integer persist(Order object) throws PersistException;

    public Order read(int key) throws PersistException;

    public void update(Order object) throws PersistException;

    public void delete(Order object) throws PersistException;

    public List<Order> readAll() throws PersistException;
}
