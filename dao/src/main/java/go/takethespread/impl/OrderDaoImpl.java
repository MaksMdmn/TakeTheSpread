package go.takethespread.impl;

import go.takethespread.AbstractJDBCao;
import go.takethespread.Money;
import go.takethespread.Order;
import go.takethespread.exceptions.PersistException;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class OrderDaoImpl extends AbstractJDBCao<Order, Integer> {

    private static final String SELECT_ORDER = "SELECT id, ordid, instrument, dt, type, state, size, price, filled, pricefilled, deal FROM orders";
    private static final String UPDATE_ORDER = "UPDATE orders SET ordid = ?, instrument = ?,  dt=?, type = ?, state = ?, size = ?, price = ?, filled = ?, pricefilled = ?, deal=? WHERE id = ?";
    private static final String INSERT_ORDER = "INSERT INTO orders(ordid, instrument, dt, type, state, size, price, filled, pricefilled, deal) VALUES(?,?,?,?,?,?,?,?,?,?)";
    private static final String DELETE_ORDER = "DELETE FROM orders WHERE id = ?";

    public OrderDaoImpl(Connection connection) {
        super(connection);
    }

    @Override
    public String getSelectQuery() {
        return SELECT_ORDER;
    }

    @Override
    public String getUpdateQuery() {
        return UPDATE_ORDER;
    }

    @Override
    public String getDeleteQuery() {
        return DELETE_ORDER;
    }

    @Override
    public String getInsertQuery() {
        return INSERT_ORDER;
    }

    @Override
    protected List<Order> parseResultSet(ResultSet rs) throws PersistException {
        List<Order> result = new LinkedList<>();
        try {
            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setOrdId(rs.getString("ordid"));
                order.setInstrument(rs.getString("instrument"));
                order.setDate(new java.util.Date(rs.getLong("dt")));
                order.setType(Order.Type.valueOf(rs.getString("type")));
                order.setState(Order.State.valueOf(rs.getString("state")));
                order.setSize(rs.getInt("size"));
                order.setPrice(Money.dollars(rs.getDouble("price")));
                order.setFilled(rs.getInt("filled"));
                order.setPriceFilled(Money.dollars(rs.getDouble("pricefilled")));
                order.setDeal(Order.Deal.valueOf(rs.getString("deal")));

                result.add(order);
            }
        } catch (Exception e) {
            throw new PersistException("Parsing resultSet error: ", e);
        }

        return result;
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement ps, Order object) throws PersistException {
        try {
            ps.setString(1, object.getOrdId());
            ps.setString(2, object.getInstrument());
            ps.setLong(3, object.getDate().getTime());
            ps.setString(4, object.getType().name());
            ps.setString(5, object.getState().name());
            ps.setInt(6, object.getSize());
            ps.setDouble(7, object.getPrice().getAmount());
            ps.setInt(8, object.getFilled());
            ps.setDouble(9, object.getPriceFilled().getAmount());
            ps.setString(10, object.getDeal().name());
            ps.setInt(11, object.getId());

        } catch (Exception e) {
            throw new PersistException("Parsing resultSet error: ", e);
        }
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement ps, Order object) throws PersistException {
        try {
            ps.setString(1, object.getOrdId());
            ps.setString(2, object.getInstrument());
            ps.setLong(3, object.getDate().getTime());
            ps.setString(4, object.getType().name());
            ps.setString(5, object.getState().name());
            ps.setInt(6, object.getSize());
            ps.setDouble(7, object.getPrice().getAmount());
            ps.setInt(8, object.getFilled());
            ps.setDouble(9, object.getPriceFilled().getAmount());
            ps.setString(10,object.getDeal().name());
        } catch (Exception e) {
            throw new PersistException("Parsing resultSet error: ", e);
        }
    }
}
