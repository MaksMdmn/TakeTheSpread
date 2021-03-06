package go.takethespread;

import java.io.Serializable;

public class User implements Serializable, Identified<Integer> {
    private static final long serialVersionUID = 2L;

    private Integer id= null;
    private int roleId;
    private String name;
    private String password;

    public User(){

    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", roleId=" + roleId +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public Integer getId() {
      return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
