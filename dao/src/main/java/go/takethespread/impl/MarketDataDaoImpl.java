package go.takethespread.impl;

import go.takethespread.MarketData;
import go.takethespread.MarketDataDao;
import go.takethespread.exceptions.DbException;

import java.util.List;

public class MarketDataDaoImpl implements MarketDataDao {
    @Override
    public MarketData getById(int id) throws DbException {
        return null;
    }

    @Override
    public List<MarketData> getAll() throws DbException {
        return null;
    }

    @Override
    public int insert(MarketData marketData) throws DbException {
        return 0;
    }

    @Override
    public void update(MarketData marketData, int id) throws DbException {

    }

    @Override
    public void deleteById(int id) throws DbException {

    }
}
