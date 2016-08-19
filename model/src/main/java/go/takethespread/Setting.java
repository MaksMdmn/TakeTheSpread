package go.takethespread;

import java.io.Serializable;
import java.util.Date;

public class Setting implements Serializable, Identified<Integer>{

    private static final long serialVersionUID = 4L;
    private Integer id = null;
    private Settings name;
    private String value;
    private Date lastUpdate;

    public Setting(){

    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Settings getName() {
        return name;
    }

    public void setName(Settings name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public Integer getId() {
        return null;
    }

    @Override
    public String toString() {
        return "Setting{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", lastUpdate=" + lastUpdate +
                '}';
    }
}
