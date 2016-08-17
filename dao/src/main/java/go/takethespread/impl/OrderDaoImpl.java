package go.takethespread.impl;

import go.takethespread.AbstractJDBCao;
import go.takethespread.Order;
import go.takethespread.OrderDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class OrderDaoImpl extends AbstractJDBCao<Order, Integer> implements OrderDao {
    public OrderDaoImpl(Connection connection) {
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
    protected List<Order> parseResultSet(ResultSet resultSet) {
        return null;
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement ps, Order object) {

    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement ps, Order object) {

    }
}
