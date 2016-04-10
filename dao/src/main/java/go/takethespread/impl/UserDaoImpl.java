package go.takethespread.impl;

import go.takethespread.User;
import go.takethespread.UserDao;
import go.takethespread.exceptions.DbException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {

    private static final String USERS_SELECT_BY_ID = "SELECT nick, pass, roleid FROM users WHERE id = ? ";
    private static final String USERS_SELECT_BY_NAME = "SELECT nick, pass, roleid FROM users WHERE nick = ?";
    private static final String USERS_SELECT_ALL = "SELECT nick, pass, roleid FROM users";
    private static final String USERS_INSERT = "INSERT INTO users(nick,pass,roleid) VALUES(?,?,?)";
    private static final String USERS_UPDATE = "UPDATE users SET nick = ?, pass = ? roleid = ? WHERE id = ?";
    private static final String USERS_DELETE_BY_ID = "DELETE FROM users WHERE id = ?";

    private Connection cn;
    private DaoHelper daoHelper;

    public UserDaoImpl() {
    }

    @Override
    public User getById(int id) throws DbException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;
        try {
            prepareConnection();
            ps = cn.prepareStatement(USERS_SELECT_BY_ID);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            rs.next();

            user = new User();
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            user.setRoleId(rs.getInt("role"));

        } catch (SQLException e) {
            throw new DbException("Can' execute the following SQL: " + USERS_SELECT_BY_ID, e);
        } finally {
            daoHelper.closeDataBaseEntities(ps, rs, cn);
        }
        return user;
    }

    @Override
    public User getByName(String name) throws DbException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;
        try {
            prepareConnection();
            ps = cn.prepareStatement(USERS_SELECT_BY_NAME);
            ps.setString(1, name);
            rs = ps.executeQuery();
            rs.next();

            user = new User();
            user.setName(name);
            user.setPassword(rs.getString("password"));
            user.setRoleId(rs.getInt("role"));

        } catch (SQLException e) {
            throw new DbException("Can' execute the following SQL: " + USERS_SELECT_BY_NAME, e);
        } finally {
            daoHelper.closeDataBaseEntities(ps, rs, cn);
        }
        return user;
    }

    @Override
    public List<User> getAll() throws DbException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<User> users = null;
        try {
            prepareConnection();
            ps = cn.prepareStatement(USERS_SELECT_ALL);
            rs = ps.executeQuery();

            users = new ArrayList<>();
            while (rs.next()) {
                User user = new User();
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                user.setRoleId(rs.getInt("role"));

                users.add(user);
            }
        } catch (SQLException e) {
            throw new DbException("Can' execute the following SQL: " + USERS_SELECT_ALL, e);
        } finally {
            daoHelper.closeDataBaseEntities(ps, rs, cn);
        }

        return users;
    }

    @Override
    public int insert(User user) throws DbException {
        PreparedStatement ps = null;
        int result;
        try {
            prepareConnection();
            ps = cn.prepareStatement(USERS_INSERT, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getName());
            ps.setString(2, user.getPassword());
            ps.setInt(3, user.getRoleId());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    result = (int) rs.getLong("id");
                } else {
                    throw new DbException("Inserting failed on query: '" + USERS_INSERT + "' no ID obtained.");
                }

            }
        } catch (SQLException e) {
            throw new DbException("Can't execute the following SQL: " + USERS_INSERT, e);
        } finally {
            daoHelper.closeDataBaseEntities(ps, null, cn);
        }

        return result;
    }

    @Override
    public void update(User user, int id) throws DbException {
        PreparedStatement ps = null;
        try {
            prepareConnection();
            ps = cn.prepareStatement(USERS_UPDATE, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getName());
            ps.setString(2, user.getPassword());
            ps.setInt(3, user.getRoleId());
            ps.setInt(4, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DbException("Can't execute the following SQL: " + USERS_UPDATE, e);
        } finally {
            daoHelper.closeDataBaseEntities(ps, null, cn);
        }
    }

    @Override
    public void deleteById(int id) throws DbException {
        PreparedStatement ps = null;
        try {
            prepareConnection();
            ps = cn.prepareStatement(USERS_DELETE_BY_ID);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DbException("Can't execute the following SQL: " + USERS_DELETE_BY_ID, e);
        } finally {
            daoHelper.closeDataBaseEntities(ps, null, cn);
        }
    }

    private void prepareConnection() {
        daoHelper = new DaoHelper();
        daoHelper.initConn();
        cn = daoHelper.getConnection();
    }
}
