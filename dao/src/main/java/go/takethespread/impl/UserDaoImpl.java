package go.takethespread.impl;

import go.takethespread.AbstractJDBCao;
import go.takethespread.User;
import go.takethespread.UserDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class UserDaoImpl extends AbstractJDBCao<User, Integer> implements UserDao {

    public UserDaoImpl(Connection connection) {
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
    protected List<User> parseResultSet(ResultSet resultSet) {
        return null;
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement ps, User object) {

    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement ps, User object) {

    }
}
