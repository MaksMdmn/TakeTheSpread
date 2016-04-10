package go.takethespread.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public final class DaoHelper {
    private String URL;
    private String USER;
    private String PASSWORD;
    private Connection connection;

    public DaoHelper() {

    }

    public Connection getConnection() {
        return connection;
    }

    public void initConn() {

        if (connection == null) {

            Properties properties = new Properties();
            try (InputStream is = getClass().getResourceAsStream("/config.properties")) {
                properties.load(is);
                URL = properties.getProperty("url");
                USER = properties.getProperty("user");
                PASSWORD = properties.getProperty("password");

                Class.forName("org.postgresql.Driver");

                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    protected synchronized void closeDataBaseEntities(Statement stm, ResultSet rs, Connection cn) {
        try {
            if (stm != null) stm.close();
            if (rs != null) rs.close();
            if (cn != null) cn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
