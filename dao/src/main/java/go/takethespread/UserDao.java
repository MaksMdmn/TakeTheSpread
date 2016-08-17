package go.takethespread;

import go.takethespread.exceptions.PersistException;

import java.util.List;

public interface UserDao {
    public Integer persist(User object) throws PersistException;

    public User read(int key) throws PersistException;

    public void update(User object) throws PersistException;

    public void delete(User object) throws PersistException;

    public List<User> readAll() throws PersistException;
}
