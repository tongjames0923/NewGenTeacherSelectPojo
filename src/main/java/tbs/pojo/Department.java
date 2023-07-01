package tbs.pojo;

import tbs.framework.annotation.SqlField;
import tbs.framework.sql.model.BasicModel;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "department")
public class Department extends BasicModel {
    @Id
    @Column
    private int id;

    @SqlField
    private int parentId;
    @Column
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
