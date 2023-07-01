package tbs.pojo;

import tbs.utils.sql.annotations.SqlField;
import tbs.utils.sql.annotations.Updateable;

@Updateable(table = "teacher")
public class Teacher {
    /**
     * 工号
     */
    @SqlField
    String workNo;
    /**
     * 手机号
     */
    @SqlField(isPrimary = true)
    String phone;
    /**
     * 职位
     */
    @SqlField
    String position;
    /**
     * 职称
     */
    @SqlField
    String pro_title;


    public String getWorkNo() {
        return workNo;
    }

    public void setWorkNo(String workNo) {
        this.workNo = workNo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPro_title() {
        return pro_title;
    }

    public void setPro_title(String pro_title) {
        this.pro_title = pro_title;
    }
}
