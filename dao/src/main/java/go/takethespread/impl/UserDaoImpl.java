package go.takethespread.impl;

import go.takethespread.AbstractJDBCao;
import go.takethespread.User;
import go.takethespread.exceptions.PersistException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class UserDaoImpl extends AbstractJDBCao<User, Integer>  {

    private static final String SELECT_USER = "SELECT id, roleid, name, password FROM users";
    private static final String UPDATE_USER = "UPDATE users SET roleid = ?, name = ?, password = ? WHERE id = ?";
    private static final String INSERT_USER = "INSERT INTO users(roleid, name, password) VALUES(?,?,?)";
    private static final String DELETE_USER = "DELETE FROM users WHERE id = ?";

    public UserDaoImpl(Connection connection) {
        super(connection);
    }

    @Override
    public String getSelectQuery() {
        return SELECT_USER;
    }

    @Override
    public String getUpdateQuery() {
        return UPDATE_USER;
    }

    @Override
    public String getDeleteQuery() {
        return DELETE_USER;
    }

    @Override
    public String getInsertQuery() {
        return INSERT_USER;
    }

    @Override
    protected List<User> parseResultSet(ResultSet rs) throws PersistException {
        List<User> result = new LinkedList<>();
        try {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setRoleId(rs.getInt("roleid"));
                user.setName((rs.getString("name")));
                user.setPassword(rs.getString("password"));

                result.add(user);
            }
        } catch (Exception e) {
            throw new PersistException("Parsing resultSet error: ", e);
        }
        return result;
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement ps, User object) throws PersistException {
        try {
            ps.setInt(1, object.getRoleId());
            ps.setString(2, object.getName());
            ps.setString(3, object.getPassword());
            ps.setInt(4, object.getId());
        } catch (Exception e) {
            throw new PersistException("preparing user to update error: ", e);
        }
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement ps, User object) throws PersistException {
        try {
            ps.setInt(1, object.getRoleId());
            ps.setString(2, object.getName());
            ps.setString(3, object.getPassword());
        } catch (Exception e) {
            throw new PersistException("preparing user to insert error: ", e);
        }
    }
}
