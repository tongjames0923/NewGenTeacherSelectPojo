package tbs.pojo;

import lombok.ToString;
import tbs.utils.sql.annotations.SqlField;
import tbs.utils.sql.annotations.Updateable;

@Updateable(table = "basicuser")
@ToString
public class BasicUser {

    @SqlField
    private String uid;
    @SqlField
    private String name;
    @SqlField
    private String password;
    @SqlField(isPrimary = true)
    private String phone;
    @SqlField
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
