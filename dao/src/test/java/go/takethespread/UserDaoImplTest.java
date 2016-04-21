package go.takethespread;

import go.takethespread.exceptions.DbException;
import go.takethespread.impl.UserDaoImpl;

public class UserDaoImplTest {
    public static void main(String[] args) {
        User user0 = new User();
        User user1 = new User();

        user0.setRoleId(0);
        user0.setName("BARRY");
        user0.setPassword("BAPTIST");

        user1.setRoleId(1);
        user1.setName("GARRY");
        user1.setPassword("AXE");

        UserDao dao = new UserDaoImpl();
        try {
            int id0 = dao.insert(user0);
            int id1 = dao.insert(user1);
            System.out.println(dao.getAll());

            user0.setPassword("I'M SAY - THE BAPTIST");
            dao.update(user0, id0);
            System.out.println(dao.getById(id0));

            dao.deleteById(id0);
            System.out.println(dao.getAll());

            dao.deleteByName(user1.getName());
            System.out.println(dao.getAll().isEmpty());

        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
