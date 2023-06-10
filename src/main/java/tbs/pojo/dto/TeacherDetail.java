package tbs.pojo.dto;

import com.alibaba.fastjson.JSON;

public class TeacherDetail extends BasicUserDetail {

    public static final String BASIC_DATA_SQL=" bu.`name` AS `name`,bu.phone AS `phone`,bu.departmentId AS `departmentId`," +
            "r.roleid AS `role`,r.rolename AS `roleName`,d.departname AS `department`," +
            "st.position as position ,st.pro_title as pro_title, st.workNo as workNo FROM basicuser bu JOIN teacher st ON st.phone=bu.phone JOIN role r ON r.roleid=bu.role JOIN department d ON bu.departmentId=d.id ";
    private String workNo;
    private String position;
    private String pro_title;

    public String getWorkNo() {
        return workNo;
    }

    public void setWorkNo(String workNo) {
        this.workNo = workNo;
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

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
