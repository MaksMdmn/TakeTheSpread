package go.takethespread.impl;

import go.takethespread.AbstractJDBCao;
import go.takethespread.MarketData;
import go.takethespread.MarketDataDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class MarketDataDaoImpl extends AbstractJDBCao<MarketData, Integer> implements MarketDataDao {
    public MarketDataDaoImpl(Connection connection) {
        super(connection);
    }

    @Override
    public String getSelectQuery() {
        return null;
    }

    @Override
    public String getUpdateQuery() {
        return null;
    }

    @Override
    public String getDeleteQuery() {
        return null;
    }

    @Override
    public String getInsertQuery() {
        return null;
    }

    @Override
    protected List<MarketData> parseResultSet(ResultSet resultSet) {
        return null;
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement ps, MarketData object) {

    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement ps, MarketData object) {

    }
}
