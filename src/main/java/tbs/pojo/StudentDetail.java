package tbs.pojo;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "student",indexes = {@Index(name = "studentNo",columnList = "studentnumber"),
@Index(name = "gradeIndex",columnList = "grade")})
public class StudentDetail implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(name = "grade")
    Integer grade;
    @Column(name = "studentnumber",length = 50,unique = true)
    String studentNo;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY,targetEntity = BasicUser.class)
    @JoinColumn(name = "userphone",referencedColumnName = "phone",unique = true,nullable = false)
    private BasicUser basic;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public BasicUser getBasic() {
        return basic;
    }

    public void setBasic(BasicUser basic) {
        this.basic = basic;
    }

    @Override
    public String toString() {
        return "StudentDetail{" +
                "id=" + id +
                ", grade=" + grade +
                ", studentNo='" + studentNo + '\'' +
                ", basic=" + basic +
                '}';
    }
}
