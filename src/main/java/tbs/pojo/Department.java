package tbs.pojo;

import tbs.utils.sql.annotations.SqlField;
import tbs.utils.sql.annotations.Updateable;

@Updateable(table = "department")
public class Department {
    @SqlField
    private int id;
    @SqlField
    private int parentId;
    @SqlField
    private String departname;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getDepartname() {
        return departname;
    }

    public void setDepartname(String departname) {
        this.departname = departname;
    }

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", departname='" + departname + '\'' +
                '}';
    }
}
