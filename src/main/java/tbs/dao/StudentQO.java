package tbs.dao;

import tbs.pojo.dto.StudentDetail;
import tbs.utils.sql.query.Queryable;
import tbs.utils.sql.query.SqlField;

@Queryable(StudentDetail.BASIC_DATA_SQL)
public class StudentQO {
    @SqlField(field = "bu.phone",index = 0)
    private String phone;
    @SqlField(field = "bu.name",index = 1)
    private String userName;

    @SqlField(field = "bu.departmentId",index = 2)
    private String departId;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDepartId() {
        return departId;
    }

    public void setDepartId(String departId) {
        this.departId = departId;
    }
}
