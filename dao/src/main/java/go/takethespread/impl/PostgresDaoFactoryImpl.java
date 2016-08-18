package go.takethespread.impl;

import go.takethespread.*;
import go.takethespread.exceptions.PersistException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class PostgresDaoFactoryImpl implements DaoFactory<Connection> {

    private String user = "postgres";
    private String password = "31444";
    private String url = "jdbc:postgresql://127.0.0.1:5432/lobstadb";
    private String driver = "org.postgresql.Driver";
    private Map<Class, DaoCreator> creatorsMap;


    @Override
    public Connection getContext() throws PersistException {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new PersistException("get exception, when connection was creating", e);
        }

        return connection;
    }

    @Override
    public GenericDao getDao(Connection connection, Class dtoClass) throws PersistException {
        DaoCreator creator = creatorsMap.get(dtoClass);
        if (creator == null) {
            throw new PersistException("Pass class not found: " + dtoClass);
        }
        return creator.create(connection);
    }

    public PostgresDaoFactoryImpl() {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        creatorsMap = new HashMap<Class, DaoCreator>();
        creatorsMap.put(MarketData.class, new DaoCreator<Connection>() {
            @Override
            public GenericDao create(Connection connection) {
                return new MarketDataDaoImpl(connection);
            }
        });

        creatorsMap.put(User.class, new DaoCreator<Connection>() {
            @Override
            public GenericDao create(Connection connection) {
                return new UserDaoImpl(connection);
            }

        });

        creatorsMap.put(Order.class, new DaoCreator<Connection>() {
            @Override
            public GenericDao create(Connection connection) {
                return new OrderDaoImpl(connection);
            }

        });

    }
}
