package go.takethespread.impl;

import go.takethespread.AbstractJDBCao;
import go.takethespread.Setting;
import go.takethespread.Settings;
import go.takethespread.exceptions.PersistException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class SettingDaoImpl extends AbstractJDBCao<Setting, Integer> {

    private static final String SELECT_SETTING = "SELECT id, name, value, dt FROM settings";
    private static final String UPDATE_SETTING = "UPDATE settings SET name = ?, value = ?, dt = ? WHERE id = ?";
    private static final String INSERT_SETTING = "INSERT INTO settings(name, value, dt) VALUES(?,?,?)";
    private static final String DELETE_SETTING = "DELETE FROM settings WHERE id = ?";

    public SettingDaoImpl(Connection connection) {
        super(connection);
    }

    @Override
    public String getSelectQuery() {
        return SELECT_SETTING;
    }

    @Override
    public String getUpdateQuery() {
        return UPDATE_SETTING;
    }

    @Override
    public String getDeleteQuery() {
        return DELETE_SETTING;
    }

    @Override
    public String getInsertQuery() {
        return INSERT_SETTING;
    }

    @Override
    protected List<Setting> parseResultSet(ResultSet rs) throws PersistException {
        List<Setting>result = new LinkedList<>();
        try {
            while (rs.next()){
                Setting st = new Setting();
                st.setId(rs.getInt("id"));
                st.setName(Settings.valueOf(rs.getString("name")));
                st.setValue(rs.getString("value"));
                st.setLastUpdate(new Date(rs.getLong("dt")));

                result.add(st);
            }
        } catch (Exception e) {
            throw new PersistException("Parsing resultSet error: ", e);
        }

        return result;
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement ps, Setting object) throws PersistException {
        try {
            ps.setString(1, object.getName().name());
            ps.setString(2, object.getValue());
            ps.setLong(3, object.getLastUpdate().getTime());
            ps.setInt(4, object.getId());
        } catch (Exception e) {
            throw new PersistException("Parsing resultSet error: ", e);
        }
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement ps, Setting object) throws PersistException {
        try {
            ps.setString(1, object.getName().name());
            ps.setString(2, object.getValue());
            ps.setLong(3, object.getLastUpdate().getTime());
        } catch (Exception e) {
            throw new PersistException("Parsing resultSet error: ", e);
        }
    }
}
