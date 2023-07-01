package tbs.pojo;

import lombok.ToString;
import tbs.framework.annotation.SqlField;
import tbs.framework.sql.model.BasicModel;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "basicuser")
@ToString
public class BasicUser extends BasicModel {

    @Column
    private String uid;
    @Column
    private String name;
    @Column
    private String password;
    @SqlField(isPrimary = true)
    @Column
    private String phone;
    @Column
    private Integer role;
    @SqlField
    private Integer departmentId;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }
}
