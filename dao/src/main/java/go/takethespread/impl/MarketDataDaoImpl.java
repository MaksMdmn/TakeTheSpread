package go.takethespread.impl;

import go.takethespread.AbstractJDBCao;
import go.takethespread.MarketData;
import go.takethespread.Money;
import go.takethespread.Term;
import go.takethespread.exceptions.PersistException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class MarketDataDaoImpl extends AbstractJDBCao<MarketData, Integer> {

    private static String SELECT_MARKETDATA = "SELECT id, dt, bid_n, bidSize_n, ask_n, askSize_n,  bid_f, bidSize_f, ask_f, askSize_f FROM marketdata";
    private static String UPDATE_MARKETDATA = "UPDATE marketdata SET dt = ?, bid_n = ?, bidSize_n = ?, ask_n = ?, askSize_n = ?,  bid_f = ?, bidSize_f = ?, ask_f = ?, askSize_f = ? WHERE id = ?";
    private static String INSERT_MARKETDATA = "INSERT INTO marketdata(dt, bid_n, bidSize_n, ask_n, askSize_n,  bid_f, bidSize_f, ask_f, askSize_f) VALUES(?,?,?,?,?,?,?,?,?)";
    private static String DELETE_MARKETDATA = "DELETE FROM marketdata WHERE id = ?";

    private static String SPECIAL_SELECT_MARKETDATA = SELECT_MARKETDATA + " ORDER BY id DESC LIMIT ";

    public MarketDataDaoImpl(Connection connection) {
        super(connection);
    }

    @Override
    public String getSelectQuery() {
        return SELECT_MARKETDATA;
    }

    @Override
    public String getUpdateQuery() {
        return UPDATE_MARKETDATA;
    }

    @Override
    public String getDeleteQuery() {
        return DELETE_MARKETDATA;
    }

    @Override
    public String getInsertQuery() {
        return INSERT_MARKETDATA;
    }

    @Override
    protected List<MarketData> parseResultSet(ResultSet rs) throws PersistException {
        List<MarketData> result = new LinkedList<>();
        try {
            while (rs.next()) {
                MarketData md = new MarketData();
                md.setId(rs.getInt("id"));
                md.setDate(new java.util.Date(rs.getLong("dt")));
                md.setBid_n(Money.dollars(rs.getDouble("bid_n")));
                md.setBidSize_n(rs.getInt("bidSize_n"));
                md.setAsk_n(Money.dollars(rs.getDouble("ask_n")));
                md.setAskSize_n(rs.getInt("askSize_n"));
                md.setBid_f(Money.dollars(rs.getDouble("bid_n")));
                md.setBidSize_f(rs.getInt("bidSize_n"));
                md.setAsk_f(Money.dollars(rs.getDouble("ask_n")));
                md.setAskSize_f(rs.getInt("askSize_n"));
                result.add(md);
            }
        } catch (Exception e) {
            throw new PersistException("Parsing resultSet error: ", e);
        }


        return result;
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement ps, MarketData object) throws PersistException {
        try {
            ps.setLong(1, object.getDate().getTime());
            ps.setDouble(2, object.getBid_n().getAmount());
            ps.setInt(3, object.getBidSize_n());
            ps.setDouble(4, object.getAsk_n().getAmount());
            ps.setInt(5, object.getAskSize_n());
            ps.setDouble(6, object.getBid_f().getAmount());
            ps.setInt(7, object.getBidSize_f());
            ps.setDouble(8, object.getAsk_f().getAmount());
            ps.setInt(9, object.getAskSize_f());
            ps.setInt(10, object.getId());
        } catch (Exception e) {
            throw new PersistException("Parsing resultSet error: ", e);
        }
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement ps, MarketData object) throws PersistException {
        try {
            ps.setLong(1, object.getDate().getTime());
            ps.setDouble(2, object.getBid_n().getAmount());
            ps.setInt(3, object.getBidSize_n());
            ps.setDouble(4, object.getAsk_n().getAmount());
            ps.setInt(5, object.getAskSize_n());
            ps.setDouble(6, object.getBid_f().getAmount());
            ps.setInt(7, object.getBidSize_f());
            ps.setDouble(8, object.getAsk_f().getAmount());
            ps.setInt(9, object.getAskSize_f());
        } catch (Exception e) {
            throw new PersistException("Parsing resultSet error: ", e);
        }
    }

    public List<MarketData> readLastActualMarketData(int numberOfRows) throws PersistException {
        if (numberOfRows <= 0) {
            numberOfRows = 1; //default
        }
        List<MarketData> answer;
        String oldQuery = SELECT_MARKETDATA;
        SELECT_MARKETDATA = SPECIAL_SELECT_MARKETDATA + numberOfRows;
        answer = this.readAll();
        SELECT_MARKETDATA = oldQuery;
        return answer;
    }
}
