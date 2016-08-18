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

    private static final String SELECT_MARKETDATA = "SELECT id, term, bid, ask, bidsize, asksize, dt FROM marketdata";
    private static final String UPDATE_MARKETDATA = "UPDATE marketdata SET term = ?, bid = ?, ask = ?, bidsize = ?, asksize = ?, dt = ? WHERE id = ?";
    private static final String INSERT_MARKETDATA = "INSERT INTO marketdata(term, bid, ask, bidsize, asksize, dt) VALUES(?,?,?,?,?,?)";
    private static final String DELETE_MARKETDATA = "DELETE FROM marketdata WHERE id = ?";

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
        List<MarketData>result = new LinkedList<>();
        try {
            while(rs.next()){
                MarketData md = new MarketData();
                md.setId(rs.getInt("id"));
                md.setTerm(Term.valueOf(rs.getString("term")));
                md.setBid(Money.dollars(rs.getDouble("bid")));
                md.setAsk(Money.dollars(rs.getDouble("ask")));
                md.setBidSize(rs.getInt("bidsize"));
                md.setAskSize(rs.getInt("asksize"));
                md.setDate(new java.util.Date(rs.getLong("dt")));

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
            ps.setString(1, object.getTerm().name());
            ps.setDouble(2, object.getBid().getAmount());
            ps.setDouble(3, object.getAsk().getAmount());
            ps.setInt(4, object.getBidSize());
            ps.setInt(5, object.getAskSize());
            ps.setLong(6, object.getDate().getTime());
            ps.setInt(7, object.getId());
        } catch (Exception e) {
            throw new PersistException("Parsing resultSet error: ", e);
        }
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement ps, MarketData object) throws PersistException {
        try {
            ps.setString(1, object.getTerm().name());
            ps.setDouble(2, object.getBid().getAmount());
            ps.setDouble(3, object.getAsk().getAmount());
            ps.setInt(4, object.getBidSize());
            ps.setInt(5, object.getAskSize());
            ps.setLong(6, object.getDate().getTime());
        } catch (Exception e) {
            throw new PersistException("Parsing resultSet error: ", e);
        }
    }
}
