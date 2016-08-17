package go.takethespread;

import go.takethespread.exceptions.PersistException;

import java.util.List;

public interface MarketDataDao {
    public Integer persist(MarketData object) throws PersistException;

    public MarketData read(int key) throws PersistException;

    public void update(MarketData object) throws PersistException;

    public void delete(MarketData object) throws PersistException;

    public List<MarketData> readAll() throws PersistException;
}
