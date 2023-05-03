package tbs.pojo.dto;

import com.alibaba.fastjson.JSON;


public class StudentDetail {
    public static final String BASIC_DATA_SQL = "SELECT bu.`name` AS `name`,bu.phone AS `phone`,bu.departmentId AS `departmentId`,st.studentNo AS `studentNo`,st.cla AS `clas`,st.grade AS `grade`,r.roleid AS `role`,r.rolename AS `roleName`,d.departname AS `department` FROM basicuser bu JOIN student st ON st.phone=bu.phone JOIN role r ON r.roleid=bu.role JOIN department d ON bu.departmentId=d.id ";
    private String name, phone, studentNo, clas, roleName, department;
    private int grade, role, departmentId;

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public String getClas() {
        return clas;
    }

    public void setClas(String clas) {
        this.clas = clas;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
