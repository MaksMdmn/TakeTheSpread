package go.takethespread;

import go.takethespread.exceptions.PersistException;

import java.io.Serializable;
import java.sql.*;
import java.util.List;

public abstract class AbstractJDBCao<T extends Identified<PK>, PK extends Serializable> implements GenericDao<T, PK> {

    private Connection connection;
    private static final String ADD_TO_SELECT = " WHERE id = ?";

    public AbstractJDBCao(Connection connection) {
        this.connection = connection;
    }

    public abstract String getSelectQuery();

    public abstract String getUpdateQuery();

    public abstract String getDeleteQuery();

    public abstract String getInsertQuery();

    protected abstract List<T> parseResultSet(ResultSet rs) throws PersistException;

    protected abstract void prepareStatementForUpdate(PreparedStatement ps, T object) throws PersistException;

    protected abstract void prepareStatementForInsert(PreparedStatement ps, T object) throws PersistException;

    @Override
    public Integer persist(T object) throws PersistException {
        Integer answer;

        if (object.getId() != null) {
            throw new PersistException("Object already has id " + object.getId());
        }

        String sql = getInsertQuery();
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            prepareStatementForInsert(ps, object);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                answer = rs.getInt(1);
            } else {
                throw new PersistException("cannot read id of inserted object.");
            }
        } catch (Exception e) {
            throw new PersistException("we have another exception: ", e);
        }

        return answer;
    }

    @Override
    public T read(int key) throws PersistException {
        List<T> list;
        T answer;

        String sql = getSelectQuery();
        sql += ADD_TO_SELECT;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, key);
            ResultSet rs = ps.executeQuery();
            list = parseResultSet(rs);
        } catch (Exception e) {
            throw new PersistException("we have another exception: ", e);
        }

        if (list == null || list.size() == 0) {
            answer = null;
        } else {
            answer = list.iterator().next();
        }

        if (list.size() > 1) {
            throw new PersistException("Received more than 1 record.");
        }

        return answer;
    }

    @Override
    public List<T> readAll() throws PersistException {
        List<T> answer;
        String sql = getSelectQuery();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            answer = parseResultSet(rs);
        } catch (Exception e) {
            throw new PersistException("we have another exception: ", e);
        }

        return answer;
    }

    @Override
    public void update(T object) throws PersistException {
        String sql = getUpdateQuery();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            prepareStatementForUpdate(ps, object);
            int count = ps.executeUpdate();
            if (count != 1) {
                throw new PersistException("Were updated more than 1 record: " + count + ", object: " + object.toString());
            }

        } catch (Exception e) {
            throw new PersistException("we have another exception: ", e);
        }
    }

    @Override
    public void delete(T object) throws PersistException {
        String sql = getDeleteQuery();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setObject(1, object.getId());
            int count = ps.executeUpdate();
            if (count == 0) {
                throw new PersistException("Object with such id doesn't exist: " + object.getId() + " db answer: " + count);
            }
            if (count != 1) {
                throw new PersistException("Were deleted more than 1 record: " + count + ", object: " + object.toString());
            }

        } catch (Exception e) {
            throw new PersistException("we have another exception: ", e);
        }
    }
}
