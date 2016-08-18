package go.takethespread;


import go.takethespread.exceptions.PersistException;
import go.takethespread.impl.PostgresDaoFactoryImpl;

import java.sql.Connection;

public class UserDaoImplTest {
    public static void main(String[] args) {
        DaoFactory<Connection> daoFactory= new PostgresDaoFactoryImpl();
        try {
            GenericDao<User, Integer> userDao = daoFactory.getDao(daoFactory.getContext(), User.class);
            User user = new User();
            user.setName("MAWA");
            user.setPassword("GLAWA");
            user.setRoleId(0);
            int key = userDao.persist(user);
            System.out.println("key " + key);
            System.out.println("user before: " + user);
            user = userDao.read(key);
            System.out.println("user after: " + user);
            user.setName("BAJ");
            userDao.update(user);
            System.out.println("user after UPDATE: " + user);


        } catch (PersistException e) {
            e.printStackTrace();
        }
    }
}
