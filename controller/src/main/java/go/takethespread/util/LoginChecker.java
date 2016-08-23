package go.takethespread.util;

import go.takethespread.DaoFactory;
import go.takethespread.User;
import go.takethespread.exceptions.PersistException;
import go.takethespread.impl.PostgresDaoFactoryImpl;
import go.takethespread.impl.UserDaoImpl;

import java.sql.Connection;

public final class LoginChecker {
    private DaoFactory<Connection> daoFactory;
    private UserDaoImpl userDao;

    public LoginChecker() {
    }

    public boolean verifyUser(String login, String password) {
        try {
            daoFactory = new PostgresDaoFactoryImpl();
            userDao = new UserDaoImpl(daoFactory.getContext());
            User user = userDao.readUserByName(login);
            if (user.getPassword().equals(password)) {
                return true;
            }
        } catch (PersistException e) {
            e.printStackTrace();
        }
        return false;
    }

}
