package tbs.pojo.dto;


import lombok.ToString;

@ToString
public class StudentUserDetail extends BasicUserDetail {
    public static final String BASIC_DATA_SQL = "SELECT bu.`name` AS `name`,bu.phone AS `phone`,bu.departmentId" +
            " AS `departmentId`,st.studentNo AS `studentNo`,st.cla AS `clas`,st.grade AS `grade`,st.score as `score`,r.roleid AS `role`," +
            "r.rolename AS `roleName`,d.departname AS `department` FROM basicuser bu JOIN student st ON " +
            "st.phone=bu.phone JOIN role r ON r.roleid=bu.role JOIN department d ON bu.departmentId=d.id ";
    String studentNo, clas;
    private int grade;
    private double score;

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
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

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

}
