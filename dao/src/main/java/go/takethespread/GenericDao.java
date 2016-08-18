package go.takethespread;

import go.takethespread.exceptions.PersistException;

import java.io.Serializable;
import java.util.List;

public interface GenericDao<T extends Identified<PK>, PK extends Serializable> {

    public Integer persist(T object) throws PersistException;

    public T read(int key) throws PersistException;

    public void update(T object) throws PersistException;

    public void delete(T object) throws PersistException;

    public List<T> readAll() throws PersistException;

}
