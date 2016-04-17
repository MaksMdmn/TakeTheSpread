package go.takethespread;


import go.takethespread.exceptions.DbException;

import java.util.List;

public interface MarketDataDao extends GenericDao<MarketData> {

    public MarketData getById(int id) throws DbException;

    public List<MarketData> getAll() throws DbException;

    public int insert(MarketData marketData) throws DbException;

    public void update(MarketData marketData, int id) throws DbException;

    public void deleteById(int id) throws DbException;
}
