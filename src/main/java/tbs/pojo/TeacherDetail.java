package tbs.pojo;

import javax.persistence.*;

@Entity
@Table(name = "teacher",indexes = {@Index(name = "workNoIndex",columnList = "workNo"),@Index(name = "TitleIndex",columnList = "technicalTitle")})
public class TeacherDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(length = 50)
    String technicalTitle;
    @Column(length = 50,unique = true,nullable = false)
    String workNo;

    @OneToOne
    @JoinColumn(name = "userphone",referencedColumnName = "phone",nullable = false,unique = true)
    BasicUser basic;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTechnicalTitle() {
        return technicalTitle;
    }

    public void setTechnicalTitle(String technicalTitle) {
        this.technicalTitle = technicalTitle;
    }

    public String getWorkNo() {
        return workNo;
    }

    public void setWorkNo(String workNo) {
        this.workNo = workNo;
    }
}
