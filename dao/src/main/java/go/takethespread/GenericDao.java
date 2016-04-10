package go.takethespread;

import go.takethespread.exceptions.DbException;

import java.util.List;

public interface GenericDao<T> {

    public T getById(int id) throws DbException;

    public List<T> getAll() throws DbException;

    public int insert(T object) throws DbException;

    public void update(T object, int id) throws DbException;

    public void deleteById(int id) throws DbException;
}
