package go.takethespread;

import go.takethespread.exceptions.DbException;

import java.util.List;

public interface UserDao  extends GenericDao<User>{

    public User getById(int id) throws DbException;

    public User getByName(String name) throws DbException;

    public List<User> getAll() throws DbException;

    public int insert(User user) throws DbException;

    public void update(User user, int id) throws DbException;

    public void deleteById(int id) throws DbException;

    public void deleteByName(String name) throws DbException;
}
