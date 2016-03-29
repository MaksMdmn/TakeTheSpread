package go.takethespread;

import java.util.List;

public interface GenericDao<T> {

    public long create(T obj);

    public T read(int id);

    public List<T> readAll();

    public void update(T obj);

    public void delete(int id);

}
